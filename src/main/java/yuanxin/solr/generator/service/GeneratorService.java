package yuanxin.solr.generator.service;

import org.springframework.stereotype.Service;
import yuanxin.solr.generator.entity.BuiltTableInfo;
import yuanxin.solr.generator.model.SolrResult;
import yuanxin.solr.generator.model.solr.DataSource;
import yuanxin.solr.generator.model.solr.Entity;
import yuanxin.solr.generator.model.solr.Field;

import java.util.List;

/**
 * 生成data-config的接口
 *
 * @author huyuanxin
 */
@Service
public interface GeneratorService {
    /**
     * 生成 {@link DataSource}
     *
     * @param tableIdList 前端的输入 {@link List<Integer>}
     * @return 生成的 {@link DataSource}
     */
    List<DataSource> generatorDataSource(List<Integer> tableIdList);

    /**
     * 生成 {@link Entity}
     *
     * @param tableIdList 前端的输入 {@link List<Integer>}
     * @return 成的 {@link Entity}
     */
    List<Entity> generatorEntity(List<Integer> tableIdList);

    /**
     * 生成FieldList
     *
     * @param builtTableInfoList 通过前端输入获得的 {@link BuiltTableInfo}
     * @return 生成的 {@link Field}
     */
    List<Field> generatorField(List<BuiltTableInfo> builtTableInfoList);

    /**
     * 生成Xml文件的主函数
     *
     * @param tableIdList 输入的数据 {@link List<Integer>}
     * @return 是否生成成功的相关信息 {@link SolrResult}
     */
    SolrResult generator(List<Integer> tableIdList);
}
