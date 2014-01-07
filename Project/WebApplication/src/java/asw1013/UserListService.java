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
            case "registration": {
                UserListFile userFile = new UserListFile();
                
                log("root: "+root.toString());
                
                NodeList ul = root.getElementsByTagName("users");
                Element userElem = (Element) ul.item(0);

                User usr = new User();
                usr.username = userElem.getElementsByTagName("username").item(0).getTextContent();
                usr.pass = userElem.getElementsByTagName("pass").item(0).getTextContent();
                usr.email = userElem.getElementsByTagName("email").item(0).getTextContent();

                try {
                    userFile.registerUser(usr);

                    UserList userList = new UserList();
                    userList.users.add(usr);

                    sendUserList(userList, response.getOutputStream(), mngXML);

                } catch (Exception e) {
                    sendUserList(new UserList(), response.getOutputStream(), mngXML);
                }

                break;
            }

            case "userlist": {
                Element recvRoot = data.getDocumentElement();
                // TODO get from XML the start and stop numbers of users to sent (pagination)

                UserListFile userFile = new UserListFile();
                UserList userList = userFile.readFile();

                // TODO filter from the userlist only the users that we want to send to the client
                sendUserList(userList, response.getOutputStream(), mngXML);

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
