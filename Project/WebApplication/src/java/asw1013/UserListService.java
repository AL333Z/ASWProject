package asw1013;

import asw1013.entity.Following;
import asw1013.entity.User;
import asw1013.util.UserListFile;
import asw1013.entity.UserList;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
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

                UserListFile userFile = UserListFile.getInstance();
                UserList userList = null;

                if (searchTerm == null || searchTerm.isEmpty()) {
                    userList = userFile.readFile();
                } else {
                    userList = userFile.searchUsers(searchTerm);
                }
                
                // replace Following from Users with an empty list if I'm following this user, with null otherwise
                List<String> following = userFile.getUserByUsername((String)session.getAttribute("username")).following.usernames;
                for(User user : userList.users){
                    if(following.contains(user.username)){
                        user.following = new Following(); // I'm following this user
                    } else {
                        user.following = null; // I'm not following this user
                    }
                }
                
                sendUserList(userList, response.getOutputStream(), mngXML);

                break;
            }
            
            case "delete": {
                if(! ((boolean) session.getAttribute("isAdmin")) ){
                    return; // Hacking attempt!!
                }
                
                Element recvRoot = data.getDocumentElement();
                String usernameToDelete = recvRoot.getElementsByTagName("username").item(0).getTextContent();
                
                UserListFile userFile = UserListFile.getInstance();
                userFile.deleteUser(usernameToDelete);
                
                break;
            }
        
            case "toggleFollow": {
                Element recvRoot = data.getDocumentElement();
                String followingUsername = recvRoot.getElementsByTagName("username").item(0).getTextContent();
                UserListFile userFile = UserListFile.getInstance();
                userFile.toggleFollowing( (String)session.getAttribute("username"), followingUsername);
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
