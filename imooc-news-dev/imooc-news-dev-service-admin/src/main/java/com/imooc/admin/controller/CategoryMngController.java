package com.imooc.admin.controller;

import com.imooc.admin.service.CategoryService;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.CategoryMngControllerApi;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Category;

import com.imooc.pojo.bo.SaveCatrgoryBO;
import com.imooc.utils.JsonUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CategoryMngController extends BaseController implements CategoryMngControllerApi {

    final static Logger logger = LoggerFactory.getLogger(CategoryMngController.class);

    @Autowired
    private CategoryService categoryService;

    @Override
    public GraceJSONResult saveOrUpdateCategory(SaveCatrgoryBO saveCatrgoryBO, BindingResult result) {
        if (result.hasErrors()){
        // 判断BindingResult是否保存错误的验证信息，如果有，则直接return
            Map<String, String> errorMap = getErrors(result);
            return GraceJSONResult.errorMap(errorMap);
        }
        Category newCat = new Category();
        BeanUtils.copyProperties(saveCatrgoryBO,newCat);
        // id为空新增，不为空修改
        if (saveCatrgoryBO.getId() == null){
            //查询新增的分类名称不能重复存在
            boolean isExist = categoryService.queryCatIsExist(newCat.getName(), null);
            if (!isExist){
                //新增到数据库
                categoryService.createCategory(newCat);
            }else {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
            }
        }else {
            //查询修改的分类名称不能重复存在
            boolean isExist = categoryService.queryCatIsExist(newCat.getName(), saveCatrgoryBO.getOldName());
            if (!isExist){
                //修改到数据库
                categoryService.modifyCategory(newCat);
            } else {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.CATEGORY_EXIST_ERROR);
            }
        }
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getCatList() {
        List<Category> categoryList = categoryService.queryCategoryList();
        return GraceJSONResult.ok(categoryList);
    }

    @Override
    public GraceJSONResult getCats() {
        // 先从redis中查询，如果有，则返回，如果没有，则查询数据库库后先放缓存，放返回
        String allCatJson = redis.get(REDIS_ALL_CATEGORY);

        List<Category> categoryList = null;
        if (StringUtils.isBlank(allCatJson)) {
            categoryList = categoryService.queryCategoryList();
            redis.set(REDIS_ALL_CATEGORY, JsonUtils.objectToJson(categoryList));
        } else {
            categoryList = JsonUtils.jsonToList(allCatJson, Category.class); //字符串转换成list
        }

        return GraceJSONResult.ok(categoryList);
    }
}

