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
@Data
@ApiModel(description = "分页DTO")
public class PageDTO implements Serializable {

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private Integer current;

    /**
     * 页大小
     */
    @ApiModelProperty(value = "页大小")
    private Integer size;

    /**
     * 构造 {@link Page}
     *
     * @param dto dto
     * @param <T> {@link Page}的类型
     * @return 构造的 {@link Page}
     */
    public static <T> Page<T> buildPage(PageDTO dto) {
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
