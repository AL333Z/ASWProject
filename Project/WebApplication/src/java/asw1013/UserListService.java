package asw1013;

import java.io.OutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Service from which a client can get a list of users
 */

@WebServlet(urlPatterns = {"/users"})
public class UserListService extends AbstractXmlServiceServlet{

    @Override
    protected void operations(Document data, HttpSession session, HttpServletResponse response, ManageXML mngXML) throws Exception {
        
        Element recvRoot = data.getDocumentElement();
        // TODO get from XML the start and stop numbers of users to sent (pagination)

        UserListFile userFile = new UserListFile();
        UserList userList = userFile.readFile();
       
        // TODO filter from the userlist only the users that we want to send to the client
        UserList userListToSend = userList; //only for example
        
        JAXBContext jc = JAXBContext.newInstance(UserList.class);
        Marshaller marsh = jc.createMarshaller();
        Document doc = mngXML.newDocument();
        marsh.marshal(userListToSend, doc);
        
        OutputStream os = response.getOutputStream();
        mngXML.transform(os, doc);
        os.close();
        
    }
    
}
