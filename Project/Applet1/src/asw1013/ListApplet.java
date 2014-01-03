package asw1013;

import javax.swing.JApplet;
import java.awt.*;
import javax.swing.JFrame;
import java.awt.event.*;
import javax.swing.*;
import org.w3c.dom.*;

/**
 *
 * @author al333z
 */
public class ListApplet extends JApplet {

    JLabel l = new JLabel("Dai cazzo");
    JTextField t = new JTextField(10);

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

                    t.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            
                        }
                    });
                }
            });
        } catch (Exception e) {

        }
    }
}
