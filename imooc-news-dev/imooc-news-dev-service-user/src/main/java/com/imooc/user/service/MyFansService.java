package com.imooc.user.service;

import com.imooc.enums.Sex;
import com.imooc.pojo.vo.RegionRatioVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface MyFansService {
    /**
     * 查询当前用户是否关注作家
     */
    public boolean isMeFollowThisWriter(String writerId, String fanId);

    /**
     * 关注成为粉丝
     */
    public void follow(String writerId, String fanId);

    /**
     * 粉丝取消关注
     */
    public void unfollow(String writerId, String fanId);

    /**
     * 查询我的粉丝
     */
    public PagedGridResult queryMyFansList(String writerId, Integer page, Integer pageSize);

    /**
     * 查询粉丝数
     */
    public Integer queryFansCounts(String writerId, Sex sex);

    /**
     * 查询粉丝数
     */
    public List<RegionRatioVO> queryRegionRatioCounts(String writerId);
}
