package cn.edu.tongji.myblogbackend.dao;

import cn.edu.tongji.myblogbackend.entity.StarArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarArticleDAO extends JpaRepository<StarArticleEntity, String> {
    Long countByAndArticleId(String articleId);
    StarArticleEntity getByArticleIdAndUserId(String articleId, String userId);
    StarArticleEntity save(StarArticleEntity starArticleEntity);
}
