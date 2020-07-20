package cn.edu.tongji.myblogbackend.controller;

import cn.edu.tongji.myblogbackend.pojo.User;
import cn.edu.tongji.myblogbackend.result.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.HtmlUtils;

import java.util.Objects;

public class LoginController {
    public Result login(@RequestBody User requestUser){
        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username);
        if (!Objects.equals("admin", username) || !Objects.equals("123456", requestUser.getPassword())){
            String message = "账号密码错误";
            System.out.println("username or password errow");
            return new Result(400);
        }else {
            return new Result(200);
        }
    }
}
