package yuanxin.solr.generator.po;

import lombok.Data;

/**
 * @author huyuanxin
 */
@Data
public class TableInfo {
    int id;
    String dataBaseName;
    String tableName;
    boolean build;
}
