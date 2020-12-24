package yuanxin.solr.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import yuanxin.solr.generator.entity.TableInfo;
import org.springframework.stereotype.Repository;


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
}
