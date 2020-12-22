package yuanxin.solr.generator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import yuanxin.solr.generator.api.GeneratorService;
import yuanxin.solr.generator.api.ThymeleafService;
import yuanxin.solr.generator.dto.InputDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * ThymeleafService的实现类
 *
 * @author huyuanxin
 */
@Service("ThymeleafService")
public class ThymeleafServiceImpl implements ThymeleafService {
    final GeneratorService generatorService;
    final String TemplateName = "data-config";
    final String DATA_CONFIG_DEFAULT_NAME = "data-config.xml";
    final String dataSourceInThymeleaf = "dataSourceList";
    final String entityListInThymeleaf = "entityList";

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
        if (generator(inputDTO)) {
            return "生成成功";
        }
        return "生成失败";
    }

    /**
     * 生成Xml文件的主函数
     *
     * @param inputDTO 输入的数据 {@link InputDTO}
     * @return 是否生成成功 {@link Boolean}
     */
    private boolean generator(InputDTO inputDTO) {
        try {
            Context context = new Context();
            String fileLocation=inputDTO.getFileLocation();
            FileWriter writer = newFileWriter(fileLocation+DATA_CONFIG_DEFAULT_NAME);
            TemplateEngine templateEngine = newTemplateEngine();
            context.setVariable(dataSourceInThymeleaf, generatorService.generatorDataSource(inputDTO));
            context.setVariable(entityListInThymeleaf, generatorService.generatorEntity(inputDTO));
            templateEngine.process(TemplateName, context, writer);
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
