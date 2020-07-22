package cn.edu.tongji.myblogbackend.entity;

import javax.persistence.*;

@Entity
@Table(name = "comment", schema = "my-blog", catalog = "")
public class CommentEntity {
    private int commentId;
    private int articleId;
    private Integer referenceCommentId;
    private int replyCount;
    private String content;

    @Id
    @Column(name = "comment_id")
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    @Basic
    @Column(name = "article_id")
    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    @Basic
    @Column(name = "reference_comment_id")
    public Integer getReferenceCommentId() {
        return referenceCommentId;
    }

    public void setReferenceCommentId(Integer referenceCommentId) {
        this.referenceCommentId = referenceCommentId;
    }

    @Basic
    @Column(name = "reply_count")
    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommentEntity that = (CommentEntity) o;

        if (commentId != that.commentId) return false;
        if (articleId != that.articleId) return false;
        if (replyCount != that.replyCount) return false;
        if (referenceCommentId != null ? !referenceCommentId.equals(that.referenceCommentId) : that.referenceCommentId != null)
            return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = commentId;
        result = 31 * result + articleId;
        result = 31 * result + (referenceCommentId != null ? referenceCommentId.hashCode() : 0);
        result = 31 * result + replyCount;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
