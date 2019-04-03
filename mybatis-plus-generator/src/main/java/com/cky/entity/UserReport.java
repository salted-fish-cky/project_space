package com.cky.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author caokeyu
 * @since 2019-04-01
 */
public class UserReport extends Model<UserReport> {

    private static final long serialVersionUID = 1L;

    private String id;

    private String dealUserId;

    private String content;

    private String userId;

    private LocalDateTime createTime;


    public String getId() {
        return id;
    }

    public UserReport setId(String id) {
        this.id = id;
        return this;
    }

    public String getDealUserId() {
        return dealUserId;
    }

    public UserReport setDealUserId(String dealUserId) {
        this.dealUserId = dealUserId;
        return this;
    }

    public String getContent() {
        return content;
    }

    public UserReport setContent(String content) {
        this.content = content;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public UserReport setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public UserReport setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UserReport{" +
        "id=" + id +
        ", dealUserId=" + dealUserId +
        ", content=" + content +
        ", userId=" + userId +
        ", createTime=" + createTime +
        "}";
    }
}
