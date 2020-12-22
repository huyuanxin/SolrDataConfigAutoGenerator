package yuanxin.solr.generator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import yuanxin.solr.generator.api.GeneratorService;
import yuanxin.solr.generator.dao.SolrTableMapper;
import yuanxin.solr.generator.dto.InputDTO;
import yuanxin.solr.generator.entity.input.BuiltDataBase;
import yuanxin.solr.generator.entity.input.ColumnNameInfoForInput;
import yuanxin.solr.generator.entity.input.DataBase;
import yuanxin.solr.generator.entity.solr.DataSource;
import yuanxin.solr.generator.entity.solr.Entity;
import yuanxin.solr.generator.entity.solr.Field;

import java.util.ArrayList;
import java.util.List;

/**
 * GeneratorService的实现类
 *
 * @author huyuanxin
 */
@Service("GeneratorService")
@PropertySource(value = {"classpath:/application.properties"})
public class GeneratorServiceImpl implements GeneratorService {
    @Value("${spring.datasource.driver-class-name}")
    public String dataConfigDriver;

    @Value("${spring.datasource.url}")
    public String dataConfigUrl;

    @Value("${spring.datasource.username}")
    public String dataConfigUser;

    @Value("${spring.datasource.password}")
    public String dataConfigPassword;

    final SolrTableMapper solrTableMapper;

    final String DATABASE_TYPE_DATETIME = "datetime";
    final String DATABASE_TYPE_TIME = "time";

    final String DynamicFIELD_PDATE_TYPE_NAME = "yuanxin_pdate_";
    final String DynamicFIELD_STRING_TYPE_NAME = "yuanxin_string_";

    @Autowired
    public GeneratorServiceImpl(SolrTableMapper solrTableMapper) {
        this.solrTableMapper = solrTableMapper;
    }

    /**
     * 生成 {@link DataSource}
     *
     * @param inputDTO 输入的需要生成的信息 {@link InputDTO}
     * @return 生成的 {@link DataSource}
     */
    @Override
    public List<DataSource> generatorDataSource(InputDTO inputDTO) {
        List<BuiltDataBase> builtDataBaseList = inputDTO.getBuiltDataBaseInfo();
        List<DataBase> dataBaseList = inputDTO.getDataBaseList();
        List<DataSource> dataSourceList = new ArrayList<>();
        for (DataBase dataBase : dataBaseList
        ) {
            DataSource dataSource = new DataSource(dataBase.getDataBaseName(), dataConfigDriver, dataConfigUrl, dataConfigUser, dataConfigPassword);
            dataSourceList.add(dataSource);
        }
        for (BuiltDataBase buildDataBase : builtDataBaseList
        ) {
            DataSource dataSource = new DataSource(buildDataBase.getDataBaseName(), dataConfigDriver, dataConfigUrl, dataConfigUser, dataConfigPassword);
            dataSourceList.add(dataSource);
        }
        return dataSourceList;
    }

    /**
     * 生成 {@link Entity}
     *
     * @param inputDTO 输入的需要生成的信息 {@link InputDTO}
     * @return 生成的 {@link Entity}
     */
    @Override
    public List<Entity> generatorEntity(InputDTO inputDTO) {
        List<DataBase> dataBaseList = inputDTO.getDataBaseList();
        List<BuiltDataBase> builtDataBaseList = inputDTO.getBuiltDataBaseInfo();

        for (BuiltDataBase buildDataBase : builtDataBaseList
        ) {
            // 表内查询已构建的信息,并加入到生成队列中
            List<ColumnNameInfoForInput> columnNameInfoForInputList = solrTableMapper.getBuiltTableInfoForInput(buildDataBase.getDataBaseName(), buildDataBase.getTableName());
            if (columnNameInfoForInputList.size() > 0) {
                dataBaseList.add(new DataBase(
                                buildDataBase.getDataBaseName(),
                                buildDataBase.getTableName(),
                                solrTableMapper.getBuiltTableInfoForInput(
                                        buildDataBase.getDataBaseName(),
                                        buildDataBase.getTableName()
                                )
                        )
                );
            }
        }
        // 记录已构建的列和更新经构建的表
        clearBuiltDataBaseInfo();
        initTableInfo();
        updateBuiltDataBaseInfo(dataBaseList);
        updateTableInfo(dataBaseList);
        // 构建列
        List<Entity> entityList = new ArrayList<>();
        for (DataBase dataBase : dataBaseList
        ) {
            entityList.add(dataBaseToEntity(dataBase));
        }
        return entityList;
    }

    /**
     * {@link DataBase}转换为 {@link Entity}
     *
     * @param dataBase 需要转换的 {@link DataBase}
     * @return 转换的 {@link Entity}
     */
    private Entity dataBaseToEntity(DataBase dataBase) {
        List<ColumnNameInfoForInput> columnNameInfoForInputList = dataBase.getColumnNameInfoForInputList();
        List<Field> fieldList = columnNameListToField(columnNameInfoForInputList);
        return new Entity(dataBase.getTableName(),
                dataBase.getDataBaseName(),
                generatorQuerySqlCommand(dataBase),
                generatorDeltaImportCommand(dataBase),
                generatorDeltaQuerySqlCommand(dataBase),
                fieldList);
    }

