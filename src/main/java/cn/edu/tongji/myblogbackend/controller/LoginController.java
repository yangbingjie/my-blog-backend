package cn.edu.tongji.myblogbackend.controller;

import cn.edu.tongji.myblogbackend.entity.UserEntity;
import cn.edu.tongji.myblogbackend.result.Result;
import cn.edu.tongji.myblogbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping(value = "api/login")
    @ResponseBody
    public Result login(@RequestBody UserEntity requestUser, HttpSession session){
        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username);
        UserEntity userEntity = userService.get(username, requestUser.getPassword());
        if (null == userEntity){
            String message = "账号密码错误";
            System.out.println("username or password errow");
            return new Result(400);
        }else {
            session.setAttribute("user", userEntity);
            return new Result(200);
        }
    }
}
