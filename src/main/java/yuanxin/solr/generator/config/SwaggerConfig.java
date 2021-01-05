package yuanxin.solr.generator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


/**
 * @author huyuanxin
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30).apiInfo(
                new ApiInfoBuilder()
                        .contact(
                                new Contact(
                                        "huyuanxin",
                                        "https://github.com/huyuanxin",
                                        "huyuanxin1999@outlook.com"
                                )
                        )
                        .title("Solr自动生成配置文件")
                        .build()
        );
    }
}
