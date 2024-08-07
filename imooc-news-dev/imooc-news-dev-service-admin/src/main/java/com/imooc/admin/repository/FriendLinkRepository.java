package com.imooc.admin.repository;

import com.imooc.pojo.mo.FriendLinkMO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendLinkRepository extends MongoRepository<FriendLinkMO, String> { //持久层
    // 内置提供了很多方法 find.. delete...
    public List<FriendLinkMO> getAllByIsDelete(Integer isDelete); //后面可以加ANDID
}