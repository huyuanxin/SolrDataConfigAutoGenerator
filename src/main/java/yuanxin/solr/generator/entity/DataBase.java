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
    String dataBaseName;

    @ApiModelProperty("数据库的url")
    String dataBaseUrl;

    @ApiModelProperty("数据库的用户名")
    String dataBaseUserName;

    @ApiModelProperty("数据库的密码")
    String dataBasePassword;

    @ApiModelProperty("数据库的驱动名")
    String driverName;

    @ApiModelProperty("表名")
    String tableName;

    @ApiModelProperty("列名集合")
    List<String> columnNameList;
}
