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

/** Mini logo panel. Represents mini logo and link to site.
 * Created by bezrukov on 5/29/15.
 */
public class MiniLogoPanel extends JPanel {

    public MiniLogoPanel(String logoPath, final String url, String text) throws IOException {

        setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel urlLabel = new JLabel("<html><a href=\"" + url + "\">" + text + "</a></html>");
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
        urlLabel.setIcon(icon);


        urlLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        urlLabel.setFont(new Font(urlLabel.getFont().getName(),urlLabel.getFont().getStyle(), (int)(urlLabel.getFont().getSize()*1.5)));
        urlLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(MiniLogoPanel.this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (URISyntaxException e1) {

                }

            }
        });
        urlLabel.setHorizontalTextPosition(JLabel.RIGHT);
        Rectangle2D rec = new TextLayout(text, urlLabel.getFont(), new FontRenderContext(null, true, true)).getBounds();
        urlLabel.setSize((int) (rec.getWidth()) + 50, (int) (rec.getHeight() + 4));

        add(urlLabel);

    }
}
