package cn.e3mall.item.controller;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成静态页面测试Controller
 */
@Controller
public class HtmlGenController {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @RequestMapping("/genhtml")
    @ResponseBody
    public String genHtml() throws Exception {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //加载模板对象
        Template template = configuration.getTemplate("hello.ftl");
        //创建一个数据集
        Map map = new HashMap();
        map.put("hello",1234565);
        //指定文件输出的路劲及文件名
        FileWriter out = new FileWriter(new File("G:\\test\\hello.html"));
        //输出文件
        template.process(map,out);
        out.close();

        return "ok";
    }
}
