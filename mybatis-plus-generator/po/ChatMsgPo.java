import java.io.Serializable;
import java.util.Date;

/**
 * @author itar
 * @mail wuhandzy@gmail.com
 * @date 2019-04-01 11:05:22
 * @since jdk1.8
 */
public class ChatMsgPo implements Serializable {
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
     *
     */
    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    /**
     *
     */
    public String getSendUserId() {
        return sendUserId;
    }

    /**
     *
     */
    public void setAcceptUserId(String acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    /**
     *
     */
    public String getAcceptUserId() {
        return acceptUserId;
    }

    /**
     *
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     *
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 消息是否签收状态 1：签收 0：未签收
     */
    public void setSignFlag(Integer signFlag) {
        this.signFlag = signFlag;
    }

    /**
     * 消息是否签收状态 1：签收 0：未签收
     */
    public Integer getSignFlag() {
        return signFlag;
    }

    /**
     * 发送请求的事件
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 发送请求的事件
     */
    public Date getCreateTime() {
        return createTime;
    }
}
