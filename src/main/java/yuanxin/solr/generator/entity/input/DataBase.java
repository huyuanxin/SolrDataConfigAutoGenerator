package yuanxin.solr.generator.entity.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 需要生成的数据库信息
 *
 * @author huyuanxin
 */
@Data
@ApiModel(value = "DataBase", description = "生成的内容")
@AllArgsConstructor
@NoArgsConstructor
public class DataBase {
    @ApiModelProperty("数据库的名字")
    public String dataBaseName;

    @ApiModelProperty("表名")
    public String tableName;

    @ApiModelProperty("列名集合")
    public List<ColumnNameInfoForInput> columnNameInfoForInputList;
}
