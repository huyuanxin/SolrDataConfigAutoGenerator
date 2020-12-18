package yuanxin.solr.generator.api;

import org.springframework.stereotype.Service;
import yuanxin.solr.generator.dto.InputDTO;

/**
 * ThymeleafService的接口
 *
 * @author huyuanxin
 */
@Service("ThymeleafService")
public interface ThymeleafService {

    /**
     * 生成需要的xml
     *
     * @param inputDTO 输入的参数 {@link InputDTO}
     * @return 一些生成信息 {@link String}
     */
    String generatorXmlFile(InputDTO inputDTO);

    /**
     * 一个一个生成需要的xml
     *
     * @param inputDTO 输入的参数 {@link InputDTO}
     * @return 一些生成信息 {@link String}
     */
    @Deprecated
    String generatorXmlFileOneByOne(InputDTO inputDTO);
}
