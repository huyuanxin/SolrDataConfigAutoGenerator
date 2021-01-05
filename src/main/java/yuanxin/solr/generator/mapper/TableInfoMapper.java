package yuanxin.solr.generator.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;

import org.springframework.stereotype.Repository;
import yuanxin.solr.generator.entity.TableInfo;

import java.util.List;


/**
 * @author huyuanxin
 */
@Repository
public interface TableInfoMapper extends BaseMapper<TableInfo> {
    /**
     * 重置是否构建为否
     *
     * @return 是否成功
     */
    int initTableBuild();

    /**
     * 获得已构建的表id
     *
     * @return 表id
     */
    List<Integer> getBuiltTableId();
}
