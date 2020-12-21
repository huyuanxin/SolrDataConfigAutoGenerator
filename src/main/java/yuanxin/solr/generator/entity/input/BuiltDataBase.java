package yuanxin.solr.generator.entity.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huyuanxin
 */

@Data
@ApiModel(value = "BuiltDataBase", description = "已经生成或者未修改的数据库")
@AllArgsConstructor
@NoArgsConstructor
public class BuiltDataBase {
    @ApiModelProperty("数据库的名字")
    public String dataBaseName;

    @ApiModelProperty("表名")
    public String tableName;
}
