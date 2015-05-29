package org.verapdf.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Panel with partners logo
 * Created by bezrukov on 5/28/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class PartnersPanel extends JPanel {

    private final BufferedImage partnersLogo;
    private final int BORDER_WIDTH = 10;
    private Color background;
    private double SCALE = 0.5;

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int imageHeight = (int) (partnersLogo.getHeight()*SCALE);
        int imageWidth = (int) (partnersLogo.getWidth()*SCALE);
        int imageStartY = BORDER_WIDTH;
        int imageStartX = (getWidth() - imageWidth)/2;

        g.setColor(background);

        g.drawImage(partnersLogo, imageStartX, imageStartY, imageStartX + imageWidth, imageStartY + imageHeight, 0, 0, partnersLogo.getWidth(), partnersLogo.getHeight(), this);

    }

    /**
     * Creates logo panel
     * @param logoName - name of the partners logo image
     * @param backgroundColor - background color
     * @throws IOException - throws when there is a problem with reading image from the input stream
     */
    public PartnersPanel(String logoName, Color backgroundColor) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(logoName);
        partnersLogo = ImageIO.read(is);
        this.background = backgroundColor;
        this.setLayout(null);

        setBackground(backgroundColor);

        int height = (int) (partnersLogo.getHeight()*SCALE);
        setPreferredSize(new Dimension(450, height + BORDER_WIDTH*2));
    }

}
