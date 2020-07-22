package cn.edu.tongji.myblogbackend.entity;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "message", schema = "my-blog", catalog = "")
public class MessageEntity {
    private int messageId;
    private String userId;
    private String content;
    private byte[] status;

    @Id
    @Column(name = "message_id")
    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
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
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "status")
    public byte[] getStatus() {
        return status;
    }

    public void setStatus(byte[] status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageEntity that = (MessageEntity) o;

        if (messageId != that.messageId) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (!Arrays.equals(status, that.status)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = messageId;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(status);
        return result;
    }
}