    /**
     * 生成DeltaImport
     *
     * @param dataBase 需要生成DeltaImport的 {@link DataBase}
     * @return 生成的sql语句 {@link String}
     */
    private String generatorDeltaImportCommand(DataBase dataBase) {
        String sql = generatorQuerySqlCommand(dataBase);
        return sql + " where id='${dataimporter.delta.id}'";
    }

    /**
     * 生成DeltaQuery
     *
     * @param dataBase 需要生成DeltaQuery的 {@link DataBase}
     * @return 生成的sql语句 {@link String}
     */
    private String generatorDeltaQuerySqlCommand(DataBase dataBase) {
        return "Select id Form " + dataBase.getTableName() + " where create_time > '${dataimporter.last_index_time}'";
    }

    /**
     * 生成QueryCommand
     *
     * @param dataBase 需要生成Query的 {@link DataBase}
     * @return 生成的sql语句 {@link String}
     */
    private String generatorQuerySqlCommand(DataBase dataBase) {
        List<ColumnNameInfoForInput> columnNameInfoForInputList = dataBase.getColumnNameInfoForInputList();
        List<String> columnNameList = new ArrayList<>();
        for (ColumnNameInfoForInput columnInfoName : columnNameInfoForInputList
        ) {
            if (!"".equals(columnInfoName.getColumnName())) {
                columnNameList.add(columnInfoName.getColumnName());
            }
        }
        // 此处为了避免传入的数据有id这个字段
        columnNameList.removeIf("id"::equals);
        StringBuilder sql = new StringBuilder();
        // 构造Query
        sql.append("Select CONCAT(").append("'").append(dataBase.getTableName()).append("'").append(",id) as id,");
        int size = columnNameList.size() - 1;
        for (int i = 0; i < size; i++) {
            sql.append("`").append(columnNameList.get(i)).append("`,");
        }
        sql.append("`").append(columnNameList.get(size)).append("` ").append("From ").append(dataBase.getTableName());
        return sql.toString();
    }

    /**
     * 把列名转换成{@link Field}
     *
     * @param columnNameInfoForInputList 列名列 {@link List<ColumnNameInfoForInput>}
     * @return 列名生成的 {@link List<Field>}
     */
    private List<Field> columnNameListToField(List<ColumnNameInfoForInput> columnNameInfoForInputList) {
        List<Field> fieldList = new ArrayList<>();
        columnNameInfoForInputList.removeIf(it -> "id".equals(it.getColumnName()));
        fieldList.add(new Field("id", "id"));
        for (ColumnNameInfoForInput columnName : columnNameInfoForInputList
        ) {
            switch (columnName.getColumnName()) {
                case DATABASE_TYPE_DATETIME:
                case DATABASE_TYPE_TIME: {
                    fieldList.add(new Field(columnName.getColumnName(), DynamicFIELD_PDATE_TYPE_NAME + columnName.getColumnName()));
                    break;
                }
                default: {
                    fieldList.add(new Field(columnName.getColumnName(), DynamicFIELD_STRING_TYPE_NAME + columnName.getColumnName()));
                    break;
                }
            }
        }
        return fieldList;
    }

    /**
     * 清空BuildTableInfo
     */
    private void clearBuiltDataBaseInfo() {
        solrTableMapper.clearBuiltDataBaseInfo();
    }

    /**
     * 更新BuildTableInfo
     *
     * @param dataBaseList 更新的队列 {@link List<DataBase>}
     */
    private void updateBuiltDataBaseInfo(List<DataBase> dataBaseList) {
        for (DataBase dataBase : dataBaseList
        ) {
            List<ColumnNameInfoForInput> columnInfoList = dataBase.getColumnNameInfoForInputList();
            for (ColumnNameInfoForInput columnInfo : columnInfoList
            ) {
                solrTableMapper.insertToBuildTableInfo(
                        dataBase.getDataBaseName(),
                        dataBase.getTableName(),
                        columnInfo.getColumnName(),
                        columnInfo.getColumnType(),
                        columnInfo.getColumnInfo());
            }
        }
    }

    /**
     * 初始化TableInfo
     */
    private void initTableInfo() {
        solrTableMapper.initTableInfo();
    }

    /**
     * 更新已构建的到TableInfo
     *
     * @param dataBaseList 数据库队列 {@link List<DataBase>}
     */
    private void updateTableInfo(List<DataBase> dataBaseList) {
        for (DataBase dataBase : dataBaseList
        ) {
            solrTableMapper.updateTableInfo(dataBase.getDataBaseName(), dataBase.getTableName(), true);
        }
    }
}
