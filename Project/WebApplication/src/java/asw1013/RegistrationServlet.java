package asw1013;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import asw1013.entity.User;
import asw1013.util.UserListFile;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

/**
 *
 * @author al333z
 */
@WebServlet(urlPatterns = {"/RegistrationServlet"})
@MultipartConfig(location = "/tmp",
        fileSizeThreshold = 32,
        maxFileSize = 1024 * 1024,
        maxRequestSize = 1024 * 1024 * 5)
public class RegistrationServlet extends HttpServlet {

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
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            Part part = request.getPart("file");

            User user = new User();
            user.email = request.getParameter("emailsignup");
            user.pass = request.getParameter("passwordsignup");
            user.username = request.getParameter("usernamesignup");

            UserListFile ulf = UserListFile.getInstance(getServletContext());
            ulf.registerUser(user);

            // setting user as logged in
            HttpSession session = request.getSession();
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("username", user.username);
            session.setAttribute("email", user.email);
            session.setAttribute("isAdmin", new Boolean(false));

            // save the pic with username
            part.write(user.username);
            File oldPic = new File("/tmp/" + user.username);
            String picsDirPath = getServletContext().getRealPath("/WEB-INF/profilepics");
            //File picsDir = new File(picsDirPath);
            //picsDir.mkdirs();
            File newPic = new File(picsDirPath + "/" + user.username);
            //oldPic.renameTo(newPic);
            InputStream instr = new FileInputStream(oldPic);
            OutputStream outstr = new FileOutputStream(newPic);

            byte[] buf = new byte[1024];
            int len;
            while ((len = instr.read(buf)) > 0) {
                outstr.write(buf, 0, len);
            }
            instr.close();
            outstr.close();

            ServletContext context = getServletContext();
            RequestDispatcher dispatcher = context.getRequestDispatcher("/jsp/registration.jsp");
            dispatcher.forward(request, response);

        } catch (Exception ex) {
            Logger.getLogger(RegistrationServlet.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
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
