package cn.edu.tongji.myblogbackend.controller;

import cn.edu.tongji.myblogbackend.entity.UserEntity;
import cn.edu.tongji.myblogbackend.service.FileService;
import cn.edu.tongji.myblogbackend.service.UserService;
import cn.edu.tongji.myblogbackend.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
@RequestMapping(value = "api/file")
public class FileController {
    @Autowired
    FileService fileService;

    @CrossOrigin
    @PostMapping(value = "/uploadAvatar")
    @ResponseBody
    public String avatarUpload(MultipartFile file, @RequestParam(value="user_id")String userId) throws Exception {
        // TODO add token
        return fileService.setAvatar(file, userId);
    }

    @CrossOrigin
    @PostMapping(value = "/uploadArticleImg")
    @ResponseBody
    public JSONObject articleImgUpload(@RequestParam(value="folder")String folder, @RequestParam(value="image")MultipartFile file) throws Exception {
        // TODO add token
        JSONObject res = new JSONObject();
        try {
            Map<String, String> m = fileService.saveArticleImg(file, folder);
            res.put("code", 200);
            res.put("url", m.get("url"));
            res.put("folder", m.get("folder"));
        } catch (Exception e) {
            e.printStackTrace();
            res.put("code", 400);
            res.put("errmsg", "网络错误");
        }
        return res;
    }

    @CrossOrigin
    @PostMapping(value = "/removeArticleImg")
    @ResponseBody
    public JSONObject removeArticleImg(@RequestBody JSONObject requestBody) throws Exception {
        // TODO add token
        JSONObject res = new JSONObject();
        try {
            String folder = requestBody.getString("folder");
            String fileUrl = requestBody.getString("file_url");
            String filename = StringUtils.getLastString(fileUrl);
            fileService.removeArticleImg(folder, filename);
            res.put("code", 200);
        }catch (Exception e) {
            e.printStackTrace();
            res.put("code", 400);
            res.put("errmsg", "网络错误");
        }
        return res;
    }
}
