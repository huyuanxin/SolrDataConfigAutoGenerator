package yuanxin.solr.generator.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import yuanxin.solr.generator.entity.BuiltTableInfo;

import java.util.List;


/**
 * @author huyuanxin
 */
@Repository
public interface BuiltTableInfoMapper extends BaseMapper<BuiltTableInfo> {
    /**
     * 获得字段的信息
     *
     * @param databaseName 数据库名称
     * @param tableName    表名称
     * @return 获得的字段信息
     */
    List<BuiltTableInfo> getAllColumnInfoList(
            @Param("databaseName") String databaseName,
            @Param("tableName") String tableName);

    /**
     * 获得字段的信息
     *
     * @param page         分页参数
     * @param databaseName 数据库名称
     * @param tableName    表名称
     * @return 获得的字段信息
     */
    List<BuiltTableInfo> getColumnInfoPage(
            Page<?> page,
            @Param("databaseName") String databaseName,
            @Param("tableName") String tableName);
}
