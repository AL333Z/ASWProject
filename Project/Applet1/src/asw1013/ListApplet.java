package asw1013;

import javax.swing.JApplet;
import java.awt.*;
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

    public void init() {

        try {
            hc.setSessionId(getParameter("sessionId"));
            hc.setBase(getDocumentBase());
            mngXML = new ManageXML();
            
            Container cp = getContentPane();
            cp.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
            
            
            new SwingWorker<Void, NodeList>(){
                @Override
                protected Void doInBackground() throws Exception {
                    while(true){
                        NodeList tweets = getTweets();
                        publish(tweets);
                        waitForUpdate();
                    }
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
                
            }.execute();
            
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
        startTweetElem.appendChild(data.createTextNode(lastDownloadedTweet+""));
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

}
