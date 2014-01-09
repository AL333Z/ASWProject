package asw1013;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.w3c.dom.Document;

/**
 * An abstract servlet that takes an XML from the HTTP requests, parse it 
 * and pass the resulting Document to the abstract operations method,
 * where who use this class (by extending it) must do something
 */
public abstract class AbstractXmlServiceServlet extends HttpServlet {
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {        
        
        HttpSession session = request.getSession();
        
        InputStream is = request.getInputStream();
        response.setContentType("text/xml;charset=UTF-8");
        
        try {
            ManageXML mngXML = new ManageXML();
            Document data = mngXML.parse(is);
            is.close();

            operations(data, session, request, response, mngXML);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    protected abstract void operations(Document data, HttpSession session, 
            HttpServletRequest request, HttpServletResponse response, 
            ManageXML mngXML) throws Exception;
    
}
