package yuanxin.solr.generator.model.solr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对应data-config内的<dataSource></dataSource>
 *
 * @author huyuanxin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSource {
    /**
     * dataSource的name属性 {@link String}
     */
    private String dataConfigName;

    /**
     * dataSource的driver属性 {@link String}
     */
    private String dataConfigDriver;

    /**
     * dataSource的url属性 {@link String}
     */
    private String dataConfigUrl;

    /**
     * dataSource的user属性 {@link String}
     */
    private String dataConfigUser;

    /**
     * dataSource的password属性 {@link String}
     */
    private String dataConfigPassword;

}