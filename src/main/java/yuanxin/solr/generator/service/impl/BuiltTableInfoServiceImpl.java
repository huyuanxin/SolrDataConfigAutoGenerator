package yuanxin.solr.generator.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;
import yuanxin.solr.generator.entity.BuiltTableInfo;
import yuanxin.solr.generator.mapper.BuiltTableInfoMapper;
import yuanxin.solr.generator.service.BuiltTableInfoService;


/**
 * Mybatis-plus的built_table_info IService CURD接口
 *
 * @author huyuanxin
 */
@Service
public class BuiltTableInfoServiceImpl extends ServiceImpl<BuiltTableInfoMapper, BuiltTableInfo> implements BuiltTableInfoService {

}
