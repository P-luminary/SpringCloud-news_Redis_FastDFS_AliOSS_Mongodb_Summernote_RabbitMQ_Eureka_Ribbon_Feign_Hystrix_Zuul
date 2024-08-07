package com.imooc.article.controller;

import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.Article;
import com.imooc.pojo.Spouse;
import com.imooc.pojo.Stu;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

@Controller
@RequestMapping("free")
public class FreemarkerController{

    @Value("${freemarker.html.target}")
    private String htmlTarget;

    @GetMapping("/createHTML")
    @ResponseBody
    public String createHTML(Model model) throws IOException, TemplateException {
        // 0. 配置freemarker基本环境
        Configuration cfg = new Configuration(Configuration.getVersion());
        // 声明freemarker模板所需要加载的目录的位置
            //resources/templates/stu.ftl
        String classpath = this.getClass().getResource("/").getPath();
        cfg.setDirectoryForTemplateLoading(new File((classpath + "templates")));

            // 测试打印
        System.out.println(htmlTarget);
        System.out.println(classpath + "templates");
        /**
         * D:\apache-tomcat-8.5.93\webapps\imooc-news\portal\a
         * /C:/Users/Pluminary/Desktop/backup/imooc-news-dev/imooc-news-dev-service-article/target/classes/templates
         */
        // 1. 获得现有的模板ftl文件
        Template template = cfg.getTemplate("stu.ftl", "utf-8");

        // 2. 获得动态数据
            // 定义输出到模板的内容
            // 输入字符串
        String stranger = "慕课网 imooc.com";
        model.addAttribute("there", stranger);
        model = makeModel(model);

        // 3. 融合动态数据和ftl，生成html
        File tempDic = new File(htmlTarget);
        if (!tempDic.exists()) {
            tempDic.mkdirs();
        }
        Writer out = new FileWriter(htmlTarget + File.separator + "10010" + ".html");
        template.process(model, out);
        out.close();
        return "ok";
        // C:\workspace\freemarker_html\10010.html 里面的数据都是静态数据
    }

    @GetMapping("/hello")
    public String hello(Model model){
        makeModel(model);
        // 返回的stu是freemarker模板所在的目录 classpath:/templates/
        // 匹配 *.ftl
        return "stu";
    }

    private Model makeModel(Model model) {
        Stu stu = new Stu();
        stu.setUid("10010");
        stu.setUsername("imooc");
        stu.setAmount(88.86f);
        stu.setAge(18);
        stu.setHaveChild(true);
        stu.setBirthday(new Date());

        Spouse spouse = new Spouse();
        spouse.setUsername("Lucy");
        spouse.setAge(25);

        stu.setSpouse(spouse);
        stu.setArticleList(getArticles());
        stu.setParents(getParents());

        model.addAttribute("stu",stu);
        return model;
    }

    private List<Article> getArticles(){
        Article article1 = new Article();
        article1.setId("1001");
        article1.setTitle("今天天气不错");

        Article article2 = new Article();
        article2.setId("1002");
        article2.setTitle("今天下雨了");

        Article article3 = new Article();
        article3.setId("1003");
        article3.setTitle("昨天下雨了");

        List<Article> list = new ArrayList<>();
        list.add(article1);
        list.add(article2);
        list.add(article3);
        return list;
    }

    private Map<String, String> getParents(){
        Map<String, String> parents = new HashMap<>();
        parents.put("father", "XiaoMing");
        parents.put("mother", "LiLi");
        return parents;
    }
}