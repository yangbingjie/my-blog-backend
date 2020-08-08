package cn.edu.tongji.myblogbackend.controller;

import cn.edu.tongji.myblogbackend.entity.UserEntity;
import cn.edu.tongji.myblogbackend.result.Result;
import cn.edu.tongji.myblogbackend.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "api/user")
public class UserController {
    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping(value = "/getProfile")
    @ResponseBody
    public Object getProfile(@RequestBody JSONObject requestBody) {
        JSONObject res = new JSONObject();
        String userId = requestBody.getString("user_id");
        UserEntity userEntity = userService.getByUserId(userId);
        if (userEntity != null){
            JSONObject object = new JSONObject();
            object.put("username", userEntity.getUsername());
            object.put("avatar", userEntity.getAvatar());
            object.put("description", userEntity.getDescription());
            res.put("code", 200);
            res.put("user", object);
        }else{
            res.put("code", 400);
            res.put("errmsg", "用户不存在");
        }
        return res;
    }

    @CrossOrigin
    @PostMapping(value = "/editProfile")
    @ResponseBody
    public Object editProfile(@RequestBody JSONObject requestBody) {
        JSONObject res = new JSONObject();
        String userId = requestBody.getString("user_id");
        UserEntity userEntity = userService.getByUserId(userId);
        if (userEntity != null){
            userEntity.setDescription(requestBody.getString("description"));
            userEntity.setUsername(requestBody.getString("username"));
            userService.save(userEntity);
            res.put("code", 200);
        }else{
            res.put("code", 400);
            res.put("errmsg", "用户不存在");
        }
        return res;
    }

    @CrossOrigin
    @PostMapping(value = "/login")
    @ResponseBody
    public Object login(@RequestBody UserEntity requestUser, HttpSession session){
        JSONObject res = new JSONObject();

        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username);
        UserEntity userEntity = userService.get(username, requestUser.getPassword());
        if (null == userEntity){
            String message = "账号密码错误";
            res.put("code", 400);
            res.put("errmsg", "网络错误");
        }else {
            JSONObject user = new JSONObject();
            session.setAttribute("user", userEntity);
            res.put("code", 200);
            user.put("user_id", userEntity.getUserId());
            user.put("role", userEntity.getRole());
            user.put("avatar", userEntity.getAvatar());
            res.put("user", user);
        }
        return res;
    }
}
