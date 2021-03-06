package yuanxin.solr.generator.util;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import yuanxin.solr.generator.entity.BuiltTableInfo;
import yuanxin.solr.generator.entity.TableInfo;
import yuanxin.solr.generator.model.solr.DataSource;
import yuanxin.solr.generator.service.BuiltTableInfoService;
import yuanxin.solr.generator.service.TableInfoService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 辅助生成的工具
 *
 * @author huyuanxin
 */
@Component
public class GeneratorUtil {
    @Value("${spring.datasource.driver-class-name}")
    private String dataConfigDriver;

    @Value("${data-config.generator.sqlUrl}")
    private String dataConfigUrl;

    @Value("${spring.datasource.username}")
    private String dataConfigUser;

    @Value("${spring.datasource.password}")
    private String dataConfigPassword;

    final BuiltTableInfoService builtTableInfoService;
    final TableInfoService tableInfoService;

    final String MYSQL = "mysql";
    final String SQLSERVER = "sqlserver";
    final String ORACLE = "oracle";

    @Autowired
    public GeneratorUtil(BuiltTableInfoService builtTableInfoService, TableInfoService tableInfoService) {
        this.builtTableInfoService = builtTableInfoService;
        this.tableInfoService = tableInfoService;
    }

    /**
     * 通过 {@link TableInfo} 获得 {@link BuiltTableInfo}
     *
     * @param tableInfo 通过前端输入获得的单个 {@link TableInfo}
     * @return {@link BuiltTableInfo}
     */
    public List<BuiltTableInfo> getBuiltTableInfo(TableInfo tableInfo) {
        EntityWrapper<BuiltTableInfo> queryWrapper = new EntityWrapper<>();
        queryWrapper.eq("database_name", tableInfo.getDatabaseName());
        queryWrapper.eq("table_name", tableInfo.getTableName());
        return builtTableInfoService.selectList(queryWrapper);
    }

    /**
     * 构造QuerySqlCommand
     *
     * @param builtTableInfo 通过前端输入获得的 {@link BuiltTableInfo}
     * @return 构造的QuerySqlCommand
     */
    public String generatorQuerySqlCommand(List<BuiltTableInfo> builtTableInfo) {
        String tableName = builtTableInfo.get(0).getTableName();
        builtTableInfo.removeIf(it -> "id".equals(it.getColumnName()));
        StringBuilder sql = new StringBuilder();
        // 构造Query
        if (dataConfigUrl.contains(MYSQL) || dataConfigUrl.contains(ORACLE)) {
            sql.append("Select CONCAT(").append("'").append(tableName).append("_").append("'").append(",id) as id,");
        } else if (dataConfigUrl.contains(SQLSERVER)) {
            // SQLServer 2012之前需要
            // Select CONCAT('sd'+id) as id,hosp_district_code From pa_patient_info
            sql.append("Select ").append("'").append(tableName).append("_").append("'+").append("id as id,");
        }
        int size = builtTableInfo.size() - 1;
        for (int i = 0; i < size; i++) {
            sql.append(builtTableInfo.get(i).getColumnName()).append(",");
        }
        sql.append(builtTableInfo.get(size).getColumnName()).append(" From ").append(tableName);
        return sql.toString();
    }

    /**
     * 构造DeltaImport
     *
     * @param builtTableInfo 通过前端输入获得的 {@link BuiltTableInfo}
     * @return 生成的sql语句 {@link String}
     */
    public String generatorDeltaImportCommand(List<BuiltTableInfo> builtTableInfo) {
        String sql = generatorQuerySqlCommand(builtTableInfo);
        return sql + " where id='${dataimporter.delta.id}'";
    }

    /**
     * 构造DeltaQuery
     *
     * @param builtTableInfo 通过前端输入获得的 {@link BuiltTableInfo}
     * @return 生成的sql语句 {@link String}
     */
    public String generatorDeltaQuerySqlCommand(List<BuiltTableInfo> builtTableInfo) {
        String tableName = builtTableInfo.get(0).getTableName();
        return "Select id Form " + tableName + " where create_time > '${dataimporter.last_index_time}'";
    }

    /**
     * @param tableIdList 前端的输入
     * @return {@link TableInfo}
     */
    public List<TableInfo> getTableInfo(List<Integer> tableIdList) {
        EntityWrapper<TableInfo> queryWrapper = new EntityWrapper<>();
        queryWrapper.in("id", tableIdList);
        List<TableInfo> result = tableInfoService.selectList(queryWrapper);
        if (result != null && result.size() != 0) {
            return result;
        }
        return new ArrayList<>();
    }

    /**
     * {@link TableInfo} 转换为 {@link DataSource}
     *
     * @param tableInfo 需要转换的 {@link TableInfo}
     * @return 转换出的 {@link DataSource}
     */
    public DataSource tableInfoToDataSource(TableInfo tableInfo) {
        return new DataSource(
                tableInfo.getDatabaseName(),
                dataConfigDriver,
                dataConfigUrl + tableInfo.getDatabaseName(),
                dataConfigUser,
                dataConfigPassword
        );
    }


    /**
     * 生成{@link TemplateEngine}引擎
     *
     * @return 生成的 {@link TemplateEngine} 引擎
     */
    public TemplateEngine newTemplateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".xml");
        //创建模板引擎
        TemplateEngine templateEngine = new TemplateEngine();
        //将加载器放入模板引擎
        templateEngine.setTemplateResolver(resolver);
        return templateEngine;
    }

    /**
     * 生成文件写入工具
     *
     * @param fileName 写入的文件名
     * @return 生成的 {@link FileWriter}
     * @throws FileNotFoundException 抛出的 {@link FileNotFoundException} 异常
     */
    public FileWriter newFileWriter(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("文件生成成功");
                } else {
                    throw new FileNotFoundException("文件创建失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter writer = new FileWriter(file);
            writer.flush();
            return writer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new FileNotFoundException("文件创建失败");
    }

    /**
     * {@link List} 去重
     *
     * @param <T>  List的类型
     * @param list 需要去重的 {@link List}
     * @return 去重后的 {@link List}
     */
    public <T> List<T> removeDuplicate(List<T> list) {
        List<T> tempList = new ArrayList<>();
        for (T obj : list) {
            if (!tempList.contains(obj)) {
                tempList.add(obj);
            }
        }
        return tempList;
    }
}
