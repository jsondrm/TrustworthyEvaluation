package com.soft.eva.web;

import com.soft.eva.domain.SoftwareProduct;
import com.soft.eva.domain.User;
import com.soft.eva.service.SoftwareProductService;
import com.soft.eva.service.UserService;
import com.soft.eva.util.ReturnUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    private SoftwareProductService softwareProductService;


    @RequestMapping(value = "/",method = RequestMethod.GET)
    String login() {
        return "login";
    }

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    String register() {
        return "register";
    }


    @RequestMapping(value = "/ajaxLogin",method = RequestMethod.POST)
    @ResponseBody
    ReturnUtil ajaxLogin(String username, String password){
        //将status为1即测评中的软件状态重置为待测评
        List<SoftwareProduct> softwareProductList = softwareProductService.findAll();
        for(int i = 0; i < softwareProductList.size(); i++){
            SoftwareProduct softwareProduct = softwareProductList.get(i);
            int status = softwareProduct.getStatus();
            if(status == 1){
                softwareProduct.setStatus(0);
                softwareProductService.update(softwareProduct);
            }
        }
        //获取用户信息，进行校验
        User user = userService.getByUsername(username);
        if(user == null){
            return ReturnUtil.error("用户名不存在，请重新输入");
        }else{
            String curPwd = user.getPassword();
            if(!curPwd.equals(password)){
                 return ReturnUtil.error("用户名和密码不匹配，请重新输入");
            }
        }
        return ReturnUtil.ok();
    }

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    String index() {
        return "index";
    }

    @RequestMapping(value = "/main",method = RequestMethod.GET)
    String main() {
        return "main";
    }


    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    String logout() {
        //ShiroUtils.logout();
        return "redirect:/login";
    }
}
