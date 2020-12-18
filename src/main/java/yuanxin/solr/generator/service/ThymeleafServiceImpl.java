package yuanxin.solr.generator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import yuanxin.solr.generator.api.GeneratorService;
import yuanxin.solr.generator.api.ThymeleafService;
import yuanxin.solr.generator.dto.InputDTO;
import yuanxin.solr.generator.entity.DataBase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ThymeleafService的实现类
 *
 * @author huyuanxin
 */
@Service("ThymeleafService")
public class ThymeleafServiceImpl implements ThymeleafService {
    final GeneratorService generatorService;
    final String DATA_CONFIG_DEFAULT_NAME = "data-config";

    @Autowired
    public ThymeleafServiceImpl(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    /**
     * 一次性生成需要的xml
     *
     * @param inputDTO 输入的参数 {@link InputDTO}
     * @return 一些生成信息 {@link String}
     */
    @Override
    public String generatorXmlFile(InputDTO inputDTO) {
        if (generator(inputDTO, DATA_CONFIG_DEFAULT_NAME)) {
            return "生成成功";
        }
        return "生成失败";
    }

    /**
     * 一个一个生成需要的xml
     *
     * @param inputDTO 输入的参数 {@link InputDTO}
     * @return 一些生成信息 {@link String}
     */
    @Deprecated
    @Override
    public String generatorXmlFileOneByOne(InputDTO inputDTO) {
        List<DataBase> dataBaseList = inputDTO.getDataBaseList();
        boolean result = true;
        for (DataBase dataBase : dataBaseList
        ) {
            List<DataBase> dataBaseList1 = new ArrayList<>();
            dataBaseList1.add(dataBase);
            InputDTO inputDto = new InputDTO();
            inputDto.setDataBaseList(dataBaseList1);
            if (!generator(inputDto, dataBase.getTableName())) {
                result = false;
            }
        }
        if (result) {
            return "生成成功";
        } else {
            return "部分生成失败";
        }
    }

    /**
     * 生成Xml文件的主函数
     *
     * @param inputDTO 输入的数据 {@link InputDTO}
     * @param fileName 输出的文件名 {@link String}
     * @return 是否生成成功 {@link Boolean}
     */
    private boolean generator(InputDTO inputDTO, String fileName) {
        try {
            Context context = new Context();
            FileWriter writer = newFileWriter(fileName + ".xml");
            TemplateEngine templateEngine = newTemplateEngine();
            context.setVariable("dataSourceList", generatorService.generatorDataSource(inputDTO));
            context.setVariable("entityList", generatorService.generatorEntity(inputDTO));
            templateEngine.process("data-config", context, writer);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
}
