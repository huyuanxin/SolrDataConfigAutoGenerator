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
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "TableInfo", description = "对应数据库中的table_info")
public class TableInfo implements Serializable {
    /**
     * id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     *
     */
    @ApiModelProperty("数据库名")
    private String databaseName;

    /**
     *
     */
    @ApiModelProperty("表名")
    private String tableName;

    /**
     *
     */
    @ApiModelProperty("是否已构建")
    private Boolean build;

    /**
     * 是否已保存
     */
    @ApiModelProperty("是否已保存")
    private Boolean saved;

}
