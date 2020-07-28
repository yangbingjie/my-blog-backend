package cn.edu.tongji.myblogbackend.service;

import cn.edu.tongji.myblogbackend.entity.UserEntity;
import cn.edu.tongji.myblogbackend.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import org.elasticsearch.common.collect.Tuple;
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


    public Tuple<String, String> saveArticleImg(MultipartFile file, String folder){
        if (folder.equals("") || folder.equals("null")){
            String time = String.valueOf(System.currentTimeMillis());
            folder = time + StringUtils.getRandomString(8);
        }
        String imgUrl = this.addFile(article_img_folder + folder, file);
        return new Tuple<String, String>(article_img_url_prefix + folder + '/' + imgUrl, folder);
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
        String suffix = file.getOriginalFilename();
        if (suffix == null){
            return "";
        }
        suffix = suffix.substring(suffix.length() - 4);
        suffix = suffix.toLowerCase();
        if (suffix.equals("jpeg")){
            suffix = ".jpg";
        }
        if(!suffix.equals(".png") && !suffix.equals(".jpg")){
            return "";
        }
        String filename = time + StringUtils.getRandomString(6)
                 + suffix;

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
        Set<String> newFiles = new HashSet<String>();
        for(int i = 0; i < array.size(); i++){
            newFiles.add(array.getString(i));
        }
        String path = article_img_folder + folder + '/';
        File file = new File(path);
        File[] oldFiles = file.listFiles();
        // 遍历oldFiles，如果在newFiles里找不到则删除该图片
        for (int i = 0; (oldFiles != null) && i < oldFiles.length; ++i){
            if (newFiles.isEmpty() || !newFiles.contains(oldFiles[i].getName())){
                removeArticleImg(folder, oldFiles[i].getName());
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
