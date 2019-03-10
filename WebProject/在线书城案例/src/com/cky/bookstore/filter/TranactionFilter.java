package com.cky.bookstore.filter;

import com.cky.bookstore.db.JdbcUtils;
import com.cky.bookstore.web.ConnectionContext;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebFilter(filterName = "TranactionFilter",urlPatterns = "/*")
public class TranactionFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        Connection connection = null;
        try{
            //1.获取链接
            connection = JdbcUtils.getConnection();
            //2.开启事务
            connection.setAutoCommit(false);
            //3.利用ThreadLocal把连接和当前线程绑定
            ConnectionContext.getInstance().bind(connection);
            //4.把请求转给Servlet


            chain.doFilter(req, resp);

            //5.提交事务
            connection.commit();
        }catch (Exception e){
            //6.回滚事务
            try{
                connection.rollback();
            }catch (Exception e1){
                e1.printStackTrace();
            }
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) resp;
            response.sendRedirect(request.getContextPath()+"/error-1.jsp");
        }finally {
            //7.解除绑定
            ConnectionContext.getInstance().remove();
            //8.关闭连接
            JdbcUtils.releaseConnection(connection);
        }


    }

    public void init(FilterConfig config) throws ServletException {

    }

}
