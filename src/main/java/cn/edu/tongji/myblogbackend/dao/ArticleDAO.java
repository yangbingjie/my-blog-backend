package cn.edu.tongji.myblogbackend.dao;

import cn.edu.tongji.myblogbackend.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Timestamp;

public interface ArticleDAO extends JpaRepository<ArticleEntity, String> {
    @Modifying
    @Transactional
    @Query(value= "update ArticleEntity set title=?1, contentHtml=?2," +
            " contentMarkdown=?3, preview=?4, isPublic=?5, updateTime=?6 where articleId=?7")
    void updateArticle(String title, String contentHtml, String contentMarkdown,
                       String preview, Integer isPublic, Timestamp updateTime, String articleId);

    ArticleEntity save(ArticleEntity articleEntity);
}
