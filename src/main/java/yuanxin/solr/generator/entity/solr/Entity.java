package yuanxin.solr.generator.entity.solr;

import lombok.Data;

import java.util.List;

/**
 * 对应data-config内的<entity></entity>
 *
 * @author huyuanxin
 */
@Data
public class Entity {
    /**
     * entity的name属性 {@link String}
     */
    public String entityName;

    /**
     * entity的dataSource属性 {@link String}
     */
    public String entityDataSource;

    /**
     * entity的query属性 {@link String}
     */
    public String querySqlCommand;

    /**
     * entity的deltaImportQuery属性 {@link String}
     */
    public String deltaImportQueryCommand;

    /**
     * entity的deltaQuery属性 {@link String}
     */
    public String deltaQueryCommand;

    /**
     * entity内部field的属性 {@link List<Field>}
     */
    public List<Field> fieldList;

    public Entity(String entityName, String entityDataSource, String querySqlCommand, String deltaImportQueryCommand, String deltaQueryCommand, List<Field> fieldList) {
        this.entityName = entityName;
        this.entityDataSource = entityDataSource;
        this.querySqlCommand = querySqlCommand;
        this.deltaImportQueryCommand = deltaImportQueryCommand;
        this.deltaQueryCommand = deltaQueryCommand;
        this.fieldList = fieldList;
    }

}
