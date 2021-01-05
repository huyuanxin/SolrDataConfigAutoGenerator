package yuanxin.solr.generator.model.solr;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 对应data-config内的<field></field>
 *
 * @author huyuanxin
 */
@Data
@AllArgsConstructor
public class Field {
    /**
     * field的columnName属性 {@link String}
     */
    private String columnName;

    /**
     * field的fieldName属性 {@link String}
     */
    private String fieldName;

}
