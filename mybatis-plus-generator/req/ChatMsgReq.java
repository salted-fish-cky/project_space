import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * @author itar
 * @mail wuhandzy@gmail.com
 * @date 2019-04-01 11:05:22
 * @since jdk1.8
 */
@Setter
@Getter
public class ChatMsgReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private String id;
    /**
     *
     */
    private String sendUserId;
    /**
     *
     */
    private String acceptUserId;
    /**
     *
     */
    private String msg;
    /**
     * 消息是否签收状态 1：签收 0：未签收
     */
    private Integer signFlag;
    /**
     * 发送请求的事件
     */
    private Date createTime;
}
