package yuanxin.solr.generator.entity.solr;


import lombok.Data;

/**
 * 对应data-config内的<field></field>
 *
 * @author huyuanxin
 */
@Data
public class Field {
    /**
     * field的columnName属性 {@link String}
     */
    public String columnName;

    /**
     * field的fieldName属性 {@link String}
     */
    public String fieldName;

    public Field(String columnName, String fieldName) {
        this.columnName = columnName;
        this.fieldName = fieldName;
    }
}
