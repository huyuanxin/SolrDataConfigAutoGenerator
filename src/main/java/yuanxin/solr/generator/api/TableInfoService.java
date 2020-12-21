package yuanxin.solr.generator.api;

import org.springframework.stereotype.Service;
import yuanxin.solr.generator.entity.output.ColumnNameInfoForOutput;
import yuanxin.solr.generator.po.TableInfo;

import java.util.List;

/**
 * @author huyuanxin
 */
@Service("TableInfoService")
public interface TableInfoService {
    /**
     * 获得尚未构建的表 {@link TableInfo}
     *
     * @param build 是否构建的状态
     * @return 未构建的表 {@link TableInfo}
     */
    List<TableInfo> getTableInfoWithBuild(boolean build);

    /**
     * 获得数据库列的具体信息
     *
     * @param databaseName 数据库名 {@link String}
     * @param tableName    表名 {@link String}
     * @return 数据库列的具体信息  {@link ColumnNameInfoForOutput}
     */
    List<ColumnNameInfoForOutput> getTableColumnInfo(String databaseName, String tableName);
}
