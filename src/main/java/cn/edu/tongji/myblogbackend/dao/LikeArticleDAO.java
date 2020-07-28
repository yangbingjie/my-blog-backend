package cn.edu.tongji.myblogbackend.dao;

import cn.edu.tongji.myblogbackend.entity.LikeArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeArticleDAO extends JpaRepository<LikeArticleEntity, String> {
    Long countByAndArticleId(String articleId);
    LikeArticleEntity getByArticleIdAndUserId(String articleId, String userId);
    LikeArticleEntity save(LikeArticleEntity likeArticleEntity);
}
