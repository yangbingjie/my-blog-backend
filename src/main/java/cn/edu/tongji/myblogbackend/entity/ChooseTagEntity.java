package cn.edu.tongji.myblogbackend.entity;

import javax.persistence.*;

@Entity
@Table(name = "choose_tag", schema = "my-blog", catalog = "")
public class ChooseTagEntity {
    private int choseTagId;
    private int tagId;
    private int articleId;

    @Id
    @Column(name = "chose_tag_id")
    public int getChoseTagId() {
        return choseTagId;
    }

    public void setChoseTagId(int choseTagId) {
        this.choseTagId = choseTagId;
    }

    @Basic
    @Column(name = "tag_id")
    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    @Basic
    @Column(name = "article_id")
    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChooseTagEntity that = (ChooseTagEntity) o;

        if (choseTagId != that.choseTagId) return false;
        if (tagId != that.tagId) return false;
        if (articleId != that.articleId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = choseTagId;
        result = 31 * result + tagId;
        result = 31 * result + articleId;
        return result;
    }
}
