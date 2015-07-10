package org.verapdf.gui;

import org.verapdf.gui.tools.GUIConstants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Panel with veraPDF logo
 *
 * @author Maksim Bezrukov
 */
public class LogoPanel extends JPanel {

    private final BufferedImage logo;
    private int borderWidth;
    private Color background;

    /**
     * Paints the component
     *
     * @param g - graphics for painting
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int imageHeight = getHeight() - borderWidth * 2;
        int imageWidth = logo.getWidth() * imageHeight / logo.getHeight();
        int imageStartY = borderWidth;
        int imageStartX = (getWidth() - imageWidth) / 2;

        g.setColor(background);

        g.drawImage(logo, imageStartX, imageStartY, imageStartX + imageWidth, imageStartY + imageHeight, 0, 0, logo.getWidth(), logo.getHeight(), this);

    }

    /**
     * Creates logo panel
     *
     * @param logoName        - name of the logo image
     * @param backgroundColor - background color
     * @throws IOException - throws when there is a problem with reading image from the input stream
     */
    public LogoPanel(String logoName, Color backgroundColor, int borderWidth) throws IOException {
        this.borderWidth = borderWidth;
        InputStream is = getClass().getClassLoader().getResourceAsStream(logoName);
        logo = ImageIO.read(is);
        this.background = backgroundColor;
        this.setLayout(null);

        setBackground(backgroundColor);

        setPreferredSize(new Dimension(GUIConstants.LOGOPANEL_PREFERRED_SIZE_WIDTH, GUIConstants.LOGOPANEL_PREFERRED_SIZE_HEIGHT));
    }

}
