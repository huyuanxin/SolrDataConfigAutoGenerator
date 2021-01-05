package yuanxin.solr.generator.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import yuanxin.solr.generator.model.SaveInput;
import yuanxin.solr.generator.model.SolrResult;
import yuanxin.solr.generator.service.DatabaseService;

import java.util.List;

/**
 * 保存控制器
 *
 * @author huyuanxin
 */
@Api(tags = "solr-auto-generator", description = "Solr自动生成")
@RestController
@RequestMapping("/xboot/solr/")
public class CurdController {

    final DatabaseService databaseService;

    public CurdController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @ApiOperation(value = "保存需要生成的字段", response = boolean.class)
    @PostMapping("/save")
    public SolrResult save(
            @RequestBody SaveInput saveInput
    ) {
        return databaseService.saveGeneratorColumn(saveInput);
    }

    @ApiOperation(value = "删除不需要的表", response = boolean.class)
    @DeleteMapping("/delete")
    public SolrResult deleteBuiltOrSavedTable(
            @RequestBody List<Integer> tableIdList
    ) {
        return databaseService.deleteBuiltOrSavedTable(tableIdList);
    }
}
