package asw1013.applet;



import asw1013.ui.EntryListCellRenderer;
import asw1013.HTTPClient;
import asw1013.ManageXML;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import org.w3c.dom.*;

/**
 *
 * @author al333z
 */
public class TweetListApplet extends JApplet {

    // http client
    HTTPClient hc = new HTTPClient();

    // xml utility
    ManageXML mngXML = null;

    // logged user flag
    boolean logged = false;

    // username of the desired user profile
    String username = null;

    // list model
    DefaultListModel<String[]> model;

    public void init() {

        username = getParameter("username");

        try {
            hc.setSessionId(getParameter("sessionId"));

            if (getParameter("isMainPage") != null) {
                //hc.setBase(getDocumentBase());
            } else {

                // represent the path portion of the URL as a file
                URL url = getDocumentBase();
                File file = new File(url.getPath());

                // get the parent of the file
                String parentPath = file.getParent();

                // construct a new url with the parent path
                URL parentUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), parentPath);

                //hc.setBase(parentUrl);
            }
            
            hc.setBase(new URL("http://si-tomcat.csr.unibo.it:8080/~mattia.baldani"));

            mngXML = new ManageXML();

            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {

                    // Initialize the Swing UI
                    Container cp = getContentPane();
                    cp.setLayout(new GridLayout(1, 1));

                    model = new DefaultListModel<String[]>();
                    JList jlist = new JList(model);
                    jlist.setCellRenderer(new EntryListCellRenderer(hc.getBase()));
                    JScrollPane scrollPane = new JScrollPane(jlist);
                    cp.add(scrollPane);

                    // add mouse listener to delete tweets on double click
                    jlist.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent evt) {
                            JList list = (JList) evt.getSource();
                            if (evt.getClickCount() == 2) { // User has double-clicked
                                int index = list.locationToIndex(evt.getPoint());
                                String[] row = (String[]) model.getElementAt(index);
                                new TweetDeleteWorker(row[0], row[1]).execute();
                            }
                        }
                    });
                }
            });

            // start worker to get initial value and to get notified when 
            // new tweets are availlable
            new TweetDownloadWorker().execute();
            new CometUpdaterThread().start();

        } catch (Exception e) {
        }
    }

    private NodeList getTweets() throws Exception {

        // prepare the request xml
        Document data = mngXML.newDocument();
        Element rootReq = data.createElement("getTweets");

        String tweetsOfUsername = getParameter("tweetsOfUsername");
        if (tweetsOfUsername != null) {
            Element tweetsOfElem = data.createElement("tweetsOfUsername");
            tweetsOfElem.appendChild(data.createTextNode(tweetsOfUsername));
            rootReq.appendChild(tweetsOfElem);
        }

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
            while (true) {
                try {
                    // prepare the request xml
                    Document data = mngXML.newDocument();
                    Element rootReq = data.createElement("waitForUpdate");
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

            for (int i = tweetsList.getLength() - 1; i >= 0; i--) {
                Element tweetElem = (Element) tweetsList.item(i);

                String[] listElem = {
                    tweetElem.getElementsByTagName("username").item(0).getTextContent(),
                    tweetElem.getElementsByTagName("message").item(0).getTextContent(),
                    buildDateString(tweetElem.getElementsByTagName("date").item(0).getTextContent()),
                    ""
                };

                model.addElement(listElem);
            }
        }
    }

    private class TweetDeleteWorker extends SwingWorker<Void, Void> {

        private String username;
        private String message;

        public TweetDeleteWorker(String username, String message) {
            this.username = username;
            this.message = message;
        }

        @Override
        protected Void doInBackground() throws Exception {

            Document data = mngXML.newDocument();
            Element rootReq = data.createElement("deleteTweet");

            Element usernameElem = data.createElement("username");
            usernameElem.appendChild(data.createTextNode(username));
            rootReq.appendChild(usernameElem);

            Element msgElem = data.createElement("message");
            msgElem.appendChild(data.createTextNode(message));
            rootReq.appendChild(msgElem);

            data.appendChild(rootReq);

            Document answer = hc.execute("tweets", data);

            return null;
        }

        @Override
        protected void done() {
            new TweetDownloadWorker().execute();
        }

    }

    private static String buildDateString(String xmlDate) {
        Calendar tweetCal = DatatypeConverter.parseDateTime(xmlDate);

        long diff = (new Date().getTime() - tweetCal.getTimeInMillis()) / 1000;
        double dayDiff = Math.floor(diff / 86400);

        if (diff < 0) {
            return "now";
        } else if (diff < 60) {
            return "seconds ago";
        } else if (diff < 120) {
            return "1 minute ago";
        } else if (diff < 3600) {
            return ((int) (diff / 60)) + " minutes ago";
        } else if (diff < 7200) {
            return "one hour ago";
        } else if (diff < 86400) {
            return ((int) (diff / 3600)) + " hours ago";
        } else if (dayDiff == 1) {
            return "yesterday";
        } else if (dayDiff < 7) {
            return ((int) dayDiff) + " days ago";
        } else {
            return Math.ceil(dayDiff / 7) + " weeks ago";
        }
    }
}
