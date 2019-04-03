import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * @author itar
 * @mail wuhandzy@gmail.com
 * @date 2019-04-01 11:05:25
 * @since jdk1.8
 */
@Setter
@Getter
public class UsersReq implements Serializable {
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
}
