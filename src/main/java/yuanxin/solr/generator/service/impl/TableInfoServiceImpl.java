package yuanxin.solr.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import yuanxin.solr.generator.entity.TableInfo;
import yuanxin.solr.generator.mapper.TableInfoMapper;
import yuanxin.solr.generator.service.TableInfoService;
import org.springframework.stereotype.Service;


/**
 * Mybatis-plus的table_info IService CURD接口
 *
 * @author huyuanxin
 */
@Service
public class TableInfoServiceImpl extends ServiceImpl<TableInfoMapper, TableInfo> implements TableInfoService {

}
