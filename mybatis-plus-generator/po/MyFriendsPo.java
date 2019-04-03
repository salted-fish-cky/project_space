import java.io.Serializable;

/**
 * @author itar
 * @mail wuhandzy@gmail.com
 * @date 2019-04-01 11:05:24
 * @since jdk1.8
 */
public class MyFriendsPo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private String id;
    /**
     * 用户id
     */
    private String myUserId;
    /**
     * 用户的好友id
     */
    private String myFriendUserId;

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
     * 用户id
     */
    public void setMyUserId(String myUserId) {
        this.myUserId = myUserId;
    }

    /**
     * 用户id
     */
    public String getMyUserId() {
        return myUserId;
    }

    /**
     * 用户的好友id
     */
    public void setMyFriendUserId(String myFriendUserId) {
        this.myFriendUserId = myFriendUserId;
    }

    /**
     * 用户的好友id
     */
    public String getMyFriendUserId() {
        return myFriendUserId;
    }
}
