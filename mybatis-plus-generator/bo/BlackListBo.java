import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * @author itar
 * @mail wuhandzy@gmail.com
 * @date 2019-04-01 11:05:21
 * @since jdk1.8
 */
@Setter
@Getter
public class BlackListBo implements Serializable {
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
}
