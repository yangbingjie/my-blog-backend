package cn.edu.tongji.myblogbackend.controller;

import cn.edu.tongji.myblogbackend.entity.ArticleEntity;
import cn.edu.tongji.myblogbackend.entity.UserEntity;
import cn.edu.tongji.myblogbackend.service.ArticleService;
import cn.edu.tongji.myblogbackend.service.UserService;
import cn.edu.tongji.myblogbackend.utils.TimeUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "api/article")
public class ArticleController {
    @Autowired
    ArticleService articleService;
    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping(value = "/save")
    @ResponseBody
    public Object saveArticle(@RequestBody JSONObject requestBody) {
        JSONObject res = new JSONObject();
        String articleId = requestBody.getString("article_id");
        String userId = requestBody.getString("user_id");
        if (articleId != null && !articleService.isAuthor(articleId, userId)){
            res.put("code", 400);
            res.put("errmsg", "权限不足");
        } else {
            articleId = articleService.saveArticle(requestBody);
            if (articleId == null){
                res.put("code", 400);
                res.put("errmsg", "网络错误");
            } else {
                res.put("code", 200);
                res.put("article_id", articleId);
            }
        }
        return res;
    }

    @CrossOrigin
    @PostMapping(value = "/getArticleById")
    @ResponseBody
    public Object getArticleDetails(@RequestBody JSONObject requestBody){
        JSONObject res = new JSONObject();
        String articleId = requestBody.getString("article_id");
        String userId = requestBody.getString("user_id");
        ArticleEntity articleEntity = articleService.getArticleDetails(articleId, userId);
        if (articleEntity != null){
            res.put("code", 200);
            JSONObject article = new JSONObject();
            UserEntity user = userService.getByUserId(articleEntity.getAuthorId());
            article.put("author_name", user.getUsername());
            article.put("title", articleEntity.getTitle());
            article.put("content_html", articleEntity.getContentHtml());
            article.put("preview", articleEntity.getPreview());
            article.put("content_markdown", articleEntity.getContentMarkdown());
            article.put("is_public", articleEntity.getIsPublic());
            article.put("view_count", articleEntity.getViewCount());
            article.put("like_count", articleEntity.getLikeCount());
            article.put("star_count", articleEntity.getStarCount());
            article.put("update_time", TimeUtils.format(articleEntity.getUpdateTime()));
            article.put("author_id", articleEntity.getAuthorId());
            article.put("img_folder", articleEntity.getImgFolder());
            res.put("article", article);
        }else {
            res.put("code", 400);
            res.put("errmsg", "文章不存在");
        }
        return res;
    }
}
