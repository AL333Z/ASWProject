/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */
package asw1013;

import asw1013.entity.User;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * ListCellRenderer
 *
 * @author al333z
 */
final class UserListCellRenderer extends JPanel implements ListCellRenderer {

    private static final int LIST_CELL_ICON_SIZE = 36;

    private JLabel userLabel = null;
    private JLabel messageLabel = null;
    private JLabel timeLabel = null;
    private JLabel imageLabel = null;

    UserListCellRenderer() {

        userLabel = new JLabel(" ");
        messageLabel = new JLabel(" ");
        timeLabel = new JLabel(" ");
        imageLabel = new JLabel();

        imageLabel.setOpaque(true);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        int imageSize = LIST_CELL_ICON_SIZE + 4;

        imageLabel.setBorder(new CompoundBorder(
                new LineBorder(Color.BLACK, 1),
                new EmptyBorder(1, 1, 1, 1)));
        imageLabel.setBackground(Color.WHITE);

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        GroupLayout.SequentialGroup hg = layout.createSequentialGroup();
        layout.setHorizontalGroup(hg);
        hg.
                addComponent(imageLabel, imageSize, imageSize, imageSize).
                addGroup(layout.createParallelGroup().
                        addComponent(userLabel, 10, 10, Integer.MAX_VALUE).
                        addComponent(messageLabel, 10, 10, Integer.MAX_VALUE).
                        addComponent(timeLabel, 10, 10, Integer.MAX_VALUE)
                );

        GroupLayout.ParallelGroup vg = layout.createParallelGroup();
        layout.setVerticalGroup(vg);
        vg.
                addComponent(imageLabel, GroupLayout.Alignment.CENTER, imageSize, imageSize, imageSize).
                addGroup(layout.createSequentialGroup().
                        addComponent(userLabel).
                        addComponent(messageLabel).
                        addComponent(timeLabel));

        layout.linkSize(SwingConstants.VERTICAL, imageLabel);

        setOpaque(true);
    }

    public JComponent getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {

        //TODO get actual object
        String user = null;
        String message = null;
        String time = null;
        String img = null;
        boolean following = false;

        if (value instanceof User) {
            User usr = (User) value;
            user = usr.username;
            message = usr.email;
            following = !(usr.following==null);

            //TODO replace..
            if (index == 0) {
                img = "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash1/186727_1574644517_1361820427_q.jpg";
            } else if (index == 1) {
                img = "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-ash2/c35.34.435.435/s160x160/379902_2720385459044_1006392870_n.jpg";
            } else {
                img = "https://fbcdn-profille-ak-ash1/277109_132707500076838_551252056_q.jpg";
            }

        }

        if (user == null) {
            user = "";
        }
        if (message == null) {
            message = "";
        }
        if (time == null) {
            time = "";
        }

        userLabel.setText(user);
        messageLabel.setText(message);
        timeLabel.setText(time);
        
        try {
            imageLabel.setIcon(getImageIcon(img, 100));
        } catch (Exception ex) {
            imageLabel.setIcon(null);
        }

        if (isSelected) {
            adjustColors(list.getSelectionBackground(),
                    list.getSelectionForeground(), this, messageLabel, userLabel);
        } else {
            if(!following) {
                adjustColors(list.getBackground(),
                        list.getForeground(), this, messageLabel, userLabel);
            } else {
                adjustColors(Color.decode("#ffffcc"),
                        list.getForeground(), this, messageLabel, userLabel);
            }
        }

        return this;
    }

    private void adjustColors(Color bg, Color fg, JComponent... components) {
        for (JComponent c : components) {
            c.setForeground(fg);
            c.setBackground(bg);
        }
    }

    private Icon getImageIcon(String path, int size) throws Exception {
        if (path != null) {
            Image image = ImageCache.getInstance().getImage(
                    imageLabel, new URI(path), size, size);
            if (image != null) {
                return new ImageIcon(image);
            }
        }
        return null;
    }

}
