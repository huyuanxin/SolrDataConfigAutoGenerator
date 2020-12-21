package yuanxin.solr.generator.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yuanxin.solr.generator.api.TableInfoService;
import yuanxin.solr.generator.entity.input.ColumnNameInfoForInput;
import yuanxin.solr.generator.entity.output.ColumnNameInfoForOutput;
import yuanxin.solr.generator.po.TableInfo;

import java.util.List;

/**
 * @author huyuanxin
 */
@RestController
@Api(tags = "test")
public class TestController {
    final TableInfoService tableInfoService;

    @Autowired
    public TestController(TableInfoService tableInfoService) {
        this.tableInfoService = tableInfoService;
    }

    @ApiOperation(value = "根据是否构建获得表信息",response = TableInfo.class)
    @GetMapping("/getTable")
    public List<TableInfo> test(
            @ApiParam(value = "是否已经被构建",type = "boolean")
            @RequestParam(value = "build",defaultValue = "true") boolean build) {
        return tableInfoService.getTableInfoWithBuild(build);
    }

    @ApiOperation(value = "获得列的信息",response = ColumnNameInfoForInput.class)
    @GetMapping("/getTableInfo")
    public List<ColumnNameInfoForOutput> test(
            @ApiParam(value = "数据库名",type = "String")
            @RequestParam("dataBaseName") String dataBaseName,

            @ApiParam(value = "表名",type = "String")
            @RequestParam("tableName") String tableName) {
        return tableInfoService.getTableColumnInfo(dataBaseName,tableName);
    }
}
