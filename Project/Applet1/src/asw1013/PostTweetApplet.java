package asw1013;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class PostTweetApplet extends JApplet {

    HTTPClient hc = new HTTPClient();
    ManageXML mngXML;
    
    
    public void init() {
        try{
            hc.setSessionId(getParameter("sessionId"));
            hc.setBase(getDocumentBase());
            mngXML = new ManageXML();
            
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    
                    // Initialize the Swing UI
                    Container cp = getContentPane();
                    cp.setLayout(new GridBagLayout());

                    // textfield and button, to perform search
                    final JTextField field = new JTextField();

                    final JButton btn = new JButton("Post message");
                    btn.setPreferredSize(new Dimension(200, 40));

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
                    
                    btn.addActionListener(new ActionListener(){
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
        
        private final JTextField field;
        private final JButton button;
        
        public PostTweetWorker(JTextField field, JButton button) {
            super();
            this.field = field;
            this.button = button;
        }
        
        @Override
        protected Void doInBackground() throws Exception {
            // prepare the request xml
            Document data = mngXML.newDocument();
            Element rootReq = data.createElement("tweetsrequest");
            Element op = data.createElement("operation");
            op.appendChild(data.createTextNode("postTweet"));
            rootReq.appendChild(op);
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
        }  
    }

}
