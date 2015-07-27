package org.verapdf.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.verapdf.gui.tools.GUIConstants;

/**
 * Panel with partners logo
 *
 * @author Maksim Bezrukov
 */
public class PartnersPanel extends JPanel {

    /**
     * ID for serialisation
     */
    private static final long serialVersionUID = -5926089530817358566L;
    private final transient BufferedImage partnersLogo;
    private Color background;
    private JLabel consortium;
    private JLabel version;

    /**
     * Paints the component
     *
     * @param g - graphics for painting
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int imageHeight = (int) (partnersLogo.getHeight() * GUIConstants.SCALE);
        int imageWidth = (int) (partnersLogo.getWidth() * GUIConstants.SCALE);
        int imageStartY = GUIConstants.BORDER_WIDTH * 2 + consortium.getHeight();
        int imageStartX = (getWidth() - imageWidth) / 2;

        g.setColor(background);

        consortium.setLocation((getWidth() - consortium.getWidth()) / 2, GUIConstants.BORDER_WIDTH);

        g.drawImage(partnersLogo, imageStartX, imageStartY, imageStartX + imageWidth, imageStartY + imageHeight, 0, 0, partnersLogo.getWidth(), partnersLogo.getHeight(), this);

        version.setLocation((getWidth() - version.getWidth()) / 2, getHeight() - version.getHeight() - GUIConstants.BORDER_WIDTH);
    }

    /**
     * Creates logo panel
     *
     * @param logoName        - name of the partners logo image
     * @param backgroundColor - background color
     * @throws IOException - throws when there is a problem with reading image from the input stream
     */
    public PartnersPanel(String logoName, Color backgroundColor) throws IOException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(logoName)) {
            partnersLogo = ImageIO.read(is);
        }
        this.background = backgroundColor;
        this.setLayout(null);

        consortium = new JLabel(GUIConstants.CONSORTIUM_TEXT);

        consortium.setHorizontalTextPosition(JLabel.CENTER);
        consortium.setFont(new Font(consortium.getFont().getName(), consortium.getFont().getStyle(), (int) (consortium.getFont().getSize() * GUIConstants.CONSORTIUM_FONT_SCALE)));
        Rectangle2D rec = new TextLayout(GUIConstants.CONSORTIUM_TEXT, consortium.getFont(), new FontRenderContext(null, true, true)).getBounds();
        consortium.setSize((int) (rec.getWidth()) + GUIConstants.BORDER_WIDTH * 2, (int) (rec.getHeight() + GUIConstants.BORDER_WIDTH));

        add(consortium);

        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream(GUIConstants.PROPERTIES_NAME));

        String versionText = "Version: " + properties.getProperty("application.version");

        version = new JLabel(versionText);

        version.setHorizontalTextPosition(JLabel.CENTER);
        Rectangle2D recVer = new TextLayout(versionText, version.getFont(), new FontRenderContext(null, true, true)).getBounds();
        version.setSize((int) (recVer.getWidth() + GUIConstants.BORDER_WIDTH * 2), (int) (recVer.getHeight() + GUIConstants.BORDER_WIDTH));
        add(version);


        setBackground(backgroundColor);

        int height = (int) (partnersLogo.getHeight() * GUIConstants.SCALE + consortium.getHeight() * 2 + version.getHeight() * 2);
        setPreferredSize(new Dimension(GUIConstants.PREFERRED_WIDTH, height + GUIConstants.BORDER_WIDTH * 2));
    }

}
