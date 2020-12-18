package yuanxin.solr.generator.entity;

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

    @ApiModelProperty("数据库的url")
    public String dataBaseUrl;

    @ApiModelProperty("数据库的用户名")
    public String dataBaseUserName;

    @ApiModelProperty("数据库的密码")
    public String dataBasePassword;

    @ApiModelProperty("数据库的驱动名")
    public String driverName;

    @ApiModelProperty("表名")
    public String tableName;

    @ApiModelProperty("列名集合")
    public List<ColumnNameInfo> columnNameInfoList;
}
