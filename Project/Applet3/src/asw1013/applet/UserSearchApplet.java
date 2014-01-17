package asw1013.applet;

import asw1013.HTTPClient;
import asw1013.ManageXML;
import asw1013.ui.UserListCellRenderer;
import asw1013.entity.Following;
import asw1013.entity.User;
import javax.swing.JApplet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.w3c.dom.*;
    
/**
 *
 * @author al333z
 */
public class UserSearchApplet extends JApplet {

    // http client to manage request
    HTTPClient hc = new HTTPClient();

    // xml utility
    ManageXML mngXML;

    // ui
    DefaultListModel<User> model = new DefaultListModel<User>();
    final JList jlist = new JList(model);
    final JTextField field = new JTextField();
    JButton deleteBtn;
    JButton profileBtn;

    public void init() {
        
        try {

            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {

                    initUi();
                    
                    try {
                        // represent the path portion of the URL as a file
                        URL url = getDocumentBase();
                        File file = new File(url.getPath());

                        // get the parent of the file
                        String parentPath = file.getParent();

                        // construct a new url with the parent path
                        URL parentUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), parentPath);
                        
                        hc.setBase(parentUrl);
                        
                        
                    } catch (Exception e) {

                    }

                    
                }
            });

            // set param to http client
            hc.setSessionId(getParameter("sessionId"));
            
            mngXML = new ManageXML();

            

        } catch (Exception e) {
            
        }
    }

    public void start() {
        // enable delete button is only for admin users
        if (getParameter("isAdmin") != null && getParameter("isAdmin").equals("Y")) {
            deleteBtn.setVisible(true);
        } else {
            deleteBtn.setVisible(false);
        }
    }

    private void initUi() {

        // setting the layout
        Container cp = getContentPane();
        cp.setLayout(null);

        final JButton searchBtn = new JButton("Search");

        // button to delete users
        deleteBtn = new JButton("Delete selected user");
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // delete selected user
                int indexToDelete = jlist.getSelectedIndex();
                if (indexToDelete >= 0) {
                    new UserDeleterWorker(indexToDelete).execute();
                }
            }
        });

        // button to show selected user
        profileBtn = new JButton("Show selected user");
        profileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // selected user
                int indexToShow = jlist.getSelectedIndex();
                if (indexToShow >= 0) {

                    User usr = (User) model.getElementAt(indexToShow);
                    try {
                        String path = new URL(hc.getBase()+"/jsp/profile.jsp").toString() + "?username=" + usr.username;
                        getAppletContext().showDocument(new URL(path));
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(UserSearchApplet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        // list to show results
        UserListCellRenderer renderer = new UserListCellRenderer(hc.getBase());
        jlist.setCellRenderer(renderer);
        JScrollPane scrollPane = new JScrollPane(jlist);

        // add listener to button
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserDownloadWorker dw = new UserDownloadWorker(field.getText());
                dw.execute();
            }
        });

        // add components
        field.setBounds(20, 20, 500, 40);
        cp.add(field);

        searchBtn.setBounds(540, 20, 100, 40);
        cp.add(searchBtn);

        profileBtn.setBounds(660, 20, 160, 40);
        cp.add(profileBtn);

        deleteBtn.setBounds(840, 20, 160, 40);
        cp.add(deleteBtn);

        scrollPane.setBounds(20, 80, 800, 400);
        cp.add(scrollPane);

        // add mouse listener to follow/unfollow users
        jlist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() == 2) { // User has double-clicked
                    int index = list.locationToIndex(evt.getPoint());
                    User usr = (User) model.getElementAt(index);
                    new ToggleFollowWorker(usr.username).execute();
                }
            }
        });
    }

    // get users
    private NodeList getUsers(String st) throws Exception {
        
        mngXML = new ManageXML();
        Document data = mngXML.newDocument();

        // node containing search term
        Element searchTerm = data.createElement("searchTerm");
        searchTerm.appendChild(data.createTextNode(st));

        // root node
        Element rootReq = data.createElement("userlist");

        // build the xml
        rootReq.appendChild(searchTerm);
        data.appendChild(rootReq);
        
        Document answer = hc.execute("users", data);
        NodeList userList = answer.getElementsByTagName("users");
        
        return userList;
    }

    private class UserDownloadWorker extends SwingWorker<Void, NodeList> {

        private String st;

        UserDownloadWorker(String st) {
            this.st = st;
        }

        @Override
        protected Void doInBackground() throws Exception {
            NodeList users = getUsers(st);
            publish(users);
            return null;
        }

        @Override
        protected void process(java.util.List<NodeList> chunks) {
            
            // clean the list
            model.removeAllElements();

            NodeList usersList = chunks.get(0);
            for (int i = 0; i < usersList.getLength(); i++) {
                Element userElem = (Element) usersList.item(i);

                User usr = new User();
                usr.username = userElem.getElementsByTagName("username").item(0).getTextContent();
                usr.email = userElem.getElementsByTagName("email").item(0).getTextContent();
                if (userElem.getElementsByTagName("following").getLength() == 0) {
                    usr.following = null;
                } else {
                    usr.following = new Following();
                }

                // update the list
                model.addElement(usr);
            }
        }
    }

    private class UserDeleterWorker extends SwingWorker<Void, Integer> {

        private Integer index;

        UserDeleterWorker(Integer indexToDelete) {
            this.index = indexToDelete;
        }

        @Override
        protected Void doInBackground() throws Exception {
            mngXML = new ManageXML();
            Document data = mngXML.newDocument();

            User usr = (User) model.getElementAt(index);

            // node containing username to delete
            Element searchTerm = data.createElement("username");
            searchTerm.appendChild(data.createTextNode(usr.username));

            // root node
            Element rootReq = data.createElement("delete");

            // build the xml
            rootReq.appendChild(searchTerm);
            data.appendChild(rootReq);

            Document answer = hc.execute("users", data);

            return null;
        }

        @Override
        protected void done() {
            // clean the deleted element from the model
            model.remove(index);
        }

    }

    private class ToggleFollowWorker extends SwingWorker<Void, Void> {

        private String username;

        public ToggleFollowWorker(String username) {
            this.username = username;
        }

        @Override
        protected Void doInBackground() throws Exception {

            Document data = mngXML.newDocument();
            Element usernameElem = data.createElement("username");
            usernameElem.appendChild(data.createTextNode(username));
            Element root = data.createElement("toggleFollow");
            root.appendChild(usernameElem);
            data.appendChild(root);

            Document answer = hc.execute("users", data);

            return null;
        }

        @Override
        protected void done() {
            new UserDownloadWorker(field.getText()).execute();
        }

    }

}
