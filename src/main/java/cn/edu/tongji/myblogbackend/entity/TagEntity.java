package cn.edu.tongji.myblogbackend.entity;

import javax.persistence.*;

@Entity
@Table(name = "tag", schema = "my-blog", catalog = "")
public class TagEntity {
    private int tagId;
    private String tagName;

    @Id
    @Column(name = "tag_id")
    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    @Basic
    @Column(name = "tag_name")
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagEntity tagEntity = (TagEntity) o;

        if (tagId != tagEntity.tagId) return false;
        if (tagName != null ? !tagName.equals(tagEntity.tagName) : tagEntity.tagName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tagId;
        result = 31 * result + (tagName != null ? tagName.hashCode() : 0);
        return result;
    }
}
