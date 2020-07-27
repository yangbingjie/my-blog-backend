package cn.edu.tongji.myblogbackend.service;

import cn.edu.tongji.myblogbackend.entity.UserEntity;
import cn.edu.tongji.myblogbackend.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class FileService {
    @Autowired
    UserService userService;
    @Autowired
    ArticleService articleService;
    private final String avatar_folder = "/Users/bella/Projects/my-blog-backend/src/main/resources/static/avatar/";
    private final String avatar_url_prefix = "http://localhost:8443/api/file/avatar/";
    private final String article_img_folder = "/Users/bella/Projects/my-blog-backend/src/main/resources/static/article_img/";
    private final String article_img_url_prefix = "http://localhost:8443/api/file/article_img/";


    public Map<String, String> saveArticleImg(MultipartFile file, String folder){
        Map<String, String> map = new HashMap<String, String>();
        if (folder.equals("") || folder.equals("null")){
            String time = String.valueOf(System.currentTimeMillis());
            folder = time + StringUtils.getRandomString(8);
        }
        String imgUrl = this.addFile(article_img_folder + folder, file);
        map.put("url", article_img_url_prefix + folder + '/' + imgUrl);
        map.put("folder", folder);
        return map;
    }

    public String setAvatar(MultipartFile file, String userId){
        if (!userService.isExist(userId)){
            return "";
        }
        UserEntity user = userService.getByUserId(userId);
        String oldImgUrl = user.getAvatar();
        if (oldImgUrl != null){
            String oldFileName = StringUtils.getLastString(oldImgUrl);
            this.removeFile(avatar_folder, oldFileName);
        }
        String imgUrl = this.addFile(avatar_folder, file);
        user.setAvatar(avatar_url_prefix + imgUrl);
        userService.save(user);
        return avatar_url_prefix + imgUrl;
    }
    private String addFile(String folder, MultipartFile file){
        File imageFolder = new File(folder);
        String time = String.valueOf(System.currentTimeMillis());
        String filename = time + StringUtils.getRandomString(6)
                 + file.getOriginalFilename().substring(file.getOriginalFilename().length() - 4);

        File f = new File(imageFolder, filename);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        try {
            file.transferTo(f);
            return f.getName();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    public void removeUnusedArticleImg(JSONArray array, String folder){
        Set<String> new_files = new HashSet<String>();
        for(int i = 0; i < array.size(); i++){
            new_files.add(array.getString(i));
        }
        String path = article_img_folder + folder + '/';
        File file = new File(path);
        File[] old_files = file.listFiles();

        for (int i = 0; (old_files != null) && i < old_files.length; ++i){
            if (new_files.isEmpty() || !new_files.contains(old_files[i].getName())){
                removeArticleImg(folder, old_files[i].getName());
            }
        }
    }
    private void removeArticleImg(String folder, String filename){
        removeFile(article_img_folder + folder + '/', filename);
    }
    private void removeFile(String folder, String filename){
        File f = new File(folder, filename);
        if (f.exists()){
            f.delete();
        }
    }
}
