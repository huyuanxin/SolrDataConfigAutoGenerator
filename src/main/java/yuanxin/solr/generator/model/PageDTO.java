package yuanxin.solr.generator.model;

import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.session.RowBounds;

import java.io.Serializable;

/**
 * @author huyuanxin
 */
@ApiModel(description = "分页DTO")
@Data
public class PageDTO implements Serializable {

    @ApiModelProperty(value = "当前页")
    private Integer current;
    @ApiModelProperty(value = "页大小")
    private Integer size;

    public static <T>Page<T> buildPage(PageDTO dto) {
        if (dto == null) {
            return new Page<>(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
        }
        Integer dtoCurrent = dto.getCurrent();
        Integer dtoSize = dto.getSize();
        int current = dtoCurrent == null ? 1 : dtoCurrent;
        int size = dtoSize == null || dtoSize == 0 ? 10 : dtoSize;
        return new Page<>(current, size);
    }
}
