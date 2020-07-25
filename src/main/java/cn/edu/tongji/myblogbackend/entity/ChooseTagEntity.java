package cn.edu.tongji.myblogbackend.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "choose_tag", schema = "myblog", catalog = "")
public class ChooseTagEntity {
    private String choseTagId;
    private String tagId;
    private String articleId;

    @Id
    @GeneratedValue(generator = "system_uuid")
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @Column(name = "chose_tag_id")
    public String getChoseTagId() {
        return choseTagId;
    }

    public void setChoseTagId(String choseTagId) {
        this.choseTagId = choseTagId;
    }

    @Basic
    @Column(name = "tag_id")
    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    @Basic
    @Column(name = "article_id")
    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChooseTagEntity that = (ChooseTagEntity) o;

        if (choseTagId != null ? !choseTagId.equals(that.choseTagId) : that.choseTagId != null) return false;
        if (tagId != null ? !tagId.equals(that.tagId) : that.tagId != null) return false;
        if (articleId != null ? !articleId.equals(that.articleId) : that.articleId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = choseTagId != null ? choseTagId.hashCode() : 0;
        result = 31 * result + (tagId != null ? tagId.hashCode() : 0);
        result = 31 * result + (articleId != null ? articleId.hashCode() : 0);
        return result;
    }
}
