package cn.edu.tongji.myblogbackend.dao;

import cn.edu.tongji.myblogbackend.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagDao extends JpaRepository<TagEntity, String> {
    TagEntity getByTagName(String tagName);
    TagEntity save(TagEntity tagEntity);
}
