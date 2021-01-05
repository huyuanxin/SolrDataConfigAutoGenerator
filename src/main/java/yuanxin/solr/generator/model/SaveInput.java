package yuanxin.solr.generator.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 保存所需的输入
 *
 * @author huyuanxin
 */
@Data
@ApiModel(value = "SaveInput", description = "前端保存的输入")
public class SaveInput {
    /**
     * 表对应的id
     */
    @ApiModelProperty(value = "表对应的id")
    private Integer tableId;

    /**
     * 字段信息
     */
    @ApiModelProperty(value = "表的字段信息")
    private List<String> columnInfoList;
}
