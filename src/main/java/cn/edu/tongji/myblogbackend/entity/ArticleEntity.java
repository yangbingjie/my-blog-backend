package cn.edu.tongji.myblogbackend.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;

@Entity
@Table(name = "article", schema = "my-blog", catalog = "")
public class ArticleEntity {
    private int articleId;
    private int authorId;
    private String content;
    private String title;
    private byte[] isPublic;
    private int viewCount;
    private String likeCount;
    private String starCount;
    private Timestamp updateTime;
    private String preview;

    @Id
    @Column(name = "article_id")
    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    @Basic
    @Column(name = "author_id")
    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "is_public")
    public byte[] getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(byte[] isPublic) {
        this.isPublic = isPublic;
    }

    @Basic
    @Column(name = "view_count")
    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    @Basic
    @Column(name = "like_count")
    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    @Basic
    @Column(name = "star_count")
    public String getStarCount() {
        return starCount;
    }

    public void setStarCount(String starCount) {
        this.starCount = starCount;
    }

    @Basic
    @Column(name = "update_time")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "preview")
    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArticleEntity that = (ArticleEntity) o;

        if (articleId != that.articleId) return false;
        if (authorId != that.authorId) return false;
        if (viewCount != that.viewCount) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (!Arrays.equals(isPublic, that.isPublic)) return false;
        if (likeCount != null ? !likeCount.equals(that.likeCount) : that.likeCount != null) return false;
        if (starCount != null ? !starCount.equals(that.starCount) : that.starCount != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;
        if (preview != null ? !preview.equals(that.preview) : that.preview != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = articleId;
        result = 31 * result + authorId;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(isPublic);
        result = 31 * result + viewCount;
        result = 31 * result + (likeCount != null ? likeCount.hashCode() : 0);
        result = 31 * result + (starCount != null ? starCount.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (preview != null ? preview.hashCode() : 0);
        return result;
    }
}
