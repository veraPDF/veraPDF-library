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
 * Panel with veraPDF logo
 * Created by bezrukov on 5/28/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class LogoPanel extends JPanel {

    private final BufferedImage logo;
    private JLabel urlLabel;
    private final int BORDER_WIDTH = 10;
    private int borderBottomHeight;
    private Color background;

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int imageHeight = getHeight() - BORDER_WIDTH - borderBottomHeight;
        int imageWidth = logo.getWidth()*imageHeight/logo.getHeight();
        int imageStartY = BORDER_WIDTH;
        int imageStartX = (getWidth() - imageWidth)/2;

        g.setColor(background);

        urlLabel.setLocation((getWidth() - urlLabel.getWidth()) / 2, imageStartY + imageHeight + BORDER_WIDTH - 7);

        g.drawImage(logo, imageStartX, imageStartY, imageStartX + imageWidth, imageStartY + imageHeight, 0, 0, logo.getWidth(), logo.getHeight(), this);

    }

    /**
     * Creates logo panel
     * @param logoName - name of the logo image
     * @param backgroundColor - background color
     * @param url - url for the link
     * @param text - text of the link
     * @throws IOException - throws when there is a problem with reading image from the input stream
     */
    public LogoPanel(String logoName, Color backgroundColor, final String url, String text) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(logoName);
        logo = ImageIO.read(is);
        this.background = backgroundColor;
        this.setLayout(null);

        setBackground(backgroundColor);

        urlLabel = new JLabel("<html><a href=\"" + url + "\">" + text + "</a></html>");
        urlLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        urlLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(LogoPanel.this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (URISyntaxException e1) {
                    JOptionPane.showMessageDialog(LogoPanel.this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        urlLabel.setHorizontalTextPosition(JLabel.CENTER);
        Rectangle2D rec = new TextLayout(text, urlLabel.getFont(), new FontRenderContext(null, true, true)).getBounds();
        urlLabel.setSize((int) (rec.getWidth()) + 7, (int) (rec.getHeight() + 4));
        add(urlLabel);

        borderBottomHeight = (int) (rec.getHeight() + BORDER_WIDTH + 1);

        setPreferredSize(new Dimension(450, 200));
    }

}
