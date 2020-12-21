package yuanxin.solr.generator.entity.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 需要生成列的信息
 *
 * @author huyuanxin
 */
@Data
@ApiModel(value = "ColumnNameInfo", description = "数据库列的信息")
public class ColumnNameInfoForInput {
    /**
     * 列名 {@link String}
     */
    @ApiModelProperty("列名")
    String columnName;

    /**
     * 列的类型 {@link String}
     */
    @ApiModelProperty("列的类型")
    String columnType;

    /**
     * 列名描述 {@link String}
     */
    @ApiModelProperty("列的描述")
    String columnInfo;
}
