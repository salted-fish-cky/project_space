import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "ProccessStep1Servlet",urlPatterns = "/proccessStep1")
public class ProccessStep1Servlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String[] books = request.getParameterValues("book");
        HttpSession session = request.getSession();
        session.setAttribute("book",books);

        response.sendRedirect(request.getContextPath()+"/shoppingcart/step-2.jsp");

    }


}
