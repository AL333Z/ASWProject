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
    final JList jlist = new JList();

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

    private void updateUi() {

        // setting the layout
        Container cp = getContentPane();
        cp.setLayout(new GridBagLayout());

        // textfield and button, to perform search
        final JTextField field = new JTextField();

        JButton btn = new JButton("Search");
        btn.setPreferredSize(new Dimension(200, 40));

        // list to show results
        UserListCellRenderer renderer = new UserListCellRenderer();
        jlist.setCellRenderer(renderer);
        JScrollPane scrollPane = new JScrollPane(jlist);

        // add listener to button
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserDownloadWorker dw = new UserDownloadWorker(field.getText());
                dw.execute();
            }
        });

        // add components
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        cp.add(field, c);

        c.gridx = 1;
        cp.add(btn, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_END;
        cp.add(scrollPane, c);
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

        final LinkedList<User> res = new LinkedList<>();

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

            NodeList usersList = chunks.get(0);

            final LinkedList<User> res = new LinkedList<>();

            for (int i = 0; i < usersList.getLength(); i++) {
                Element userElem = (Element) usersList.item(i);

                //TODO perform unmarshaling in a more elegant way
                User usr = new User();
                usr.username = userElem.getElementsByTagName("username").item(0).getTextContent();
                usr.email = userElem.getElementsByTagName("email").item(0).getTextContent();

                res.add(usr);
            }

            // update the list
            jlist.setListData(res.toArray());
        }
    }

}
