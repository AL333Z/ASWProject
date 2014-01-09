package asw1013;

import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.JApplet;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.*;
import org.w3c.dom.*;

/**
 *
 * @author al333z
 */
public class ListApplet extends JApplet {

    HTTPClient hc = new HTTPClient();
    ManageXML mngXML;
    boolean logged = false;

    int lastDownloadedTweet = 0;

    Object[][] testdata
            = {
                {"al333z", "funziona davvero", "1 ora fa", "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash1/186727_1574644517_1361820427_q.jpg"},
                {"mattibal", "pensa te quante bestemmie ci fa dire sta merda..", "2 ore fa", "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash2/c35.34.435.435/s160x160/379902_2720385459044_1006392870_n.jpg"},
                {"satana", "cloro al clero.", "2 giorni fa", ""}
            };
    
   DefaultListModel model;
    

    public void init() {
        try {
            hc.setSessionId(getParameter("sessionId"));
            hc.setBase(getDocumentBase());
            mngXML = new ManageXML();

            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    
                    // Initialize the Swing UI
                    Container cp = getContentPane();
                    cp.setLayout(new GridLayout(1, 1));

                    model = new DefaultListModel<Object[]>();
                    model.addElement(testdata[0]);
                    JList jlist = new JList(model);
                    jlist.setCellRenderer(new EntryListCellRenderer());
                    JScrollPane scrollPane = new JScrollPane(jlist);
                    cp.add(scrollPane);
                    model.addElement(testdata[1]);
                }
            });

        } catch (Exception e) {

        }
    }
    
    
    private NodeList getTweets() throws Exception {

        // prepare the request xml
        Document data = mngXML.newDocument();
        Element rootReq = data.createElement("tweetsrequest");
        Element op = data.createElement("operation");
        op.appendChild(data.createTextNode("getText"));
        rootReq.appendChild(op);
        Element startTweetElem = data.createElement("startTweet");
        startTweetElem.appendChild(data.createTextNode(lastDownloadedTweet + ""));
        rootReq.appendChild(startTweetElem);
        data.appendChild(rootReq);

        Document answer = hc.execute("tweets", data);

        NodeList tweetsList = answer.getElementsByTagName("tweets");
        return tweetsList;
    }

    private void waitForUpdate() {
        try {

            // prepare the request xml
            Document data = mngXML.newDocument();
            Element rootReq = data.createElement("tweetsrequest");
            Element op = data.createElement("operation");
            op.appendChild(data.createTextNode("waitForUpdate"));
            rootReq.appendChild(op);
            data.appendChild(rootReq);

            Document answer = hc.execute("tweets", data);

        } catch (Exception e) {

        }
    }


    private class TweetDownloadWorker extends SwingWorker<Void, NodeList> {
        
        @Override
        protected Void doInBackground() throws Exception {
            NodeList tweets = getTweets();
            publish(tweets);
            return null;
        }
        
        @Override
        protected void process(java.util.List<NodeList> chunks) {

            NodeList tweetsList = chunks.get(0);

            // TODO Add tweets to the UI
            for (int i = 0; i < tweetsList.getLength(); i++) {
                Element tweetElem = (Element) tweetsList.item(i);
                // TODO add data from this tweetElem to the swing UI
                // example:
                tweetElem.getElementsByTagName("message").item(0).getTextContent();
            }
        }
    }
}
