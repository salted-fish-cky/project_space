import com.cky.demo.bean.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ProccessStep2Servlet",urlPatterns = "/proccessStep2")
public class ProccessStep2Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String cardType = request.getParameter("cardType");
        String card = request.getParameter("card");

        Customer customer = new Customer(name, address, cardType, card);

        HttpSession session = request.getSession();
        session.setAttribute("customer",customer);

        response.sendRedirect(request.getContextPath()+"/shoppingcart/confirm.jsp");

    }


}
