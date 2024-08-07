package com.imooc.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.admin.mapper.AdminUserMapper;
import com.imooc.admin.repository.FriendLinkRepository;
import com.imooc.admin.service.AdminUserService;
import com.imooc.admin.service.FriendLinkService;
import com.imooc.enums.YesOrNo;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.pojo.mo.FriendLinkMO;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class FriendLinkServiceImpl implements FriendLinkService {

    @Autowired
    private FriendLinkRepository friendLinkRepository;
    @Override
    public void saveOrUpdateFriendLink(FriendLinkMO friendLinkMO) {
        friendLinkRepository.save(friendLinkMO); //有id更新 无id直接保存
    }

    @Override
    public List<FriendLinkMO> queryAllFriendLinkList() {
//        Pageable pageable = PageRequest.of(1,10);
//        friendLinkRepository.findAll(pageable);
        return friendLinkRepository.findAll();
    }

    @Override
    public void delete(String linkId) {
        friendLinkRepository.deleteById(linkId);
    }

    @Override
    public List<FriendLinkMO> queryPortalAllFriendLinkList() {
        return friendLinkRepository.getAllByIsDelete(YesOrNo.NO.type);
    }
}
