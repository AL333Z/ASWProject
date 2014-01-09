/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asw1013;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.xml.ws.Endpoint;

/**
 *
 * @author al333z
 */
public class ImageLoader extends SwingWorker<ImageIcon, ImageIcon> {

    private JLabel consumer;
    private URL url;
    public ImageLoader(JLabel consumer, URL url) {
        this.consumer = consumer;
        this.url = url;
    }

    @Override
    protected ImageIcon doInBackground() throws IOException {

        ImageIcon picture = new ImageIcon(url);
        return picture;

    }

    protected void done() {
        try {
            ImageIcon img = get();
            consumer.setIcon(img);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
}
