package mapper;

import com.cky.mybatis.beans.Orders;
import com.cky.mybatis.beans.User;

import java.util.List;

public interface OderMapper {


//一对一关联查询,一订单为中心关联客户
    public List<Orders> selectOrders();

    //一对多关联
    public List<User> selectUserList();

}
