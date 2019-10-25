import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Date;

public class Test extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        try {
            out.println("<h1>" + "Dummy Servlet" + "</h1>");
            out.println(new Date());
        } finally {
            out.close();
        }
    }
}
