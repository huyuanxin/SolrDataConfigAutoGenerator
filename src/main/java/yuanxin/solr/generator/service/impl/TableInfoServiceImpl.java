package yuanxin.solr.generator.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;
import yuanxin.solr.generator.entity.TableInfo;
import yuanxin.solr.generator.mapper.TableInfoMapper;
import yuanxin.solr.generator.service.TableInfoService;


/**
 * Mybatis-plus的table_info IService CURD接口
 *
 * @author huyuanxin
 */
@Service
public class TableInfoServiceImpl extends ServiceImpl<TableInfoMapper, TableInfo> implements TableInfoService {

}
