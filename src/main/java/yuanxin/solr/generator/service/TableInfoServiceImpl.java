package yuanxin.solr.generator.service;

import org.springframework.stereotype.Service;
import yuanxin.solr.generator.api.TableInfoService;
import yuanxin.solr.generator.dao.SolrTableMapper;
import yuanxin.solr.generator.entity.output.ColumnNameInfoForOutput;
import yuanxin.solr.generator.po.TableInfo;

import java.util.List;

/**
 * 获得相关的表信息
 *
 * @author huyuanxin
 */
@Service("TableInfoService")
public class TableInfoServiceImpl implements TableInfoService {
    final SolrTableMapper solrTableMapper;


    public TableInfoServiceImpl(SolrTableMapper solrTableMapper) {
        this.solrTableMapper = solrTableMapper;
    }

    /**
     * 获得尚未构建的表 {@link TableInfo}
     *
     * @param build 是否构建的状态 {@link Boolean}
     * @return 未构建的表 {@link TableInfo}
     */
    @Override
    public List<TableInfo> getTableInfoWithBuild(boolean build) {
        return solrTableMapper.getBuildTableInfoWithBuild(build);
    }

    /**
     * 获得数据库列的具体信息
     *
     * @param databaseName 数据库名 {@link String}
     * @param tableName    表名 {@link String}
     * @return 数据库列的具体信息 {@link ColumnNameInfoForOutput}
     */
    @Override
    public List<ColumnNameInfoForOutput> getTableColumnInfo(String databaseName, String tableName) {
        return solrTableMapper.getTableColumnInfo(databaseName, tableName);
    }
}
