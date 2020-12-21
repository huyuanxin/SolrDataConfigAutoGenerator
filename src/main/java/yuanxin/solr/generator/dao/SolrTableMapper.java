package yuanxin.solr.generator.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import yuanxin.solr.generator.entity.input.ColumnNameInfoForInput;
import yuanxin.solr.generator.entity.output.ColumnNameInfoForOutput;
import yuanxin.solr.generator.po.TableInfo;

import java.util.List;

/**
 * @author huyuanxin
 */
@Mapper
@Repository
public interface SolrTableMapper {
    /**
     * 获得尚未构建的表 {@link TableInfo}
     *
     * @param build 构建的状态
     * @return 未构建的表 {@link TableInfo}
     */
    List<TableInfo> getBuildTableInfoWithBuild(
            @Param("build") boolean build);

    /**
     * 更新
     *
     * @param databaseName 数据库名 {@link String}
     * @param tableName    表名 {@link String}
     * @param status       更新的状态 {@link Integer}
     * @return 是否成功
     */
    int updateTableInfo(
            @Param("dataBaseName") String databaseName,
            @Param("tableName") String tableName,
            @Param("status") boolean status);

    /**
     * 获得数据库列的具体信息
     *
     * @param databaseName 数据库名 {@link String}
     * @param tableName    表名 {@link String}
     * @return 数据库列的具体信息 {@link ColumnNameInfoForOutput}
     */
    List<ColumnNameInfoForOutput> getTableColumnInfo(
            @Param("databaseName") String databaseName,
            @Param("tableName") String tableName);

    /**
     * 获得已构建的表信息
     *
     * @param databaseName 数据库名 {@link String}
     * @param tableName    表名 {@link String}
     * @return ColumnNameInfoForOutput {@link ColumnNameInfoForOutput}
     */
    List<ColumnNameInfoForOutput> getBuiltTableInfoForOutput(
            @Param("databaseName") String databaseName,
            @Param("tableName") String tableName);

    /**
     * 获得已构建的表信息
     *
     * @param databaseName 数据库名 {@link String}
     * @param tableName    表名 {@link String}
     * @return ColumnNameInfoForInput {@link ColumnNameInfoForOutput}
     */
    List<ColumnNameInfoForInput> getBuiltTableInfoForInput(
            @Param("databaseName") String databaseName,
            @Param("tableName") String tableName);

    /**
     * 清空BuildTableInfo
     */
    void clearBuiltDataBaseInfo();

    /**
     * 插入BuildTableInfo
     *
     * @param databaseName 数据库名 {@link String}
     * @param tableName    表名 {@link String}
     * @param columnName   列名 {@link String}
     * @param columnType   列的类型 {@link String}
     * @param columnInfo   列的描述 {@link String}
     */
    void insertToBuildTableInfo(@Param("databaseName") String databaseName,
                                @Param("tableName") String tableName,
                                @Param("columnName") String columnName,
                                @Param("columnType") String columnType,
                                @Param("columnInfo") String columnInfo);

    /**
     * 初始化TableInfo
     */
    void initTableInfo();
}
