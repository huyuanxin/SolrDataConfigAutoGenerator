package yuanxin.solr.generator.controller;

import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;
import yuanxin.solr.generator.entity.TableInfo;
import yuanxin.solr.generator.model.SaveInput;
import yuanxin.solr.generator.service.DatabaseService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * 保存控制器
 *
 * @author huyuanxin
 */
@ApiModel("保存")
@RestController
public class CurdController {

    final DatabaseService databaseService;

    public CurdController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @ApiOperation("保存")
    @PostMapping("/save")
    public boolean save(
            @RequestBody SaveInput saveInput
    ) {
        return databaseService.saveGeneratorColumn(saveInput);
    }

    @ApiOperation("删除")
    @DeleteMapping("/delete")
    public boolean deleteBuiltOrSavedTable(
            @RequestBody List<Integer> tableIdList
    ) {
        return databaseService.deleteBuiltOrSavedTable(tableIdList);
    }

    @ApiOperation("查询")
    @PostMapping("/search")
    public List<TableInfo> search(
            @ApiParam("关键词")
            @RequestParam("key") String key
    ) {
        return databaseService.searchTableInfo(key);
    }
}
