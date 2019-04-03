import java.io.Serializable;

/**
 * @author itar
 * @mail wuhandzy@gmail.com
 * @date 2019-04-01 11:05:25
 * @since jdk1.8
 */
public class UsersPo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
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
    /**
     *
     */
    private String faceImageBig;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 新用户注册后默认后台生成二维码，并且上传到fastdfs
     */
    private String qrcode;
    /**
     *
     */
    private String cid;

    /**
     *
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     */
    public String getId() {
        return id;
    }

    /**
     * 用户名，账号，慕信号
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 用户名，账号，慕信号
     */
    public String getUsername() {
        return username;
    }

    /**
     * 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 我的头像，如果没有默认给一张
     */
    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    /**
     * 我的头像，如果没有默认给一张
     */
    public String getFaceImage() {
        return faceImage;
    }

    /**
     *
     */
    public void setFaceImageBig(String faceImageBig) {
        this.faceImageBig = faceImageBig;
    }

    /**
     *
     */
    public String getFaceImageBig() {
        return faceImageBig;
    }

    /**
     * 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 新用户注册后默认后台生成二维码，并且上传到fastdfs
     */
    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    /**
     * 新用户注册后默认后台生成二维码，并且上传到fastdfs
     */
    public String getQrcode() {
        return qrcode;
    }

    /**
     *
     */
    public void setCid(String cid) {
        this.cid = cid;
    }

    /**
     *
     */
    public String getCid() {
        return cid;
    }
}
