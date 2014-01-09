package asw1013;

import asw1013.entity.User;
import asw1013.util.UserListFile;
import asw1013.entity.UserList;
import java.io.OutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Service from which a client can get a list of users
 */
@WebServlet(urlPatterns = {"/users"})
public class UserListService extends AbstractXmlServiceServlet {

    @Override
    protected void operations(Document data, HttpSession session,
            HttpServletRequest request, HttpServletResponse response,
            ManageXML mngXML) throws Exception {

        // name of operation is message root
        Element root = data.getDocumentElement();
        String operation = root.getTagName();
        Document answer = null;

        switch (operation) {

            case "userlist": {
                Element recvRoot = data.getDocumentElement();
                String searchTerm = recvRoot.getElementsByTagName("searchTerm").item(0).getTextContent();

                // TODO get from XML the start and stop numbers of users to sent (pagination)
                UserListFile userFile = new UserListFile();
                UserList userList = null;

                if (searchTerm == null || searchTerm.isEmpty()) {
                    userList = userFile.readFile();
                } else {
                    userList = userFile.searchUsers(searchTerm);
                }

                sendUserList(userList, response.getOutputStream(), mngXML);

                break;
            }
            case "delete": {
                Element recvRoot = data.getDocumentElement();
                String usernameToDelete = recvRoot.getElementsByTagName("username").item(0).getTextContent();
                
                UserListFile userFile = new UserListFile();
                userFile.deleteUser(usernameToDelete);
                
                break;
            }
        }

    }

    private void sendUserList(UserList ul, OutputStream os, ManageXML mngXML) throws Exception {

        JAXBContext jc = JAXBContext.newInstance(UserList.class);
        Marshaller marsh = jc.createMarshaller();
        Document doc = mngXML.newDocument();
        marsh.marshal(ul, doc);

        mngXML.transform(os, doc);
        os.close();
    }
    
}
