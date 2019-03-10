package cn.ictgu.service.model;

import cn.ictgu.constant.LoginTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户
 */
@Data
public class User implements Serializable{
    private Long id;

    // 用户唯一身份识别 ID
    private String openId;

    // 密码（暂时用不到）
    private String password;

    /**
     * 登录类型 {@link LoginTypeEnum}
     */
    private String loginType;

    // 昵称
    private String nickname;

    // 头像
    private String avatar;

    // 性别
    private String gender;

    // 其他信息
    private String meta;

    // 用户信息 MD5 值，用于校验用户信息是否休息
    private String md5;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
