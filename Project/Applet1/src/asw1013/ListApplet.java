package asw1013;

import java.awt.Container;
import java.awt.GridLayout;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
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
    ManageXML mngXML = null;
    
    boolean logged = false;
    String username = null;
    
    DefaultListModel model;
    
    public void init() {
        
        username = getParameter("username");
        
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
                    JList jlist = new JList(model);
                    jlist.setCellRenderer(new EntryListCellRenderer(getDocumentBase()));
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
        // TODO implement pagination
        //Element startTweetElem = data.createElement("startTweet");
        //startTweetElem.appendChild(data.createTextNode(lastDownloadedTweet + ""));
        //rootReq.appendChild(startTweetElem);
        data.appendChild(rootReq);
        
        //showDocument(data);

        Document answer = hc.execute("tweets", data);
        
        //showDocument(answer);

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
            
            model.removeAllElements();

            for (int i = tweetsList.getLength()-1; i>=0; i--) {
                Element tweetElem = (Element) tweetsList.item(i);
                
                Object[] listElem = {
                    tweetElem.getElementsByTagName("username").item(0).getTextContent(),
                    tweetElem.getElementsByTagName("message").item(0).getTextContent(),
                    buildDateString(tweetElem.getElementsByTagName("date").item(0).getTextContent()),
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
    
    private static String buildDateString(String xmlDate){
        Calendar tweetCal = DatatypeConverter.parseDateTime(xmlDate);

        long diff = (new Date().getTime() - tweetCal.getTimeInMillis()) / 1000;
        double dayDiff = Math.floor(diff / 86400);

        if (diff < 0) {
            return "in the future?";
        } else if (diff < 60) {
            return "seconds ago";
        } else if (diff < 120) {
            return "1 minute ago";
        } else if (diff < 3600) {
            return diff / 60 + " minutes ago";
        } else if (diff < 7200) {
            return "one hour ago";
        } else if (diff < 86400) {
            return diff / 3600 + " hours ago";
        } else if (dayDiff == 1) {
            return "yesterday";
        } else if (dayDiff < 7) {
            return dayDiff + " days ago";
        } else {
            return Math.ceil(dayDiff / 7) + " weeks ago";
        }
    }
}
