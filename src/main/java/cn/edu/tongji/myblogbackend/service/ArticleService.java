package cn.edu.tongji.myblogbackend.service;

import cn.edu.tongji.myblogbackend.dao.ArticleDAO;
import cn.edu.tongji.myblogbackend.dao.LikeArticleDAO;
import cn.edu.tongji.myblogbackend.dao.StarArticleDAO;
import cn.edu.tongji.myblogbackend.entity.ArticleEntity;
import cn.edu.tongji.myblogbackend.entity.LikeArticleEntity;
import cn.edu.tongji.myblogbackend.entity.StarArticleEntity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.common.collect.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;


@Service
public class ArticleService {
    @Autowired
    ArticleDAO articleDAO;
    @Autowired
    FileService fileService;
    @Autowired
    LikeArticleDAO likeArticleDAO;
    @Autowired
    StarArticleDAO starArticleDAO;
    public Long getLikeCount(String articleId){
        return likeArticleDAO.countByAndArticleId(articleId);
    }
    public Long getStarCount(String articleId){
        return starArticleDAO.countByAndArticleId(articleId);
    }
    public LikeArticleEntity isLike(String articleId, String userId){return likeArticleDAO.getByArticleIdAndUserId(articleId, userId);}
    public StarArticleEntity isStar(String articleId, String userId){return starArticleDAO.getByArticleIdAndUserId(articleId, userId);}
    public Tuple<Long, Boolean> likeArticle(String articleId, String userId){
        ArticleEntity articleEntity = checkArticleAccess(articleId, userId);
        if (articleEntity != null){
            LikeArticleEntity likeArticleEntity = isLike(articleId, userId);
            if (likeArticleEntity == null){
                Timestamp time = new Timestamp(System.currentTimeMillis());
                likeArticleEntity = new LikeArticleEntity();
                likeArticleEntity.setArticleId(articleId);
                likeArticleEntity.setUserId(userId);
                likeArticleEntity.setLikeTime(time);
                likeArticleDAO.save(likeArticleEntity);
                return new Tuple<Long, Boolean>(likeArticleDAO.countByAndArticleId(articleId), true);
            }else{
                likeArticleDAO.delete(likeArticleEntity);
                return new Tuple<Long, Boolean>(likeArticleDAO.countByAndArticleId(articleId), false);
            }
        }else{
            return new Tuple<Long, Boolean>((long) -1, false);
        }
    }
    public Tuple<Long, Boolean> starArticle(String articleId, String userId){
        ArticleEntity articleEntity = checkArticleAccess(articleId, userId);
        if (articleEntity != null) {
            StarArticleEntity starArticleEntity = isStar(articleId, userId);
            if (starArticleEntity == null){
                Timestamp time = new Timestamp(System.currentTimeMillis());
                starArticleEntity = new StarArticleEntity();
                starArticleEntity.setArticleId(articleId);
                starArticleEntity.setUserId(userId);
                starArticleEntity.setStarTime(time);
                starArticleDAO.save(starArticleEntity);
                return new Tuple<Long, Boolean>(starArticleDAO.countByAndArticleId(articleId), true);
            }else{
                starArticleDAO.delete(starArticleEntity);
                return new Tuple<Long, Boolean>(starArticleDAO.countByAndArticleId(articleId), false);
            }
        }else{
            return new Tuple<Long, Boolean>((long) -1, false);
        }
    }
    public boolean isExist(String articleId){
        Optional<ArticleEntity> articleEntity = articleDAO.findById(articleId);
        return articleEntity.isPresent();
    }
    public boolean isAuthor(String articleId, String userId){
        Optional<ArticleEntity> articleEntity = articleDAO.findById(articleId);
        if (articleEntity.isPresent()){
            String authorId = articleEntity.get().getAuthorId();
            return authorId.equals(userId);
        }
        return true;
    }
    public String saveArticle(JSONObject jsonObject){
        ArticleEntity articleEntity = new ArticleEntity();
        Timestamp time = new Timestamp(System.currentTimeMillis());
        articleEntity.setAuthorId(jsonObject.getString("author_id"));
        articleEntity.setTitle(jsonObject.getString("title"));
        articleEntity.setContentHtml(jsonObject.getString("content_html"));
        articleEntity.setContentMarkdown(jsonObject.getString("content_markdown"));
        articleEntity.setIsPublic(jsonObject.getInteger("is_public"));
        articleEntity.setPreview(jsonObject.getString("preview"));
        String folder = jsonObject.getString("img_folder");
        articleEntity.setImgFolder(folder);
        if (jsonObject.getString("article_id") == null) {
            articleEntity.setCreateTime(time);
            articleEntity.setUpdateTime(time);
            articleEntity.setViewCount(0);
            articleDAO.save(articleEntity);
        } else {
            articleEntity.setUpdateTime(time);
            articleEntity.setArticleId(jsonObject.getString("article_id"));
            articleDAO.updateArticle(articleEntity.getTitle(),
                    articleEntity.getContentHtml(), articleEntity.getContentMarkdown(),
                    articleEntity.getPreview(), articleEntity.getIsPublic(),
                    articleEntity.getUpdateTime(), articleEntity.getArticleId());
        }
        JSONArray array = jsonObject.getJSONArray("img_list");
        fileService.removeUnusedArticleImg(array, folder);
        return articleEntity.getArticleId();
    }

    public ArticleEntity checkArticleAccess(String articleId, String userId){
        Optional<ArticleEntity> articleEntityOptional = articleDAO.findById(articleId);
        if (articleEntityOptional.isPresent()){
            ArticleEntity articleEntity = articleEntityOptional.get();
            if (articleEntity.getIsPublic() == 1 || articleEntity.getAuthorId().equals(userId)){
                return articleEntity;
            }
        }
        return null;
    }
    public ArticleEntity getArticleDetails(String articleId, String userId){
        ArticleEntity articleEntity = checkArticleAccess(articleId, userId);
        if (articleEntity != null){
            articleEntity.setViewCount(articleEntity.getViewCount() + 1);
            articleDAO.save(articleEntity);
            return articleEntity;
        }
        return null;
    }
}
