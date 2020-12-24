package yuanxin.solr.generator.controller;

import yuanxin.solr.generator.model.SaveInput;
import yuanxin.solr.generator.service.DatabaseService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 保存控制器
 *
 * @author huyuanxin
 */
@ApiModel("保存")
@RestController
public class SaveController {

    final DatabaseService databaseService;

    public SaveController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @ApiOperation("保存")
    @PostMapping("/save")
    public boolean save(
            @RequestBody SaveInput saveInput
    ) {
        return databaseService.saveGeneratorColumn(saveInput);
    }
}
