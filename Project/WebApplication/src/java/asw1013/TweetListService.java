package asw1013;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.w3c.dom.Document;

/**
 * Service from which a client can get a list of tweets
 */

@WebServlet(urlPatterns = {"/tweets"})
public class TweetListService extends AbstractXmlServiceServlet{

    @Override
    protected void operations(Document data, HttpSession session, HttpServletResponse response, ManageXML mngXML) throws Exception {
        
    }
    
}
