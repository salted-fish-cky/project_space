import com.cky.maven.ssh.dao.UserDao;
import com.cky.maven.ssh.entities.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserDaoImplTest {

    @Test
    public void test(){
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        UserDao userDao = (UserDao) context.getBean("userDao");
        User user = userDao.findById(46);
        System.out.println("***********"+user.getNickname());
    }
}
