import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * @author itar
 * @mail wuhandzy@gmail.com
 * @date 2019-04-01 11:05:24
 * @since jdk1.8
 */
@Setter
@Getter
public class MyFriendsResp implements Serializable {
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
}
