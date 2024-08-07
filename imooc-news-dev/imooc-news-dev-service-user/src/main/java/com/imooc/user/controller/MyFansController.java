package com.imooc.user.controller;

import com.github.pagehelper.PageHelper;
import com.imooc.api.BaseController;
import com.imooc.api.controller.user.MyFansControllerApi;
import com.imooc.enums.Sex;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.Fans;
import com.imooc.pojo.vo.FansCountsVO;
import com.imooc.pojo.vo.RegionRatioVO;
import com.imooc.user.mapper.FansMapper;
import com.imooc.user.service.MyFansService;
import com.imooc.utils.PagedGridResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MyFansController extends BaseController implements MyFansControllerApi {
    final static Logger logger = LoggerFactory.getLogger(MyFansController.class);

    @Autowired
    private MyFansService myFansService;



    @Override
    public GraceJSONResult isMeFollowThisWriter(String writerId, String fanId) {
        boolean res = myFansService.isMeFollowThisWriter(writerId,fanId);
        return GraceJSONResult.ok(res);
    }

    @Override
    public GraceJSONResult follow(String writerId, String fanId) {
        myFansService.follow(writerId,fanId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult unfollow(String writerId, String fanId) {
        myFansService.unfollow(writerId, fanId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult queryAll(String writerId, Integer page, Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        return GraceJSONResult.ok(myFansService.queryMyFansList(writerId, page, pageSize));
    }

    @Override
    public GraceJSONResult queryRatio(String writerId) {
        int manCount = myFansService.queryFansCounts(writerId, Sex.man);
        int womanCount = myFansService.queryFansCounts(writerId, Sex.woman);

        FansCountsVO fansCountsVO = new FansCountsVO();
        fansCountsVO.setManCounts(manCount);
        fansCountsVO.setWomanCounts(womanCount);
        return GraceJSONResult.ok(fansCountsVO);
    }

    @Override
    public GraceJSONResult queryRatioByRegion(String writerId) {
        return GraceJSONResult.ok(myFansService.queryRegionRatioCounts(writerId));
    }
}
