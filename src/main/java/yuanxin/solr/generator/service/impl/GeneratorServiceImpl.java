package yuanxin.solr.generator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import yuanxin.solr.generator.entity.BuiltTableInfo;
import yuanxin.solr.generator.entity.TableInfo;
import yuanxin.solr.generator.mapper.TableInfoMapper;
import yuanxin.solr.generator.model.SolrResult;
import yuanxin.solr.generator.model.solr.DataSource;
import yuanxin.solr.generator.model.solr.Entity;
import yuanxin.solr.generator.model.solr.Field;
import yuanxin.solr.generator.service.BuiltTableInfoService;
import yuanxin.solr.generator.service.GeneratorService;
import yuanxin.solr.generator.service.TableInfoService;
import yuanxin.solr.generator.util.GeneratorUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成data-config的实现
 *
 * @author huyuanxin
 */
@Service
public class GeneratorServiceImpl implements GeneratorService {
    final String DynamicFIELD_DATE_TYPE_NAME = "yuanxin_pdate_";
    final String DynamicFIELD_STRING_TYPE_NAME = "yuanxin_string_";
    final String DATABASE_TYPE_DATETIME = "datetime";
    final String DATABASE_TYPE_DATETIME2 = "datetime2";
    final String DATABASE_TYPE_TIME = "time";
    final String TemplateName = "data-config";
    final String DATA_CONFIG_DEFAULT_NAME = "data-config.xml";
    final String dataSourceInThymeleaf = "dataSourceList";
    final String entityListInThymeleaf = "entityList";

    @Value("${data-config.generator.location}")
    private String fileLocation;

    final BuiltTableInfoService builtTableInfoService;
    final TableInfoService tableInfoService;
    final TableInfoMapper tableInfoMapper;
    final GeneratorUtil generatorUtil;

    @Autowired
    public GeneratorServiceImpl(BuiltTableInfoService builtTableInfoService, TableInfoService tableInfoService, TableInfoMapper tableInfoMapper, GeneratorUtil generatorUtil) {
        this.builtTableInfoService = builtTableInfoService;
        this.tableInfoService = tableInfoService;
        this.tableInfoMapper = tableInfoMapper;
        this.generatorUtil = generatorUtil;
    }

    /**
     * 生成 {@link DataSource}
     *
     * @param tableIdList 前端的输入 {@link List<Integer>}
     * @return 生成的 {@link DataSource}
     */
    @Override
    public List<DataSource> generatorDataSource(List<Integer> tableIdList) {
        List<DataSource> dataSourceList = new ArrayList<>();
        List<TableInfo> tableInfoList = generatorUtil.getTableInfo(tableIdList);
        if (tableInfoList != null) {
            for (TableInfo tableInfo : tableInfoList
            ) {
                dataSourceList.add(generatorUtil.tableInfoToDataSource(tableInfo));
            }
            return generatorUtil.removeDuplicate(dataSourceList);
        }
        return new ArrayList<>();
    }

    /**
     * 生成 {@link Entity}
     *
     * @param tableIdList 前端的输入 {@link List<Integer>}
     * @return 成的 {@link Entity}
     */
    @Override
    public List<Entity> generatorEntity(List<Integer> tableIdList) {
        List<Entity> entityList = new ArrayList<>();
        List<TableInfo> tableInfoList = generatorUtil.getTableInfo(tableIdList);
        if (tableInfoList != null && tableIdList.size() != 0) {
            for (TableInfo tableInfo : tableInfoList
            ) {
                List<BuiltTableInfo> builtTableInfo = generatorUtil.getBuiltTableInfo(tableInfo);
                if (builtTableInfo != null && builtTableInfo.size() != 0) {
                    Entity entity = new Entity(
                            tableInfo.getTableName(),
                            tableInfo.getDatabaseName(),
                            generatorUtil.generatorQuerySqlCommand(builtTableInfo),
                            generatorUtil.generatorDeltaImportCommand(builtTableInfo),
                            generatorUtil.generatorDeltaQuerySqlCommand(builtTableInfo),
                            generatorField(builtTableInfo));
                    entityList.add(entity);
                }
            }
            return generatorUtil.removeDuplicate(entityList);
        }
        return new ArrayList<>();
    }

    /**
     * 生成FieldList
     *
     * @param builtTableInfoList 通过前端输入获得的 {@link BuiltTableInfo}
     * @return 生成的 {@link Field}
     */
    @Override
    public List<Field> generatorField(List<BuiltTableInfo> builtTableInfoList) {
        List<Field> fieldList = new ArrayList<>();
        builtTableInfoList.removeIf(it -> "id".equals(it.getColumnName()));
        fieldList.add(new Field("id", "id"));
        for (BuiltTableInfo buildTableInfo : builtTableInfoList
        ) {
            switch (buildTableInfo.getColumnName()) {
                case DATABASE_TYPE_DATETIME:
                case DATABASE_TYPE_DATETIME2:
                case DATABASE_TYPE_TIME: {
                    fieldList.add(new Field(buildTableInfo.getColumnName(), DynamicFIELD_DATE_TYPE_NAME + buildTableInfo.getColumnName()));
                    break;
                }
                default: {
                    fieldList.add(new Field(buildTableInfo.getColumnName(), DynamicFIELD_STRING_TYPE_NAME + buildTableInfo.getColumnName()));
                    break;
                }
            }
        }
        return generatorUtil.removeDuplicate(fieldList);
    }

    /**
     * 生成Xml文件的主函数
     *
     * @param tableIdList 输入的数据 {@link List<Integer>}
     * @return 是否生成成功 {@link Boolean}
     */
    @Override
    public SolrResult generator(List<Integer> tableIdList) {
        List<Integer> builtTableIdList = tableInfoMapper.getBuiltTableId();
        tableIdList.addAll(builtTableIdList);
        try {
            List<DataSource> dataSourceList = generatorDataSource(tableIdList);
            List<Entity> entityList = generatorEntity(tableIdList);
            if (dataSourceList == null || dataSourceList.size() == 0) {
                generatorUtil.newFileWriter(fileLocation + DATA_CONFIG_DEFAULT_NAME);
                return new SolrResult(false, "生成失败,dataSource是空的");
            }
            if (entityList == null || entityList.size() == 0) {
                generatorUtil.newFileWriter(fileLocation + DATA_CONFIG_DEFAULT_NAME);
                return new SolrResult(false, "生成失败,entity是空的");
            }
            Context context = new Context();
            FileWriter writer = generatorUtil.newFileWriter(fileLocation + DATA_CONFIG_DEFAULT_NAME);
            TemplateEngine templateEngine = generatorUtil.newTemplateEngine();
            context.setVariable(dataSourceInThymeleaf, dataSourceList);
            context.setVariable(entityListInThymeleaf, entityList);
            templateEngine.process(TemplateName, context, writer);
            writer.close();
            return new SolrResult(true, "生成成功");
        } catch (IOException e) {
            return new SolrResult(false, e.toString());
        }
    }
}
