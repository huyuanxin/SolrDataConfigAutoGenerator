package yuanxin.solr.generator.service;

import org.springframework.stereotype.Service;
import yuanxin.solr.generator.api.GeneratorService;
import yuanxin.solr.generator.dto.InputDTO;
import yuanxin.solr.generator.entity.*;

import java.util.ArrayList;
import java.util.List;

/**
 * GeneratorService的实现类
 *
 * @author huyuanxin
 */
@Service("GeneratorService")
public class GeneratorServiceImpl implements GeneratorService {
    final String DATABASE_TYPE_DATETIME = "datetime";
    final String DATABASE_TYPE_TIME = "time";

    /**
     * 生成 {@link DataSource}
     *
     * @param inputDTO 输入的需要生成的信息 {@link InputDTO}
     * @return 生成的 {@link DataSource}
     */
    @Override
    public List<DataSource> generatorDataSource(InputDTO inputDTO) {
        List<DataBase> dataBaseList = inputDTO.getDataBaseList();
        List<DataSource> dataSourceList = new ArrayList<>();
        for (DataBase dataBase : dataBaseList
        ) {
            dataSourceList.add(dataSourceToDataSource(dataBase));
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
        List<ColumnNameInfo> columnNameInfoList = dataBase.getColumnNameInfoList();
        List<Field> fieldList = columnNameListToField(columnNameInfoList);
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
        List<ColumnNameInfo> columnNameInfoList = dataBase.getColumnNameInfoList();
        List<String> columnNameList = new ArrayList<>();
        for (ColumnNameInfo columnInfoName : columnNameInfoList
        ) {
            if (!"".equals(columnInfoName.getColumnName())) {
                columnNameList.add(columnInfoName.getColumnName());
            }
        }
        // 此处为了避免传入的数据有id这个字段
        columnNameList.removeIf("id"::equals);
        StringBuilder sql = new StringBuilder();
        // 构造Query
        sql.append("Select CONCAT('tableName+',id) as id,");
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
     * @param columnNameInfoList 列名列 {@link List<ColumnNameInfo>}
     * @return 列名生成的 {@link List<Field>}
     */
    private List<Field> columnNameListToField(List<ColumnNameInfo> columnNameInfoList) {
        List<Field> fieldList = new ArrayList<>();
        for (ColumnNameInfo columnName : columnNameInfoList
        ) {
            switch (columnName.getColumnName()) {
                case DATABASE_TYPE_DATETIME:
                case DATABASE_TYPE_TIME: {
                    fieldList.add(new Field(columnName.getColumnName(), "yuanxin_pdate_" + columnName.getColumnName()));
                    break;
                }
                default: {
                    fieldList.add(new Field(columnName.getColumnName(), "yuanxin_string_" + columnName.getColumnName()));
                    break;
                }
            }
        }
        return fieldList;
    }

    /**
     * 把DataBase转换为 {@link DataBase}
     *
     * @param dataBase 需要转换为{@link DataSource}的 {@link DataBase}
     * @return 转换的 {@link DataSource}
     */
    private DataSource dataSourceToDataSource(DataBase dataBase) {
        return new DataSource(
                dataBase.getDataBaseName(),
                dataBase.getDriverName(),
                dataBase.getDataBaseUrl(),
                dataBase.getDataBaseUserName(),
                dataBase.getDataBasePassword());
    }
}
