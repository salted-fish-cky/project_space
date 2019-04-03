package com.cky.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author caokeyu
 * @since 2019-04-01
 */
public class Users extends Model<Users> {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 用户名，账号，慕信号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 我的头像，如果没有默认给一张
     */
    private String faceImage;

    private String faceImageBig;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 新用户注册后默认后台生成二维码，并且上传到fastdfs
     */
    private String qrcode;

    private String cid;


    public String getId() {
        return id;
    }

    public Users setId(String id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public Users setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Users setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public Users setFaceImage(String faceImage) {
        this.faceImage = faceImage;
        return this;
    }

    public String getFaceImageBig() {
        return faceImageBig;
    }

    public Users setFaceImageBig(String faceImageBig) {
        this.faceImageBig = faceImageBig;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public Users setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getQrcode() {
        return qrcode;
    }

    public Users setQrcode(String qrcode) {
        this.qrcode = qrcode;
        return this;
    }

    public String getCid() {
        return cid;
    }

    public Users setCid(String cid) {
        this.cid = cid;
        return this;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Users{" +
        "id=" + id +
        ", username=" + username +
        ", password=" + password +
        ", faceImage=" + faceImage +
        ", faceImageBig=" + faceImageBig +
        ", nickname=" + nickname +
        ", qrcode=" + qrcode +
        ", cid=" + cid +
        "}";
    }
}
