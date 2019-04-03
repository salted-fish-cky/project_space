import java.io.Serializable;
import java.util.Date;

/**
 * @author itar
 * @mail wuhandzy@gmail.com
 * @date 2019-04-01 11:05:21
 * @since jdk1.8
 */
public class BlackListPo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private String id;
    /**
     *
     */
    private String userId;
    /**
     *
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
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     *
     */
    public Date getCreateTime() {
        return createTime;
    }
}
