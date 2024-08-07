package com.imooc.api;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseController {
    @Autowired
    public RedisOperator redis;

    @Autowired
    public RestTemplate restTemplate;
    public static final String MOBILE_SMSCODE = "mobile:smscode";
    public static final String REDIS_USER_TOKEN = "redis_user_token";//ctrl+shift+u直接大写
    public static final String REDIS_USER_INFO = "redis_user_info";//ctrl+shift+u直接大写
    public static final String REDIS_ADMIN_TOKEN = "redis_admin_token";//ctrl+shift+u直接大写
    public static final String REDIS_ALL_CATEGORY = "redis_all_category";

    public static final String REDIS_WRITER_FANS_COUNTS = "redis_writer_fans_counts";
    public static final String REDIS_MY_FOLLOW_COUNTS = "redis_my_follow_counts";

    public static final String REDIS_ARTICLE_READ_COUNTS = "redis_article_read_counts";
    public static final String REDIS_ALREADY_READ = "redis_already_read";

    public static final String REDIS_ARTICLE_COMMENT_COUNTS = "redis_article_comment_counts";

    @Value("${website.domain-name}")
    public String DOMAIN_NAME;
    public static final Integer COOKIE_MONTH = 30 * 24 * 60 * 60;
    public static final Integer COOKIE_DELETE = 0;

    public static final Integer COMMON_START_PAGE = 1;
    public static final Integer COMMON_PAGE_SIZE = 10;
    /**
     * 可以公用 就放到BaseController里面
     * 在任何controller中都可以调用和使用
     * 获取BO中的错误信息
     *
     * @param result
     * @return
     */
    public Map<String, String> getErrors(BindingResult result) {
        //对应着RegistLoginBO的信息
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            //发生验证错误所对应的某个属性
            String field = error.getField();
            //验证的错误信息
            String msg = error.getDefaultMessage();
            map.put(field, msg);
        }
        return map;
    }

    public void setCookie(HttpServletRequest request,
                          HttpServletResponse response,
                          String cookieName,
                          String cookieValue,
                          Integer maxAge) {
        try {
            cookieValue = URLEncoder.encode(cookieValue, "utf-8");
//            Cookie cookie = new Cookie(cookieName,cookieValue);
//            cookie.setMaxAge(maxAge);
//            cookie.setDomain("imoocnews.com");
//            cookie.setPath("/");//都用cookie
            setCookieValue(request, response, cookieName, cookieValue, maxAge);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCookieValue(HttpServletRequest request,
                               HttpServletResponse response,
                               String cookieName,
                               String cookieValue,
                               Integer maxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(maxAge);
//        cookie.setDomain("imoocnews.com");
        cookie.setDomain(DOMAIN_NAME);
        cookie.setPath("/");//都用cookie
        response.addCookie(cookie);//把cookie传入
    }

    public void deleteCookie(HttpServletRequest request,HttpServletResponse response,String cookieName){
        try {
            String deleteValue = URLEncoder.encode("", "utf-8");
            setCookieValue(request, response, cookieName, deleteValue, COOKIE_DELETE);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    public Integer getCountsFromRedis(String key){
        String countsStr = redis.get(key);
        if (StringUtils.isBlank(countsStr)) {
            countsStr = "0";
        }
        return Integer.valueOf(countsStr);
    }

    public List<AppUserVO> getBasicUserList(Set idSet) {
        String userServerUrlExecute
                = "http://user.imoocnews.com:8003/user/queryByIds?userIds=" + JsonUtils.objectToJson(idSet);
        ResponseEntity<GraceJSONResult> responseEntity
                = restTemplate.getForEntity(userServerUrlExecute, GraceJSONResult.class);
        GraceJSONResult bodyResult = responseEntity.getBody();
        List<AppUserVO> userVOList = null;
        if (bodyResult.getStatus() == 200) {
            String userJson = JsonUtils.objectToJson(bodyResult.getData());
            userVOList = JsonUtils.jsonToList(userJson, AppUserVO.class);
        }
        return userVOList;
    }
}

