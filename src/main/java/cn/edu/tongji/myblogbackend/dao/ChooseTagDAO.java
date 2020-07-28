package cn.edu.tongji.myblogbackend.dao;

import cn.edu.tongji.myblogbackend.entity.ChooseTagEntity;
import cn.edu.tongji.myblogbackend.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ChooseTagDAO extends JpaRepository<ChooseTagEntity, String> {
    List<ChooseTagEntity> getByArticleId(String articleId);
    ChooseTagEntity save(ChooseTagEntity chooseTagEntity);
    ChooseTagEntity getByArticleIdAndTagId(String articleId, String tagId);

    @Query(value="select t.tag_id, t.tag_name " +
            "from myblog.tag t left join myblog.choose_tag c " +
            "on t.tag_id=c.tag_id " +
            "where c.article_id =?1",nativeQuery = true)
    List<Map<String, String>> getTagList(String articleId);
}
