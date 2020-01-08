package com.soft.eva.web;

import com.soft.eva.domain.User;
import com.soft.eva.service.UserService;
import com.soft.eva.util.PageUtils;
import com.soft.eva.util.QueryCondition;
import com.soft.eva.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(value = "/user")
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "",method = RequestMethod.GET)
    public String user(){
        return "user/userList";
    }

    /**
     * 查询用户列表
      * @param params
     * @return
     */
    @RequestMapping(value = "/userList",method = RequestMethod.GET)
    @ResponseBody
    public PageUtils userList(@RequestParam Map<String,Object> params){
        QueryCondition queryCondition = new QueryCondition(params);
        List<User> userList = userService.list(queryCondition);
        int total = userService.count(queryCondition);
        PageUtils pageUtils = new PageUtils(userList, total);
        return pageUtils;
    }

    /**
     * 添加用户
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public String add(){
        return "user/add";
    }

    /**
     * 编辑用户
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    public String edit(Model model, @PathVariable("id") String id){
        User user = userService.get(id);
        model.addAttribute("user", user);
        return "user/edit";
    }

    /**
     * 保存用户
     * @param user
     * @return
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public ReturnUtil save(User user){
        User userIdExisted = userService.get(user.getUserId());
        User usernameExisted = userService.getByUsername(user.getUsername());
        if(userIdExisted == null && usernameExisted == null){
            if(userService.save(user) > 0){
                return ReturnUtil.ok();
            }
        }
        if(usernameExisted != null){
            return ReturnUtil.error("该用户名已存在！");
        }
        return ReturnUtil.error("该工号已存在！");
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ResponseBody
    public ReturnUtil update(User user){
        if(userService.update(user) > 0){
            return ReturnUtil.ok();
        }
        return ReturnUtil.error();
    }
    /**
     * 删除用户
     * @param id
     * @return
     */
    @RequestMapping(value = "/remove",method = RequestMethod.POST)
    @ResponseBody
    public ReturnUtil remove(String id){
        if(userService.remove(id) > 0){
            return ReturnUtil.ok();
        }
        return ReturnUtil.error();
    }
    /**
     * 批量删除用户
     * @param ids
     * @return
     */
    @RequestMapping(value = "/batchRemove",method = RequestMethod.POST)
    @ResponseBody
    public ReturnUtil batchRemove(@RequestParam("ids[]") String[] ids){
        if(userService.batchRemove(ids) > 0){
            return ReturnUtil.ok();
        }
        return ReturnUtil.error();
    }

    /**
     * 请求更改用户密码
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "/resetPwd/{id}",method = RequestMethod.GET)
    public String resetPwd(Model model, @PathVariable("id") String id){
        User user = new User();
        user.setUserId(id);
        model.addAttribute("user", user);
        return "user/resetPwd";
    }

    @PostMapping("/exit")
    @ResponseBody
    boolean exit(@RequestParam Map<String, Object> params) {
        // 存在，不通过，false
        return !userService.exit(params);
    }
}
