package com.imooc.admin.controller;

import com.imooc.admin.repository.FriendLinkRepository;
import com.imooc.admin.service.FriendLinkService;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.FriendLinkControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.SaveFriendLinkBO;
import com.imooc.pojo.mo.FriendLinkMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class FriendLinkController extends BaseController implements FriendLinkControllerApi {
    final static Logger logger = LoggerFactory.getLogger(FriendLinkController.class);
    @Autowired
    private FriendLinkService friendLinkService;

    @Override
    public GraceJSONResult saveOrUpdateFriendLink(SaveFriendLinkBO saveFriendLinkBO, BindingResult result) {
        if (result.hasErrors()){
            Map<String, String> map = getErrors(result);
            return GraceJSONResult.errorMap(map);
        }

//      saveFriendLinkBO -> ***Mo  MongoDB校验的对象
        FriendLinkMO friendLinkMO = new FriendLinkMO();
        BeanUtils.copyProperties(saveFriendLinkBO,friendLinkMO);
        friendLinkMO.setCreateTime(new Date());
        friendLinkMO.setUpdateTime(new Date());

        friendLinkService.saveOrUpdateFriendLink(friendLinkMO);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getFriendLinkList() {

        return GraceJSONResult.ok(friendLinkService.queryAllFriendLinkList());
    }

    @Override
    public GraceJSONResult delete(String linkId) {
        friendLinkService.delete(linkId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult queryPortalAllFriendLinkList() {
        List<FriendLinkMO> list = friendLinkService.queryPortalAllFriendLinkList();
        return GraceJSONResult.ok(list);
    }
}