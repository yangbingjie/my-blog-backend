package cn.edu.tongji.myblogbackend.controller;

import cn.edu.tongji.myblogbackend.entity.ArticleEntity;
import cn.edu.tongji.myblogbackend.entity.UserEntity;
import cn.edu.tongji.myblogbackend.service.ArticleService;
import cn.edu.tongji.myblogbackend.service.UserService;
import cn.edu.tongji.myblogbackend.utils.TimeUtils;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.common.collect.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "api/article")
public class ArticleController {
    @Autowired
    ArticleService articleService;
    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping(value = "/editAuth")
    @ResponseBody
    public JSONObject editAuth(@RequestBody JSONObject requestBody){
        JSONObject res = new JSONObject();
        String articleId = requestBody.getString("article_id");
        String userId = requestBody.getString("user_id");
        if (!articleService.isAuthor(articleId, userId)){
            res.put("code", 400);
            res.put("errmsg", "权限不足");
        } else {
            Integer isPublic = articleService.editAuth(articleId);
            if (isPublic != -1){
                res.put("is_public", isPublic);
                res.put("code", 200);
            }else{
                res.put("code", 400);
                res.put("errmsg", "网络错误");
            }
        }
        return res;
    }

    @CrossOrigin
    @PostMapping(value = "/save")
    @ResponseBody
    public JSONObject saveArticle(@RequestBody JSONObject requestBody) {
        JSONObject res = new JSONObject();
        String articleId = requestBody.getString("article_id");
        String userId = requestBody.getString("author_id");
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
    @PostMapping(value = "/search")
    @ResponseBody
    public Object searchArticle(@RequestBody JSONObject requestBody){
        String searchType = requestBody.getString("search_type");
        String query = requestBody.getString("query");
        String userId = requestBody.getString("user_id");
        JSONObject res = new JSONObject();
        JSONObject articles= articleService.searchArticle(searchType, query, userId);
        if (articles != null){
            res.put("code", 200);
            res.put("all_tag_list", articles.getJSONArray("all_tag_list"));
            res.put("article_list", articles.getJSONArray("article_list"));
        }else{
            res.put("code", 400);
            res.put("errmsg", "网络错误");
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
            article.put("author_avatar", user.getAvatar());
            article.put("title", articleEntity.getTitle());
            article.put("content_html", articleEntity.getContentHtml());
            article.put("preview", articleEntity.getPreview());
            article.put("content_markdown", articleEntity.getContentMarkdown());
            article.put("is_public", articleEntity.getIsPublic());
            article.put("view_count", articleEntity.getViewCount());
            // [{"tag_id":null, "tag_name":"C++"}, {"tag_id":"323dfs", "tag_name":"C--"}]
            article.put("tag_list", articleService.getTagList(articleId));
            article.put("like_count", articleService.getLikeCount(articleId));
            article.put("is_like", articleService.isLike(articleId, userId) != null);
            article.put("star_count", articleService.getStarCount(articleId));
            article.put("is_star", articleService.isStar(articleId, userId) != null);
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

    @CrossOrigin
    @PostMapping(value = "/like")
    @ResponseBody
    public JSONObject likeArticle(@RequestBody JSONObject requestBody){
        String articleId = requestBody.getString("article_id");
        String userId = requestBody.getString("user_id");
        JSONObject res = new JSONObject();
        Tuple<Long, Boolean> tuple = articleService.likeArticle(articleId, userId);
        if (tuple.v1() == -1){
            res.put("code", 400);
            res.put("errmsg", "文章不存在或没有访问权限");

        }else{
            res.put("code", 200);
            res.put("like_count", tuple.v1());
            res.put("is_like", tuple.v2());
        }
        return res;
    }

    @CrossOrigin
    @PostMapping(value = "/star")
    @ResponseBody
    public JSONObject starArticle(@RequestBody JSONObject requestBody){
        String articleId = requestBody.getString("article_id");
        String userId = requestBody.getString("user_id");
        Tuple<Long, Boolean> tuple= articleService.starArticle(articleId, userId);
        JSONObject res = new JSONObject();
        if (tuple.v1() == -1){
            res.put("code", 400);
            res.put("errmsg", "文章不存在或没有访问权限");
        }else{
            res.put("code", 200);
            res.put("star_count", tuple.v1());
            res.put("is_star", tuple.v2());
        }
        return res;
    }

}
