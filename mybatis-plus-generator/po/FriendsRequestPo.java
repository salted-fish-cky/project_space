import java.io.Serializable;
import java.util.Date;

/**
 * @author itar
 * @mail wuhandzy@gmail.com
 * @date 2019-04-01 11:05:23
 * @since jdk1.8
 */
public class FriendsRequestPo implements Serializable {
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
     * 发送请求的事件
     */
    private Date requestDateTime;

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
     * 发送请求的事件
     */
    public void setRequestDateTime(Date requestDateTime) {
        this.requestDateTime = requestDateTime;
    }

    /**
     * 发送请求的事件
     */
    public Date getRequestDateTime() {
        return requestDateTime;
    }
}
