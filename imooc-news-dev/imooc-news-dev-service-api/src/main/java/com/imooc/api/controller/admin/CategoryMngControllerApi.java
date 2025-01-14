package com.imooc.api.controller.admin;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.SaveCatrgoryBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "文章分类维护", tags = {"文章分类维护controller"})
@RequestMapping("categoryMng")
public interface CategoryMngControllerApi {
    @PostMapping("saveOrUpdateCategory")
    @ApiOperation(value = "新增或修改分类", notes = "新增或修改分类", httpMethod = "POST")
    public GraceJSONResult saveOrUpdateCategory(@RequestBody @Valid SaveCatrgoryBO saveCatrgoryBO,
                                                BindingResult result);
    @PostMapping("getCatList")
    @ApiOperation(value = "查询分类列表", notes = "查询分类列表", httpMethod = "POST")
    public GraceJSONResult getCatList();

    @GetMapping("getCats")
    @ApiOperation(value = "用户端查询分类列表", notes = "用户端查询分类列表", httpMethod = "GET")
    public GraceJSONResult getCats();
}
