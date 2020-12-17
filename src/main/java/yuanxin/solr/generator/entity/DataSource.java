package yuanxin.solr.generator.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 对应data-config内的<dataSource></dataSource>
 *
 * @author huyuanxin
 */
@Data
@AllArgsConstructor
public class DataSource {
    /**
     * dataSource的name属性 {@link String}
     */
    public String dataConfigName;

    /**
     * dataSource的driver属性 {@link String}
     */
    public String dataConfigDriver;

    /**
     * dataSource的url属性 {@link String}
     */
    public String dataConfigUrl;

    /**
     * dataSource的user属性 {@link String}
     */
    public String dataConfigUser;

    /**
     * dataSource的password属性 {@link String}
     */
    public String dataConfigPassword;

}