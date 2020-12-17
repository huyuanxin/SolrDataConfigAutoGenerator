package yuanxin.solr.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import yuanxin.solr.generator.entity.DataBase;

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
    List<DataBase> dataBaseList;

    @ApiModelProperty("dataBaseList")
    public List<DataBase> getDataBaseList() {
        return dataBaseList;
    }

    public void setDataBaseList(List<DataBase> dataBaseList) {
        this.dataBaseList = dataBaseList;
    }
}
