package com.imooc.user.service.impl;

import com.imooc.enums.Sex;
import com.imooc.enums.UserStatus;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.user.mapper.AppUserMapper;
import com.imooc.user.service.UserService;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import com.imooc.utils.DateUtil;

import java.util.Date;

@Service
public class UserServiceimpl implements UserService {
    @Autowired
    public AppUserMapper appUserMapper; //基本的CRUD都可以

    @Autowired
    public Sid sid;
    public static final String REDIS_USER_INFO = "redis_user_info";//ctrl+shift+u直接大写


    @Autowired
    public RedisOperator redis;

    private static final String USER_FACE0 = "https://raw.githubusercontent.com/P-luminary/images/10d94134b65e13cc8ec9b8a9aeae4f958921cab7/data/Imooc_Cat.jpg";
    private static final String USER_FACE1 = "https://raw.githubusercontent.com/P-luminary/images/875ad52658686e6cc3a8e0cd75d2a324a3d742a9/data/Imooc_Girl.jpg";
    @Override
    public AppUser queryMobileIsExist(String mobile) {
        Example userExample = new Example(AppUser.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("mobile", mobile);
        AppUser user = appUserMapper.selectOneByExample(userExample);
        return user;
    }

    @Transactional //对整个类的方法，事务起作用。无异常时正常提交，有异常时数据回滚
    @Override
    public AppUser createUser(String mobile) {
        /**
         * 互联网项目都要考虑可扩展性
         * 如果未来的业务激增，那么就需要分表分库
         * 那么数据库表主键id必须保证全局(全库)唯一,不得重复
         */
        String userId = sid.nextShort();
        AppUser user = new AppUser();
        user.setId(userId);
        user.setMobile(mobile);
        user.setNickname("用户：" + DesensitizationUtil.commonDisplay(mobile)); //給手机号加** 是脱敏操作
        user.setFace(USER_FACE1);
        user.setBirthday(DateUtil.stringToDate("2024-06-29")); //字符串转换Date类型
        user.setSex(Sex.secret.type);
        user.setActiveStatus(UserStatus.INACTIVE.type);//是否激活
        user.setTotalIncome(0);//收入
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        appUserMapper.insert(user);
        return user;
    }

    @Override
    public AppUser getUser(String userId) {
        return appUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void updateUserInfo(UpdateUserInfoBO updateUserInfoBO){
        String userId = updateUserInfoBO.getId();
        // 保证双写一致,先删除redis中的数据,后更新数据库
//        redis.del(REDIS_USER_INFO + ":" + userId);

        AppUser userInfo = new AppUser();
        BeanUtils.copyProperties(updateUserInfoBO, userInfo);

        userInfo.setUpdatedTime(new Date());
        userInfo.setActiveStatus(UserStatus.ACTIVE.type);
        //appUserMapper.updateByPrimaryKey()//数据中现有的数据覆盖为空的
        int result = appUserMapper.updateByPrimaryKeySelective(userInfo);
        if (result != 1){
            //更新操作有问题
            GraceException.display(ResponseStatusEnum.USER_UPDATE_ERROR);
        }
        // 再次查询用户的最新信息,放入redis中
        AppUser user = getUser(userId);
        redis.set(REDIS_USER_INFO + ":" + userId, JsonUtils.objectToJson(user));

        // 缓存双删策略 [不处理可能会缓存击穿]
        try {
            Thread.sleep(100);
            redis.del(REDIS_USER_INFO + ":" + userId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
