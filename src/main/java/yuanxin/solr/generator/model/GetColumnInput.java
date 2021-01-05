package yuanxin.solr.generator.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获得字段信息的输入
 *
 * @author huyuanxin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "GetColumnInput", description = "获得字段信息的输入")
public class GetColumnInput extends PageDTO {

    /**
     * 表对应的id
     */
    @ApiModelProperty("表对应的id")
    int tableId;
}
