package org.verapdf.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Panel with veraPDF logo
 * Created by bezrukov on 5/28/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class LogoPanel extends JPanel {

    private final BufferedImage logo;
    private int borderWidth;
    private Color background;

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int imageHeight = getHeight() - borderWidth*2;
        int imageWidth = logo.getWidth()*imageHeight/logo.getHeight();
        int imageStartY = borderWidth;
        int imageStartX = (getWidth() - imageWidth)/2;

        g.setColor(background);

        g.drawImage(logo, imageStartX, imageStartY, imageStartX + imageWidth, imageStartY + imageHeight, 0, 0, logo.getWidth(), logo.getHeight(), this);

    }

    /**
     * Creates logo panel
     * @param logoName - name of the logo image
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

        setPreferredSize(new Dimension(450, 200));
    }

}
