/*
 * Copyright (C) 2006 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */
package asw1013;

import java.awt.Color;
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
final class EntryListCellRenderer extends JPanel implements ListCellRenderer {

    private static final int LIST_CELL_ICON_SIZE = 36;

    private JLabel userLabel = null;
    private JLabel messageLabel = null;
    private JLabel timeLabel = null;
    private JLabel imageLabel = null;
    private URL base = null;

    EntryListCellRenderer(URL documentBase) {
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
                        addComponent(timeLabel, 10, 10, Integer.MAX_VALUE));

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

        if (value instanceof Object[]) {
            Object values[] = (Object[]) value;
            user = (String) values[0];
            message = (String) values[1];
            time = (String) values[2];
            img = base.getPath()+"/pic?username="+user;
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

        userLabel.setText(message);
        messageLabel.setText(user);
        timeLabel.setText(time);

        try {
            String path = new URL(base, "pic").toString()+"?username="+user;
            imageLabel.setIcon(getImageIcon(path, 100));
        } catch (Exception ex) {
            imageLabel.setIcon(null);
        }

        if (isSelected) {
            adjustColors(list.getSelectionBackground(),
                    list.getSelectionForeground(), this, messageLabel, userLabel);
        } else {
            adjustColors(list.getBackground(),
                    list.getForeground(), this, messageLabel, userLabel);
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
