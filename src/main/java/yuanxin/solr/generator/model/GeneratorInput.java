package yuanxin.solr.generator.model;

import lombok.Data;

import java.util.List;

/**
 * 生成data-config所需的输入
 *
 * @author huyuanxin
 */
@Data
public class GeneratorInput {
    /**
     * 表对应的id
     */
    private List<Integer> tableIdList;
}
