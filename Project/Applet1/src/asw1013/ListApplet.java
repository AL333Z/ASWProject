package asw1013;

import java.awt.Container;
import java.awt.GridLayout;
import java.io.StringWriter;
import javax.swing.JApplet;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
                {"mattibal", "ohibo, devo aggiornare la lista dei tweet!!", "2 ore fa", "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash2/c35.34.435.435/s160x160/379902_2720385459044_1006392870_n.jpg"},
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
                }
            });
            
            new TweetDownloadWorker().execute();
            new CometUpdaterThread().start();

        } catch (Exception e) {

        }
    }
    
    
    private NodeList getTweets() throws Exception {

        // prepare the request xml
        Document data = mngXML.newDocument();
        Element rootReq = data.createElement("tweetsrequest");
        Element op = data.createElement("operation");
        op.appendChild(data.createTextNode("getTweets"));
        rootReq.appendChild(op);
        Element startTweetElem = data.createElement("startTweet");
        startTweetElem.appendChild(data.createTextNode(lastDownloadedTweet + ""));
        rootReq.appendChild(startTweetElem);
        data.appendChild(rootReq);
        
        showDocument(data);

        Document answer = hc.execute("tweets", data);
        
        showDocument(answer);

        NodeList tweetsList = answer.getElementsByTagName("tweets");
        return tweetsList;
    }

    
    private class CometUpdaterThread extends Thread {
        @Override
        public void run() {
            while(true){
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
                // I need to trigger a tweet refresh
                new TweetDownloadWorker().execute();
                model.addElement(testdata[1]); // stupid debugging thing :)
            }
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

            for (int i = 0; i < tweetsList.getLength(); i++) {
                Element tweetElem = (Element) tweetsList.item(i);
                
                Object[] listElem = {
                    tweetElem.getElementsByTagName("username").item(0).getTextContent(),
                    tweetElem.getElementsByTagName("message").item(0).getTextContent(),
                    "1 minuto fa",
                    ""
                };
                
                model.addElement(listElem);
            }
        }
    }
    
    
    private void showDocument(Document doc) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            final String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    Object[] boh = {"xmldebug",
                            output,
                            "adesso",
                            ""
                            };
                    model.addElement(boh);
                }
            });
        } catch (Exception e) {}
    }
}
