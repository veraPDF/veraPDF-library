package org.verapdf.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/** Mini logo panel. Represents mini logo and link to site.
 * Created by bezrukov on 5/29/15.
 */
public class MiniLogoPanel extends JPanel {

    private final static String LABEL_TEXT = "     Please specify input PDF, Validation Profile and press \"Validate\"";

    public MiniLogoPanel(String logoPath) throws IOException {

        setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel(LABEL_TEXT);
        InputStream is = getClass().getClassLoader().getResourceAsStream(logoPath);
        final BufferedImage image = ImageIO.read(is);
        Icon icon = new Icon() {
            double scale = 0.15;
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.drawImage(image, 0,0, getIconWidth(), getIconHeight(),0,0,image.getWidth(), image.getHeight(),null);
            }

            public int getIconWidth() {
                return (int)(image.getWidth()*scale);
            }
            public int getIconHeight() {
                return (int)(image.getHeight()*scale);
            }
        };
        label.setIcon(icon);

        add(label);

    }
}
