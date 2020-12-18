package yuanxin.solr.generator.entity;

import lombok.Data;

/**
 * @author huyuanxin
 */
@Data
public class ColumnNameInfo {
    /**
     * 列名 {@link String}
     */
    String columnName;

    /**
     * 列的类型 {@link String}
     */
    String columnType;

    /**
     * 列名描述 {@link String}
     */
    String columnInfo;
}
