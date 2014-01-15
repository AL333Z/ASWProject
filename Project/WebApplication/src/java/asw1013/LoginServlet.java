package asw1013;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import asw1013.entity.User;
import asw1013.util.UserListFile;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

/**
 * Servlet that login or logout the user, based on the url pattern matched
 */
@WebServlet(urlPatterns = { "/LoginServlet", "/LogoutServlet"})
public class LoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            HttpSession session = request.getSession();
            if (request.getServletPath().equals("/LoginServlet")) {

                // setting user as logged in
                User user = new User();
                user.pass = request.getParameter("password");
                user.username = request.getParameter("username");

                UserListFile ulf = UserListFile.getInstance(getServletContext());
                user = ulf.loginUser(user);

                // setting user as logged in
                session.setAttribute("isLoggedIn", true);
                session.setAttribute("username", user.username);
                session.setAttribute("email", user.email);
                session.setAttribute("isAdmin", user.isAdmin);

                ServletContext context = getServletContext();
                RequestDispatcher dispatcher = context.getRequestDispatcher("/jsp/login.jsp");
                dispatcher.forward(request, response);

            } else {

                // setting user as logged out
                session.removeAttribute("isLoggedIn");
                session.removeAttribute("username");
                session.removeAttribute("email");

                ServletContext context = getServletContext();
                RequestDispatcher dispatcher = context.getRequestDispatcher("/jsp/logout.jsp");
                dispatcher.forward(request, response);

            }
        } catch (Exception ex) {
            throw new ServletException(getServletContext().getContextPath());
//            throw new ServletException(ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
