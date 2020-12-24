package yuanxin.solr.generator.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 传输到前端的字段信息
 *
 * @author huyuanxin
 */
@Data
@ApiModel("字段构建信息")
@NoArgsConstructor
@AllArgsConstructor
public class ColumnInfo {
    /**
     * 字段名
     */
    @ApiModelProperty(value = "字段名")
    private String columnName;

    /**
     * 字段类型
     */
    @ApiModelProperty(value = "字段类型")
    private String columnType;

    /**
     * 字段信息
     */
    @ApiModelProperty(value = "字段信息")
    private String columnInfo;

    /**
     *
     */
    @ApiModelProperty(value = "是否已构建")
    private Boolean built;
}
