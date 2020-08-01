package cn.edu.tongji.myblogbackend.dao;

import cn.edu.tongji.myblogbackend.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface ArticleDAO extends JpaRepository<ArticleEntity, String> {
    @Modifying
    @Transactional
    @Query(value= "update ArticleEntity set title=?1, contentHtml=?2," +
            " contentMarkdown=?3, preview=?4, isPublic=?5, updateTime=?6, cover=?7 where articleId=?8")
    void updateArticle(String title, String contentHtml, String contentMarkdown,
                       String preview, Integer isPublic, Timestamp updateTime,
                       String cover, String articleId);
    ArticleEntity save(ArticleEntity articleEntity);

    @Query(value="select article_id, title, author_id, update_time, preview, cover, view_count " +
            "from myblog.article a left join myblog.user u " +
            "on a.author_id=u.user_id " +
            "where a.is_public = 1 and (u.username like ?1 or a.title like ?1 or a.preview like ?1 or a.content_markdown like ?1)",nativeQuery = true)
    List<Map<String, Object>> searchByAll(String title);

    @Query(value="select article_id, title, author_id, update_time, preview, cover, view_count " +
            "from myblog.article " +
            "where is_public = 1 and title like ?1",nativeQuery = true)
    List<Map<String, Object>> searchByTitle(String title);

    @Query(value="select article_id, title, author_id, update_time, preview, cover, view_count " +
            "from myblog.article a left join myblog.user u " +
            "on a.author_id=u.user_id " +
            "where ais_public = 1 and u.user_name like ?1",nativeQuery = true)
    List<Map<String, Object>> searchByAuthorName(String authorName);

    @Query(value="select article_id, title, author_id, update_time, preview, cover, view_count " +
            "from myblog.article " +
            "where is_public = 1 and author_id = ?1",nativeQuery = true)
    List<Map<String, Object>> searchByAuthorId(String authorId);
}
