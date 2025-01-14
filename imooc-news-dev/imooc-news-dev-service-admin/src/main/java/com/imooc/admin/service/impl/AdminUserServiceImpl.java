package com.imooc.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.admin.mapper.AdminUserMapper;
import com.imooc.admin.service.AdminUserService;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired
    public AdminUserMapper adminUserMapper;
    @Autowired
    public Sid sid;
    @Override
    public AdminUser queryAdminByUsername(String username) {
        Example adminExample = new Example(AdminUser.class);
        Example.Criteria Criteria = adminExample.createCriteria();
        Criteria.andEqualTo("username",username);
        AdminUser admin = adminUserMapper.selectOneByExample(adminExample);
        return admin;
    }

    @Override
    public void createAdminUser(NewAdminBO newAdminBO) {
        String adminId = sid.nextShort(); //获得主键
        AdminUser adminUser = new AdminUser();
        adminUser.setId(adminId);
        adminUser.setUsername(newAdminBO.getUsername());
        adminUser.setAdminName(newAdminBO.getAdminName());
        // 如果密码不为空 则密码需要加密 存入数据库
        if (StringUtils.isNotBlank(newAdminBO.getPassword())){
            String pwd = BCrypt.hashpw(newAdminBO.getPassword(), BCrypt.gensalt());
            adminUser.setPassword(pwd);
        }

        // 如果人脸上传以后，则有faceId，需要和admin信息关联存储入库
        if (StringUtils.isNotBlank(newAdminBO.getFaceId())){
            adminUser.setFaceId(newAdminBO.getFaceId());
        }
        adminUser.setCreatedTime(new Date());
        adminUser.setUpdatedTime(new Date());

        int insert = adminUserMapper.insert(adminUser);
        if (insert != 1){
            GraceException.display(ResponseStatusEnum.ADMIN_CREATE_ERROR);
        }
    }

    @Override
    public PagedGridResult queryAdminList(Integer page, Integer pageSize) {
        Example adminExample = new Example(AdminUser.class);
        adminExample.orderBy("createdTime").asc();
        PageHelper.startPage(page, pageSize);
        List<AdminUser> adminUserList = adminUserMapper.selectByExample(adminExample);
        return setterPagedGrid(adminUserList, page);
    }

    private PagedGridResult setterPagedGrid( List<?> adminUserList, Integer page){ //类型是? 后期不确定是什么泛型
        PageInfo<?> pageList = new PageInfo<>(adminUserList);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(adminUserList);
        gridResult.setPage(page);
        gridResult.setRecords(pageList.getPages());
        gridResult.setTotal(pageList.getTotal());
        return gridResult;

    }
}
