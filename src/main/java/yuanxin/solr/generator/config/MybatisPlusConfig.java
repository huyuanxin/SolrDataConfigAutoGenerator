package yuanxin.solr.generator.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author huyuanxin
 */
@Configuration
@MapperScan("yuanxin.solr.*.mapper")
public class MybatisPlusConfig {
}
