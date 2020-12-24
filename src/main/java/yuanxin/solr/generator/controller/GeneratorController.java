package yuanxin.solr.generator.controller;

import yuanxin.solr.generator.model.GeneratorInput;
import yuanxin.solr.generator.service.DatabaseService;
import yuanxin.solr.generator.service.GeneratorService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 生成data-config的控制器
 *
 * @author huyuanxin
 */
@ApiModel("生成xml")
@RestController
public class GeneratorController {
    final DatabaseService databaseService;
    final GeneratorService generatorService;

    @Autowired
    public GeneratorController(DatabaseService databaseService, GeneratorService generatorService) {
        this.databaseService = databaseService;
        this.generatorService = generatorService;
    }

    @ApiOperation(value = "生成data-config")
    @PostMapping("/generator")
    public String generatorXml(
            @RequestBody GeneratorInput generatorInput
    ) {
        boolean response = generatorService.generator(generatorInput);
        if (response) {
            databaseService.initTableBuild();
            databaseService.updateTableInfoBuiltStatus(generatorInput, true);
            return "生成成功";
        } else {
            return "生成失败";
        }
    }
}
