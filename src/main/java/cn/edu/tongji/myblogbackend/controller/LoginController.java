package cn.edu.tongji.myblogbackend.controller;

import cn.edu.tongji.myblogbackend.pojo.User;
import cn.edu.tongji.myblogbackend.result.Result;
import cn.edu.tongji.myblogbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.Objects;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping(value = "api/login")
    @ResponseBody
    public Result login(@RequestBody User requestUser, HttpSession session){
        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username);
        User user = userService.get(username, requestUser.getPassword());
        if (null == user){
            String message = "账号密码错误";
            System.out.println("username or password errow");
            return new Result(400);
        }else {
            session.setAttribute("user", user);
            return new Result(200);
        }
    }
}
