package cn.edu.tongji.myblogbackend.entity;

import javax.persistence.*;

@Entity
@Table(name = "favorite", schema = "my-blog", catalog = "")
public class FavoriteEntity {
    private int favoriteId;
    private String userId;
    private String articleId;

    @Id
    @Column(name = "favorite_id")
    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    @Basic
    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

        FavoriteEntity that = (FavoriteEntity) o;

        if (favoriteId != that.favoriteId) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (articleId != null ? !articleId.equals(that.articleId) : that.articleId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = favoriteId;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (articleId != null ? articleId.hashCode() : 0);
        return result;
    }
}
