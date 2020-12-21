package yuanxin.solr.generator.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yuanxin.solr.generator.api.ThymeleafService;
import yuanxin.solr.generator.dto.InputDTO;

/**
 * 生成XML文件的控制器
 *
 * @author huyuanxin
 */
@Api(tags = "生成xml文件")
@RestController
public class GeneratorController {
    final ThymeleafService thymeleafService;

    @Autowired
    public GeneratorController(ThymeleafService thymeleafService) {
        this.thymeleafService = thymeleafService;
    }

    @ApiOperation(value = "生成data-config.xml", response = String.class)
    @PostMapping(value = "/generator")
    public String generatorXml(
            @RequestBody InputDTO inputDTO) {
        return thymeleafService.generatorXmlFile(inputDTO);
    }
}
