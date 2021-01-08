package yuanxin.solr.generator.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yuanxin.solr.generator.model.SolrResult;
import yuanxin.solr.generator.service.DatabaseService;
import yuanxin.solr.generator.service.GeneratorService;

import java.util.List;

/**
 * 生成data-config的控制器
 *
 * @author huyuanxin
 */
@Api(tags = "solr-auto-generator", description = "Solr自动生成")
@RestController
@RequestMapping("/solr/")
public class GeneratorController {
    final DatabaseService databaseService;
    final GeneratorService generatorService;

    @Autowired
    public GeneratorController(DatabaseService databaseService, GeneratorService generatorService) {
        this.databaseService = databaseService;
        this.generatorService = generatorService;
    }

    @ApiOperation(value = "生成data-config", response = String.class)
    @PostMapping("/generator")
    public SolrResult generatorXml(
            @RequestBody List<Integer> tableIdList
    ) {
        SolrResult response = generatorService.generator(tableIdList);
        if (response.getResult()) {
            if (!databaseService.initTableBuild()) {
                return new SolrResult(false, "重置生成表构建状态失败，请联系管理员");
            }
            if (!databaseService.updateTableInfoBuiltStatus(tableIdList, true)) {
                return new SolrResult(false, "设置生成状态失败，请联系管理员");
            }
        }
        return response;
    }
}
