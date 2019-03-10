import com.cky.demo.db.JdbcUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcUtilsTest {
    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = JdbcUtils.getConnection();
        System.out.println(connection);
    }

}
