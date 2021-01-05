package yuanxin.solr.generator.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author huyuanxin
 */
@Data
@AllArgsConstructor
@ApiModel("Solr操作返回")
public class SolrResult {
    @ApiModelProperty("是否成功")
    Boolean result;
    @ApiModelProperty("返回信息")
    String message;
}
