/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */
package asw1013;

import asw1013.entity.User;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URI;
import java.net.URL;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
    private URL base = null;
    
    UserListCellRenderer(URL documentBase) {
        base = documentBase;
        
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
        boolean following = false;

        if (value instanceof User) {
            User usr = (User) value;
            user = usr.username;
            message = usr.email;
            following = !(usr.following==null);

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
            String path = new URL(base, "pic").toString()+"?username="+user;
            imageLabel.setIcon(getImageIcon(path, imageLabel.getSize()));
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

    private Icon getImageIcon(String path, Dimension size) throws Exception {
        if (path != null) {
            Image image = ImageCache.getInstance().getImage(
                    imageLabel, new URI(path), (int)size.getWidth(), (int)size.getHeight());
            if (image != null) {
                return new ImageIcon(image);
            }
        }
        return null;
    }

}
