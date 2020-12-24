package yuanxin.solr.generator.model.solr;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 对应data-config内的<entity></entity>
 *
 * @author huyuanxin
 */
@Data
@AllArgsConstructor
public class Entity {
    /**
     * entity的name属性 {@link String}
     */
    private String entityName;

    /**
     * entity的dataSource属性 {@link String}
     */
    private String entityDataSource;

    /**
     * entity的query属性 {@link String}
     */
    private String querySqlCommand;

    /**
     * entity的deltaImportQuery属性 {@link String}
     */
    private String deltaImportQueryCommand;

    /**
     * entity的deltaQuery属性 {@link String}
     */
    private String deltaQueryCommand;

    /**
     * entity内部field的属性 {@link List<Field>}
     */
    private List<Field> fieldList;

}
