package yuanxin.solr.generator.service;

import yuanxin.solr.generator.entity.BuiltTableInfo;
import yuanxin.solr.generator.model.solr.DataSource;
import yuanxin.solr.generator.model.solr.Entity;
import yuanxin.solr.generator.model.solr.Field;
import yuanxin.solr.generator.model.GeneratorInput;
import org.springframework.stereotype.Service;

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
     * @param generatorInput 前端的输入
     * @return 生成的 {@link DataSource}
     */
    List<DataSource> generatorDataSource(GeneratorInput generatorInput);

    /**
     * 生成 {@link Entity}
     *
     * @param generatorInput 前端的输入
     * @return 成的 {@link Entity}
     */
    List<Entity> generatorEntity(GeneratorInput generatorInput);

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
     * @param generatorInput 输入的数据 {@link GeneratorInput}
     * @return 是否生成成功 {@link Boolean}
     */
    boolean generator(GeneratorInput generatorInput);
}
