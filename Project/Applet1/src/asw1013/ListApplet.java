package asw1013;

import javax.swing.JApplet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.TransformerConfigurationException;
import org.w3c.dom.*;

/**
 *
 * @author al333z
 */
public class ListApplet extends JApplet {
    
    JLabel l = new JLabel("Dai cazzo");
    JTextField t = new JTextField(10);
    JButton b = new JButton("send req");

    HTTPClient hc = new HTTPClient();
    boolean logged = false;

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
                                Element root = data.createElement("tweetsrequest");
                                root.appendChild(data.createTextNode(t.getText())); // TODO put start and stop num of tweets to get (pagination)
                                data.appendChild(root);

                                Document answer = hc.execute("tweets", data);

                                JAXBContext jc = JAXBContext.newInstance(TweetList.class);
                                Unmarshaller u = jc.createUnmarshaller();
                                TweetList tweetList = (TweetList) u.unmarshal(answer);
                                
                                for(Tweet tweet : tweetList.tweets){
                                    // TODO add a tweet in the swing UI
                                    l.setText(tweet.message);
                                }

                                //t.setText("");
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            } 
                        }
                    });
                    

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
