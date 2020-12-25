package yuanxin.solr.generator.service.impl;

import yuanxin.solr.generator.entity.BuiltTableInfo;
import yuanxin.solr.generator.entity.TableInfo;
import yuanxin.solr.generator.model.GeneratorInput;
import yuanxin.solr.generator.model.solr.DataSource;
import yuanxin.solr.generator.model.solr.Entity;
import yuanxin.solr.generator.model.solr.Field;
import yuanxin.solr.generator.service.BuiltTableInfoService;
import yuanxin.solr.generator.service.GeneratorService;
import yuanxin.solr.generator.service.TableInfoService;
import yuanxin.solr.generator.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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
    final String DATABASE_TYPE_TIME = "time";
    final String TemplateName = "data-config";
    final String DATA_CONFIG_DEFAULT_NAME = "data-config.xml";
    final String dataSourceInThymeleaf = "dataSourceList";
    final String entityListInThymeleaf = "entityList";

    @Value("${data-config.generator.location}")
    private String fileLocation;

    final BuiltTableInfoService builtTableInfoService;
    final TableInfoService tableInfoService;

    final Util util;

    @Autowired
    public GeneratorServiceImpl(BuiltTableInfoService builtTableInfoService, TableInfoService tableInfoService, Util util) {
        this.builtTableInfoService = builtTableInfoService;
        this.tableInfoService = tableInfoService;
        this.util = util;
    }

    /**
     * 生成 {@link DataSource}
     *
     * @param generatorInput 前端的输入
     * @return 生成的 {@link DataSource}
     */
    @Override
    public List<DataSource> generatorDataSource(GeneratorInput generatorInput) {
        List<DataSource> dataSourceList = new ArrayList<>();
        List<TableInfo> tableInfoList = util.getTableInfo(generatorInput);
        for (TableInfo tableInfo : tableInfoList
        ) {
            dataSourceList.add(util.tableInfoToDataSource(tableInfo));
        }
        return dataSourceList;
    }

    /**
     * 生成 {@link Entity}
     *
     * @param generatorInput 前端的输入
     * @return 成的 {@link Entity}
     */
    @Override
    public List<Entity> generatorEntity(GeneratorInput generatorInput) {
        List<Entity> entityList = new ArrayList<>();
        List<TableInfo> tableInfoList = util.getTableInfo(generatorInput);
        for (TableInfo tableInfo : tableInfoList
        ) {
            List<BuiltTableInfo> builtTableInfo = util.getBuiltTableInfo(tableInfo);
            Entity entity = new Entity(tableInfo.getTableName(),
                    tableInfo.getDatabaseName(),
                    util.generatorQuerySqlCommand(builtTableInfo),
                    util.generatorDeltaImportCommand(builtTableInfo),
                    util.generatorDeltaQuerySqlCommand(builtTableInfo),
                    generatorField(builtTableInfo));
            entityList.add(entity);
        }
        return entityList;
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
        return fieldList;
    }

    /**
     * 生成Xml文件的主函数
     *
     * @param generatorInput 输入的数据 {@link GeneratorInput}
     * @return 是否生成成功 {@link Boolean}
     */
    @Override
    public boolean generator(GeneratorInput generatorInput) {
        try {
            Context context = new Context();
            FileWriter writer = util.newFileWriter(fileLocation + DATA_CONFIG_DEFAULT_NAME);
            TemplateEngine templateEngine = util.newTemplateEngine();
            context.setVariable(dataSourceInThymeleaf, generatorDataSource(generatorInput));
            context.setVariable(entityListInThymeleaf, generatorEntity(generatorInput));
            templateEngine.process(TemplateName, context, writer);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
