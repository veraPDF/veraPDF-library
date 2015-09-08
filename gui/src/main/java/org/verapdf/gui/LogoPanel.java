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

	/**
	 * ID for serialisation
	 */
	private static final long serialVersionUID = -3623071197419943686L;

	private final transient BufferedImage logo;
	private int borderWidth;
	private Color background;

	/**
	 * Creates logo panel
	 *
	 * @param logoName        name of the logo image
	 * @param backgroundColor background colour
	 * @param borderWidth     width of the panel border
	 * @throws IOException throws when there is a problem with reading image from the
	 *                     input stream
	 */
	public LogoPanel(String logoName, Color backgroundColor, int borderWidth) throws IOException {
		this.borderWidth = borderWidth;
		try (InputStream is = getClass().getClassLoader().getResourceAsStream(logoName)) {
			logo = ImageIO.read(is);
		}
		this.background = backgroundColor;
		this.setLayout(null);

		setBackground(backgroundColor);

		setPreferredSize(new Dimension(GUIConstants.LOGOPANEL_PREFERRED_SIZE_WIDTH, GUIConstants.LOGOPANEL_PREFERRED_SIZE_HEIGHT));
	}

	/**
	 * Paints the component
	 *
	 * @param g graphics for painting
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		int imageHeight = getHeight() - borderWidth * 2;
		int imageWidth = logo.getWidth() * imageHeight / logo.getHeight();
		int imageStartX = (getWidth() - imageWidth) / 2;

		g.setColor(background);

		g.drawImage(logo, imageStartX, borderWidth, imageStartX + imageWidth, borderWidth + imageHeight, 0, 0, logo.getWidth(), logo.getHeight(), this);

	}

}
