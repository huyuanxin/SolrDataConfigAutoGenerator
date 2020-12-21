package yuanxin.solr.generator.api;

import org.springframework.stereotype.Service;
import yuanxin.solr.generator.dto.InputDTO;
import yuanxin.solr.generator.entity.solr.DataSource;
import yuanxin.solr.generator.entity.solr.Entity;

import java.util.List;

/**
 * GeneratorService的接口
 *
 * @author huyuanxin
 */
@Service("GeneratorService")
public interface GeneratorService {
    /**
     * 生成 {@link DataSource}
     *
     * @param inputDTO 输入的需要生成的信息 {@link InputDTO}
     * @return 生成的 {@link DataSource}
     */
    List<DataSource> generatorDataSource(InputDTO inputDTO);

    /**
     * 生成 {@link Entity}
     *
     * @param inputDTO 输入的需要生成的信息 {@link InputDTO}
     * @return 生成的 {@link Entity}
     */
    List<Entity> generatorEntity(InputDTO inputDTO);
}
