package yuanxin.solr.generator.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 对应数据库中built_table_info的实体
 *
 * @author huyuanxin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "BuiltTableInfo", description = "对应数据库中的built_table_info")
public class BuiltTableInfo implements Serializable {
    /**
     * 数据库名
     */
    @ApiModelProperty(value = "数据库名")
    private String databaseName;

    /**
     * 表名
     */
    @ApiModelProperty(value = "表名")
    private String tableName;

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
}
