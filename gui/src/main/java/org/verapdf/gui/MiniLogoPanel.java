package org.verapdf.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.verapdf.gui.tools.GUIConstants;

/**
 * Mini logo panel. Represents mini logo and link to site.
 *
 * @author Maksim Bezrukov
 */
public class MiniLogoPanel extends JPanel {

    /**
     * ID for serialisation
     */
    private static final long serialVersionUID = -199053265127458738L;

    /**
     * Creates mini logo panel
     *
     * @param logoPath - path to the logo image
     * @throws IOException - throws when there is a problem with reading image from the input stream
     */
    public MiniLogoPanel(String logoPath) throws IOException {

        setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel(GUIConstants.LABEL_TEXT);
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(logoPath)) {
            final BufferedImage image = ImageIO.read(is);
            Icon icon = new Icon() {

                private static final double SCALE = 0.15;

                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    g.drawImage(image, 0, 0, getIconWidth(), getIconHeight(), 0, 0, image.getWidth(), image.getHeight(), null);
                }

                @Override
                public int getIconWidth() {
                    return (int) (image.getWidth() * SCALE);
                }

                @Override
                public int getIconHeight() {
                    return (int) (image.getHeight() * SCALE);
                }
            };
            label.setIcon(icon);
        }

        add(label);

    }
}
