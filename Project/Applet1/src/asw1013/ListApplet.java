package asw1013;

import javax.swing.JApplet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.*;
import org.w3c.dom.*;

/**
 *
 * @author al333z
 */
public class ListApplet extends JApplet {

    static final String BASE = "http://localhost:8080/WebApplication/";
    static int i=0;
    
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
                                    HTTPClient hc = new HTTPClient();
                                    hc.setBase(new URL(BASE));

                                    Document data = mngXML.newDocument();
                                    Element root = data.createElement("op1");                            
                                    root.appendChild(data.createTextNode(t.getText()));
                                    data.appendChild(root);

                                    Document answer = hc.execute("sampleservice",data);

                                    l.setText(answer.getDocumentElement().getTagName()+(++i));
                                    t.setText("");
                            } catch (Exception ex) {System.out.println(ex);
                            } 
                        }
                    });
                    

                }
            });
        } catch (Exception e) {

        }
    }
}
