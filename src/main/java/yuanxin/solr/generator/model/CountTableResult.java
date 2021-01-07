package yuanxin.solr.generator.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author huyuanxin
 */
@Data
@ApiModel
@AllArgsConstructor
public class CountTableResult {
    /**
     * 已构建的数量
     */
    @ApiModelProperty("已构建的数量")
    int built;

    /**
     * 未构建的数量
     */
    @ApiModelProperty("未构建的数量")
    int noBuilt;
}
