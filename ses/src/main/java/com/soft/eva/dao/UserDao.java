package com.soft.eva.dao;

import com.soft.eva.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface UserDao {
    User get(Long id);
    User getByUsername(String name);
    List<User> list(Map<String,Object> map);
    int count(Map<String,Object> map);
    int save(User user);
    int update(User user);
    int remove(Long userId);
    int batchRemove(Long[] userIds);
}

