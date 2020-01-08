package com.soft.eva.service.impl;

import com.soft.eva.dao.UserDao;
import com.soft.eva.domain.User;
import com.soft.eva.service.UserService;
import com.soft.eva.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Transactional
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userMapper;
    @Override
    public User get(String id) {
        User user = userMapper.get(id);
        return user;
    }

    @Override
    public User getByUsername(String name) {
        return userMapper.getByUsername(name);
    }

    @Override
    public List<User> list(Map<String, Object> map) {
        return userMapper.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return userMapper.count(map);
    }

    @Transactional
    @Override
    public int save(User user) {
        int count = userMapper.save(user);
        return count;
    }

    @Override
    public int update(User user) {
        int r = userMapper.update(user);
        return r;
    }

    @Override
    public int remove(String userId) {
        return userMapper.remove(userId);
    }

    @Override
    public int batchRemove(String[] userIds) {
        int count = userMapper.batchRemove(userIds);
        return count;
    }

    @Override
    public boolean exit(Map<String, Object> map) {
        boolean exit;
        exit = userMapper.list(map).size() > 0;
        return exit;
    }

    @Override
    public Set<String> listRoles(Long userId) {
        return null;
    }

    @Override
    public int resetPwd(UserVO userVo, User user) throws Exception {   return 0; }

    @Override
    public int adminResetPwd(UserVO userVO) throws Exception { return 0; }

    @Override
    public int updatePersonal(User user) {
        return userMapper.update(user);
    }
}
