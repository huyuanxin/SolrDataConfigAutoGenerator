package yuanxin.solr.generator.service;

import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.stereotype.Service;
import yuanxin.solr.generator.entity.TableInfo;
import yuanxin.solr.generator.model.*;

import java.util.List;

/**
 * 复杂数据库操作的接口
 *
 * @author huyuanxin
 */
@Service
public interface DatabaseService {
    /**
     * 更新数据表table_info
     *
     * @param tableIdList 前端传入的数据 {@link List<Integer>}
     * @param built       是否构建的状态 {@link Boolean}
     * @return 是否成功 {@link Boolean}
     */
    boolean updateTableInfoBuiltStatus(List<Integer> tableIdList, boolean built);

    /**
     * 更新数据表table_info
     *
     * @param id   id {@link Integer}
     * @param save 是否构建的状态 {@link Boolean}
     * @return 是否成功 {@link Boolean}
     */
    boolean updateTableInfoSavedStatus(int id, boolean save);

    /**
     * 重置是否构建为否
     *
     * @return 是否成功 {@link Boolean}
     */
    boolean initTableBuild();

    /**
     * 保存需要构建的字段
     *
     * @param saveInput 前端的传输的数据 {@link SaveInput}
     * @return 是否保存成功的信息 {@link SolrResult}
     */
    SolrResult saveGeneratorColumn(SaveInput saveInput);

    /**
     * 通过表id获得表的字段信息
     *
     * @param getColumnInput 表对应的id {@link GetColumnInput}
     * @return 表的字段信息 {@link List<ColumnInfo>}
     */
    Page<ColumnInfo> getTableColumn(GetColumnInput getColumnInput);

    /**
     * 通过表id获得表的字段信息
     *
     * @param tableId 表对应的id {@link Integer}
     * @return 表的字段信息 {@link List<ColumnInfo>}
     */
    List<ColumnInfo> getAllTableColumn(int tableId);

    /**
     * 获得已经构建或者已经保存的表
     *
     * @param getTableInput 输入 {@link GetTableInput}
     * @return 已经构建或者已经保存的表 {@link Page<TableInfo>}
     */
    Page<TableInfo> getTableWithSavedStatus(GetTableInput getTableInput);

    /**
     * 删除需要生成表的操作
     *
     * @param tableIdList 表对应的字段 {@link List<Integer>}
     * @return 是否删除成功的相关信息 {@link SolrResult}
     */
    SolrResult deleteBuiltOrSavedTable(List<Integer> tableIdList);
}
