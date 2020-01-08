package com.soft.eva.service;

import com.soft.eva.domain.User;
import com.soft.eva.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public interface UserService{
    User get(String id);
    User getByUsername(String name);
    List<User> list(Map<String,Object> map);
    int count(Map<String,Object> map);
    int save(User user);
    int update(User user);
    int remove(String userId);
    int batchRemove(String[] userIds);
    boolean exit(Map<String,Object> map);
    Set<String> listRoles(Long userId);
    int resetPwd(UserVO userVo,User user) throws Exception;
    int adminResetPwd(UserVO userVO) throws Exception;
    int updatePersonal(User user);
}
