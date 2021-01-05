package yuanxin.solr.generator.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获得表信息的输入
 *
 * @author huyuanxin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "GetTableInput", description = "获得表信息的输入")
public class GetTableInput extends PageDTO {
    /**
     * 是否保存
     */
    @ApiModelProperty("是否保存,true为已保存的,false为未保存的")
    private boolean saved;

    /**
     * 查询关键词
     */
    @ApiModelProperty("搜索关键词,不发送则为查询全部")
    String key;
}
