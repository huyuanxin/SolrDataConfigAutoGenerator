package yuanxin.solr.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import yuanxin.solr.generator.entity.input.BuiltDataBase;
import yuanxin.solr.generator.entity.input.DataBase;

import java.util.List;

/**
 * 前端传输信息的DTO
 *
 * @author huyuanxin
 */
@Data
@NoArgsConstructor
@ApiModel(value = "InputDTO", description = "传入的数据")
public class InputDTO {
    /**
     * 生成文件的位置
     */
    @ApiModelProperty("生成文件的位置")
    String fileLocation;
    /**
     * 已经构建了的数据库的名字
     */
    @ApiModelProperty("已经构建或无需改动的数据库")
    List<BuiltDataBase> builtDataBaseInfo;

    /**
     * 需要构建的数据库
     */
    @ApiModelProperty("需要构建或已经改动了的数据库")
    List<DataBase> dataBaseList;
}
