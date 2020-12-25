package yuanxin.solr.generator.controller;

import org.springframework.web.bind.annotation.*;
import yuanxin.solr.generator.entity.TableInfo;
import yuanxin.solr.generator.model.ColumnInfo;
import yuanxin.solr.generator.service.DatabaseService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 获得各种表信息的控制器
 *
 * @author huyuanxin
 */
@RestController
@ApiModel("获得信息")
public class GetInfoController {
    final DatabaseService databaseService;

    @Autowired
    public GetInfoController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping("/getColumnInfo")
    @ApiOperation("获得表的字段信息")
    public List<ColumnInfo> getColumnInfo(
            @ApiParam(value = "tableId", type = "int")
            @RequestParam("tableId") int tableId
    ) {
        return databaseService.getTableColumn(tableId);
    }

    @PostMapping("/getBuiltOrSavedTable")
    @ApiOperation("获得已构建和已保存的表")
    public List<TableInfo> getBuiltOrSavedTable() {
        return databaseService.getBuiltOrSavedTable();
    }
}
