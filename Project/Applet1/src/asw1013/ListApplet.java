package asw1013;

import javax.swing.JApplet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.w3c.dom.*;

/**
 *
 * @author al333z
 */
public class ListApplet extends JApplet {
    
    JLabel l = new JLabel("Dai cazzo!!!");
    JTextField t = new JTextField(10);
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

                    cp.add(t);
                    cp.add(l);
                    cp.add(b);
                    
                    b.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                ManageXML mngXML = new ManageXML();
                                Document data = mngXML.newDocument();
                                Element rootReq = data.createElement("tweetsrequest");
                                rootReq.appendChild(data.createTextNode(t.getText())); // TODO put start and stop num of tweets to get (pagination)
                                data.appendChild(rootReq);

                                String prova = "listatweet: ";
                                
                                Document answer = hc.execute("tweets", data);
                                NodeList tweetsList = answer.getElementsByTagName("tweets");
                                for(int i=0; i<tweetsList.getLength(); i++){
                                    Element tweetElem = (Element) tweetsList.item(i);
                                    // TODO add data from this tweetElem to the swing UI
                                    // example:
                                    prova = prova + tweetElem.getElementsByTagName("message").item(0).getTextContent() + ", ";
                                }

                               
                                l.setText(prova);
                                
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
