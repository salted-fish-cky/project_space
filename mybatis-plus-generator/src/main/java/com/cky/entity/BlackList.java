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
public class BlackList extends Model<BlackList> {

    private static final long serialVersionUID = 1L;

    private String id;

    private String userId;

    private LocalDateTime createTime;


    public String getId() {
        return id;
    }

    public BlackList setId(String id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public BlackList setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public BlackList setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BlackList{" +
        "id=" + id +
        ", userId=" + userId +
        ", createTime=" + createTime +
        "}";
    }
}
