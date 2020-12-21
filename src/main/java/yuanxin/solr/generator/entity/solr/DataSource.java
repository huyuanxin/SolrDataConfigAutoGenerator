package yuanxin.solr.generator.entity.solr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

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