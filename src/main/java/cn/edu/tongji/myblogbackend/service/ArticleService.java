package cn.edu.tongji.myblogbackend.service;

import cn.edu.tongji.myblogbackend.dao.ArticleDAO;
import cn.edu.tongji.myblogbackend.entity.ArticleEntity;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;


@Service
public class ArticleService {
    @Autowired
    ArticleDAO articleDAO;
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
        if (jsonObject.getString("article_id") == null) {
            articleEntity.setCreateTime(time);
            articleEntity.setUpdateTime(time);
            articleEntity.setLikeCount(0);
            articleEntity.setStarCount(0);
            articleEntity.setViewCount(0);
            articleDAO.save(articleEntity);
        } else {
            articleEntity.setUpdateTime(time);
            articleEntity.setArticleId(jsonObject.getString("article_id"));
            articleDAO.updateArticle(articleEntity.getTitle(),
                    articleEntity.getContentHtml(),
                    articleEntity.getContentMarkdown(), articleEntity.getPreview(),
                    articleEntity.getIsPublic(), articleEntity.getCreateTime(),
                    articleEntity.getUpdateTime(), articleEntity.getArticleId());
        }
        return articleEntity.getArticleId();
    }

    public ArticleEntity getArticleDetails(String articleId, String userId){
        Optional<ArticleEntity> articleEntityOptional = articleDAO.findById(articleId);
        if (articleEntityOptional.isPresent()){
            ArticleEntity articleEntity = articleEntityOptional.get();
            if (articleEntity.getIsPublic() == 1 || articleEntity.getAuthorId().equals(userId)){
                return articleEntity;
            }
        }
        return null;
    }
}
