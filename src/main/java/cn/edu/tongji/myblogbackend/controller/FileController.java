package cn.edu.tongji.myblogbackend.controller;

import cn.edu.tongji.myblogbackend.entity.UserEntity;
import cn.edu.tongji.myblogbackend.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Controller
public class FileController {
    @CrossOrigin
    @PostMapping(value = "api/file/avatar")
    @ResponseBody
    public String avatarUpload(MultipartFile file) throws Exception {
        String folder = "/Users/bella/Projects/my-blog-backend/src/main/resources/static/profile_avatar";
        File imageFolder = new File(folder);
        String time = String.valueOf(new Date());
        File f = new File(imageFolder, StringUtils.getRandomString(6) + time + file.getOriginalFilename()
                .substring(file.getOriginalFilename().length() - 4));
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        try {
            file.transferTo(f);
            String imgURL = "http://localhost:8443/api/file/cover/" + f.getName();
//            String userId = file.data;
            return imgURL;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
