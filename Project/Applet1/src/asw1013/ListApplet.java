package asw1013;

import javax.swing.JApplet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.xml.bind.JAXBContext;
import org.w3c.dom.*;

/**
 *
 * @author al333z
 */
public class ListApplet extends JApplet {

    JLabel l = new JLabel();
    JButton b = new JButton("send req");

    HTTPClient hc = new HTTPClient();
    boolean logged = false;

    JAXBContext jc = null;

    public void init() {

        try {
            hc.setSessionId(getParameter("sessionId"));
            hc.setBase(getDocumentBase());
            
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {

                    Container cp = getContentPane();
                    cp.setLayout(new GridLayout(3, 2));

                    cp.add(l);
                    cp.add(b);
                    
                    b.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                ManageXML mngXML = new ManageXML();
                                String str = "listatweet: \r\n";

                                // sample request for all tweets
                                Document data = mngXML.newDocument();
                                Element rootReq = data.createElement("tweetsrequest");
                                rootReq.appendChild(data.createTextNode("")); // TODO put start and stop num of tweets to get (pagination)
                                data.appendChild(rootReq);

                                Document answer = hc.execute("tweets", data);
                                
                                NodeList tweetsList = answer.getElementsByTagName("tweets");
                                for (int i = 0; i < tweetsList.getLength(); i++) {
                                    Element tweetElem = (Element) tweetsList.item(i);
                                    // TODO add data from this tweetElem to the swing UI
                                    // example:
                                    str = str + tweetElem.getElementsByTagName("message").item(0).getTextContent() + "\r\n";
                                }

                                l.setText(str);

                                // sample request for all users
//                                Document data2 = mngXML.newDocument();
//                                Element rootReq2 = data2.createElement("registration");
//
//                                rootReq2.appendChild(data2.createTextNode("")); // TODO put start and stop num of users to get (pagination)
//                                data2.appendChild(rootReq2);
//
//                                str += "\r\nUsers:\r\n";
//                                answer = hc.execute("users", data2);
//
//                                str += "\r\n " + answer.toString() + "\r\n";

                                l.setText(str);

                                //t.setText("");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                l.setText(ex.getMessage());
                            }
                        }
                    });

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            l.setText(e.getMessage());
        }
    }

}
