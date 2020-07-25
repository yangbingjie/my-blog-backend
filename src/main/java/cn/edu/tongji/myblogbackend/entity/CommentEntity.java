package cn.edu.tongji.myblogbackend.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "comment", schema = "myblog", catalog = "")
public class CommentEntity {
    private String commentId;
    private String articleId;
    private String referenceCommentId;
    private int replyCount;
    private String content;

    @Id
    @GeneratedValue(generator = "system_uuid")
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @Column(name = "comment_id")
    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    @Basic
    @Column(name = "article_id")
    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    @Basic
    @Column(name = "reference_comment_id")
    public String getReferenceCommentId() {
        return referenceCommentId;
    }

    public void setReferenceCommentId(String referenceCommentId) {
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

        if (replyCount != that.replyCount) return false;
        if (commentId != null ? !commentId.equals(that.commentId) : that.commentId != null) return false;
        if (articleId != null ? !articleId.equals(that.articleId) : that.articleId != null) return false;
        if (referenceCommentId != null ? !referenceCommentId.equals(that.referenceCommentId) : that.referenceCommentId != null)
            return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = commentId != null ? commentId.hashCode() : 0;
        result = 31 * result + (articleId != null ? articleId.hashCode() : 0);
        result = 31 * result + (referenceCommentId != null ? referenceCommentId.hashCode() : 0);
        result = 31 * result + replyCount;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
