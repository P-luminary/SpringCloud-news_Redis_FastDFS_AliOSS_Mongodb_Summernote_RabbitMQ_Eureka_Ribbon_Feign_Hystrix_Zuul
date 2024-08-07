<html>
<head>
    <title>Hello Freemarker</title>
</head>
<body>
<#-- 写完以后去模板页面配置 application.yml
    Freemarker 页面的语法构成：
    1. 注释
    2. 表达式 ${...}
    3. 普通文本，基本的html标签
    4. 指令
-->
    <div>
        hello ${there}
    </div>
<br>

    <div>
        用户名uid: ${stu.uid}<br>
        用户姓名: ${stu.username}<br>
        年龄：${stu.age}<br>
        生日：${stu.birthday?string('yyyy-MM-dd HH:mm:ss')}<br> <#-- 日期转换 -->
        用户余额：${stu.amount}<br>
        已育：${stu.haveChild?string('yes', 'no')}<br>
        <#if stu.spouse??>
            伴侣：${stu.spouse.username}, ${stu.spouse.age}岁
        </#if>
        <#if !stu.spouse??>
            单身狗
        </#if>
    </div>

<br>

    <div>
        <#list stu.articleList as article>
            <div>
                <span>${article.id}</span>
                <span>${article.title}</span>
            </div>
        </#list>
    </div>

<br>

    <div>
        <#list stu.parents?keys as key>
            <div>
                ${stu.parents[key]}
            </div>
        </#list>
    </div>

<br>

    <div>
        <#if stu.uid == '10010'>
            用户id是10010
            <br>
        </#if>
        <#if stu.username != 'imooc'>
            用户名不是imooc
            <br>
        </#if>
        <#if (stu.age >= 18) >
            用户已成年
        </#if>
        <br>
        <#if (stu.age > 18 || stu.age = 18) >
            成年人
            <br>
        </#if>
        <#if (stu.age < 18) >
            未成年
            <br>
        </#if>
        <#if stu.haveChild >
            已育
        </#if>
        <br>
        <#if !stu.haveChild >
             未育
        </#if>
     </div>
</body>
</html>