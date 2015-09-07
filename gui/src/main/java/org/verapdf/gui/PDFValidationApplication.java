package org.verapdf.gui;

import org.apache.log4j.Logger;
import org.verapdf.gui.tools.GUIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Main frame of the PDFA Conformance Checker
 *
 * @author Maksim Bezrukov
 */
public class PDFValidationApplication extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = -5569669411392145783L;

	private static final Logger LOGGER = Logger.getLogger(PDFValidationApplication.class);

	private AboutPanel aboutPanel;

	/**
	 * Creates the frame.
	 */
	public PDFValidationApplication() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(GUIConstants.FRAME_COORD_X, GUIConstants.FRAME_COORD_Y, GUIConstants.FRAME_WIDTH, GUIConstants.FRAME_HEIGHT);
		setResizable(false);

		setTitle(GUIConstants.TITLE);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		aboutPanel = null;
		try {
			aboutPanel = new AboutPanel();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error in reading logo image.", GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
			LOGGER.error("Exception in reading logo image: ", e);
		}

		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (aboutPanel != null) {
					aboutPanel.showDialog(PDFValidationApplication.this, "About veraPDF");
				}
			}
		});

		menuBar.add(about);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(GUIConstants.EMPTYBORDER_INSETS, GUIConstants.EMPTYBORDER_INSETS, GUIConstants.EMPTYBORDER_INSETS, GUIConstants.EMPTYBORDER_INSETS));
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		setContentPane(contentPane);

		MiniLogoPanel logoPanel = null;
		try {
			logoPanel = new MiniLogoPanel(GUIConstants.LOGO_NAME);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(PDFValidationApplication.this, "Error in creating mini logo.", GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
			LOGGER.error("Exception in creating mini logo: ", e);
		}

		contentPane.add(logoPanel);

		CheckerPanel checkerPanel = null;
		try {
			checkerPanel = new CheckerPanel();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(PDFValidationApplication.this, "Error in loading xml or html image.", GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
			LOGGER.error("Exception in loading xml or html image: ", e);
		}
		contentPane.add(checkerPanel);
	}

	/**
	 * Starting point of the gui
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(
							UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
					LOGGER.error("Exception in configuring UI manager: ", e);
				}
				try {
					PDFValidationApplication frame = new PDFValidationApplication();
					frame.setVisible(true);
				} catch (Exception e) {
					LOGGER.error("Exception: ", e);
				}
			}
		});
	}

}
