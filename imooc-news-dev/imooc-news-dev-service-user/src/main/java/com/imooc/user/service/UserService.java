package com.imooc.user.service;

import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;

public interface UserService {
    /**
     * 判断用户是否存在，如果存在返回user信息
     */
    public AppUser queryMobileIsExist(String mobile);

    /**
     * 创建用户，新增用户记录到数据库
     */
    public AppUser createUser(String mobile);

    /**
     * 根据用户主键id查询用户信息
     * @param userId
     * @return
     */
    public AppUser getUser(String userId);

    /**
     * 用户修改信息，完善资料，并且激活
     * @param updateUserInfoBO
     */
    public void updateUserInfo(UpdateUserInfoBO updateUserInfoBO);
}
