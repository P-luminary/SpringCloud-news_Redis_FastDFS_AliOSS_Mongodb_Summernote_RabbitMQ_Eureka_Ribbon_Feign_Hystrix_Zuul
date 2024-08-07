package com.imooc.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {

    /**
     * 验证是否是URL
     * @param url
     * @return
     */
    public static boolean verifyUrl(String url){

        // URL验证规则
//        String regEx ="[A-Za-z]+://[A-Za-z0-9-_]+\\\\.[A-Za-z0-9-_%&\\?\\/.=]+";
        String regEx = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        // 字符串是否与正则表达式相匹配
        boolean rs = matcher.matches();
        return rs;

    }
    //不能有空格 不能为空 字符串长度要在6-12位
    public static boolean verifyName(String name){
        // Name验证规则
        String nameEx = "^[^\\s]{6,12}$";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(nameEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        // 字符串是否与正则表达式相匹配
        boolean rs = matcher.matches();
        return rs;
    }

    public static void main(String[] args) {
        boolean res = verifyUrl("http://admin.imoocnews.com:9090/imooc-news/admin/friendLinks.html");
        boolean nres = verifyName("Jerry");
        System.out.println(nres);
    }

}
