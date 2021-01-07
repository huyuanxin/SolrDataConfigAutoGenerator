package yuanxin.solr.generator.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huyuanxin
 */
@Data
@ApiModel("搜索的输入")
@EqualsAndHashCode(callSuper = true)
public class SearchInput extends PageDTO {
    /**
     * 查询关键词
     */
    @ApiModelProperty("搜索关键词")
    String key;
}
