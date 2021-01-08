package yuanxin.solr.generator.controller;

import com.baomidou.mybatisplus.plugins.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import yuanxin.solr.generator.entity.TableInfo;
import yuanxin.solr.generator.model.ColumnInfo;
import yuanxin.solr.generator.model.CountTableResult;
import yuanxin.solr.generator.model.GetTableInput;
import yuanxin.solr.generator.service.DatabaseService;

import java.util.List;

/**
 * 获得各种表信息的控制器
 *
 * @author huyuanxin
 */
@RestController
@Api(tags = "solr-auto-generator", description = "Solr自动生成")
@RequestMapping("/solr/")
public class GetInfoController {
    final DatabaseService databaseService;

    @Autowired
    public GetInfoController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping("/getColumnInfoList/{tableId}")
    @ApiOperation(value = "获得表的字段信息")
    public List<ColumnInfo> getAllColumnInfo(
            @PathVariable int tableId
    ) {
        return databaseService.getAllTableColumn(tableId);
    }

    @PostMapping("/getBuiltOrSavedTable")
    @ApiOperation(value = "查询表")
    public Page<TableInfo> getBuiltOrSavedTable(
            @RequestBody GetTableInput getTableInput
    ) {
        return databaseService.getTableWithSavedStatus(getTableInput);
    }

    @GetMapping("/getTableNumber")
    @ApiOperation(value = "计算表数量")
    public CountTableResult getTableNumber() {
        return databaseService.countTable();
    }
}
