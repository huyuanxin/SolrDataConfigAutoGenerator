package yuanxin.solr.generator.service;

import yuanxin.solr.generator.entity.TableInfo;
import yuanxin.solr.generator.model.ColumnInfo;
import yuanxin.solr.generator.model.GeneratorInput;
import yuanxin.solr.generator.model.SaveInput;
import org.springframework.stereotype.Service;

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
     * @param generatorInput 前端传入的数据
     * @param built          是否构建的状态
     * @return 成功的条数
     */
    boolean updateTableInfoBuiltStatus(GeneratorInput generatorInput, boolean built);

    /**
     * 更新数据表table_info
     *
     * @param id   id
     * @param save 是否构建的状态
     * @return 成功的条数
     */
    boolean updateTableInfoSavedStatus(int id, boolean save);

    /**
     * 重置是否构建为否
     *
     * @return 是否成功
     */
    int initTableBuild();

    /**
     * 保存需要构建的字段
     *
     * @param saveInput 前端的传输的数据
     * @return 保存条数
     */
    boolean saveGeneratorColumn(SaveInput saveInput);

    /**
     * 通过表id获得表的字段信息
     *
     * @param tableId 表对应的id
     * @return 表的字段信息
     */
    List<ColumnInfo> getTableColumn(int tableId);

    /**
     * 获得已经构建或者已经保存的表
     *
     * @return 已经构建或者已经保存的表
     */
    List<TableInfo> getBuiltOrSavedTable();

    /**
     * 删除需要生成表的操作
     *
     * @param tableIdList 表对应的字段
     * @return 是否删除成功
     */
    boolean deleteBuiltOrSavedTable(List<Integer> tableIdList);
}
