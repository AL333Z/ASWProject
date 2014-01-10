package asw1013;

import asw1013.entity.User;
import javax.swing.JApplet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.*;
import org.w3c.dom.*;

/**
 *
 * @author al333z
 */
public class UserSearchApplet extends JApplet {

    HTTPClient hc = new HTTPClient();
    ManageXML mngXML;
    
    DefaultListModel model = new DefaultListModel<Object[]>();
    final JList jlist = new JList(model);
    JButton deleteBtn;
    
    public void init() {

        try {
            hc.setSessionId(getParameter("sessionId"));
            hc.setBase(getDocumentBase());
            mngXML = new ManageXML();

            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    updateUi();
                }
            });

        } catch (Exception e) {

        }
    }

    public void start() {
        // delete button is only for admin users
        
        if (getParameter("isAdmin") != null && getParameter("isAdmin").equals("Y")) {
            deleteBtn.setVisible(true);
        } else {
            deleteBtn.setVisible(false);
        }
    }

    private void updateUi() {

        // setting the layout
        Container cp = getContentPane();
        cp.setLayout(null);

        // textfield and button, to perform search and delete users
        final JTextField field = new JTextField();

        JButton searchBtn = new JButton("Search");

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

        // list to show results
        UserListCellRenderer renderer = new UserListCellRenderer();
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

        deleteBtn.setBounds(660, 20, 180, 40);
        cp.add(deleteBtn);

        scrollPane.setBounds(20, 80, 800, 400);
        cp.add(scrollPane);

    }

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

        final LinkedList<User> res = new LinkedList<User>();

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

                //TODO perform unmarshaling in a more elegant way
                User usr = new User();
                usr.username = userElem.getElementsByTagName("username").item(0).getTextContent();
                usr.email = userElem.getElementsByTagName("email").item(0).getTextContent();

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

            final LinkedList<User> res = new LinkedList<User>();

            Document answer = hc.execute("users", data);

            publish(index);
            return null;
        }

        @Override
        protected void done() {
            // clean the deleted element from the model
            model.remove(index);
        }

    }

}
