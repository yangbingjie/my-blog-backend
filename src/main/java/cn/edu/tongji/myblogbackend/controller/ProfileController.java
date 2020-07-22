package cn.edu.tongji.myblogbackend.controller;

import cn.edu.tongji.myblogbackend.utils.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Controller
public class ProfileController {
    @CrossOrigin
    @PostMapping(value = "api/file/cover")
    @ResponseBody
    public String coverUpload(MultipartFile file) throws Exception {
        String folder = "/Users/bella/Projects/my-blog-backend/src/main/resources/static/profile_photo";
        File imageFolder = new File(folder);
        String time = String.valueOf(new Date());
        File f = new File(imageFolder, StringUtils.getRandomString(6) + time + file.getOriginalFilename()
                .substring(file.getOriginalFilename().length() - 4));
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        try {
            file.transferTo(f);
            String imgURL = "http://localhost:8443/api/file/cover/" + f.getName();
            return imgURL;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
