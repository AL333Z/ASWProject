package asw1013.applet;

import asw1013.HTTPClient;
import asw1013.ManageXML;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PostTweetApplet extends JApplet {

    // http client
    HTTPClient hc = new HTTPClient();

    // xml util
    ManageXML mngXML;

    public void init() {
        try {
            hc.setSessionId(getParameter("sessionId"));

            if (getParameter("isMainPage") != null) {
                hc.setBase(getDocumentBase());
            } else {

                // represent the path portion of the URL as a file
                URL url = getDocumentBase();
                File file = new File(url.getPath());

                // get the parent of the file
                String parentPath = file.getParent();

                // construct a new url with the parent path
                URL parentUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), parentPath);

                hc.setBase(parentUrl);
            }

            mngXML = new ManageXML();

            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {

                    // Initialize the Swing UI
                    Container cp = getContentPane();
                    cp.setLayout(null);

                    // textfield and button, to perform search
                    final JTextArea field = new JTextArea();

                    final JButton btn = new JButton("Post message");
                    btn.setPreferredSize(new Dimension(200, 40));

                    // add components
                    field.setBounds(20, 20, 500, 60);
                    cp.add(field);

                    btn.setBounds(540, 30, 100, 40);
                    cp.add(btn);

                    btn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            btn.setEnabled(false);
                            field.setEnabled(false);
                            new PostTweetWorker(field, btn).execute();
                        }
                    });
                }
            });
        } catch (Exception e) {
        }
    }

    private class PostTweetWorker extends SwingWorker<Void, Void> {

        private final JTextArea field;
        private final JButton button;

        public PostTweetWorker(JTextArea field, JButton button) {
            super();
            this.field = field;
            this.button = button;
        }

        @Override
        protected Void doInBackground() throws Exception {
            // prepare the request xml
            Document data = mngXML.newDocument();
            Element rootReq = data.createElement("postTweet");

            Element tweetText = data.createElement("tweetText");
            tweetText.appendChild(data.createTextNode(field.getText()));
            rootReq.appendChild(tweetText);

            data.appendChild(rootReq);

            Document answer = hc.execute("tweets", data);

            return null;
        }

        @Override
        protected void done() {
            button.setEnabled(true);
            field.setEnabled(true);
            field.setText("");
        }
    }

}
