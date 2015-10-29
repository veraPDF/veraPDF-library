package org.verapdf.gui;

import org.apache.log4j.Logger;
import org.verapdf.gui.config.Config;
import org.verapdf.gui.config.ConfigPropertiesSerializator;
import org.verapdf.gui.tools.GUIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Main frame of the PDFA Conformance Checker
 *
 * @author Maksim Bezrukov
 */
public class PDFValidationApplication extends JFrame {

	private static final long serialVersionUID = -5569669411392145783L;

	private static final Logger LOGGER = Logger.getLogger(PDFValidationApplication.class);

	private AboutPanel aboutPanel;
	private transient Config config;
	private SettingsPanel settingsPanel;
	private CheckerPanel checkerPanel;
	private boolean isSerializedConfig;
	private transient Path configPath;

	private PDFValidationApplication() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(GUIConstants.FRAME_COORD_X, GUIConstants.FRAME_COORD_Y, GUIConstants.FRAME_WIDTH, GUIConstants.FRAME_HEIGHT);
		setResizable(false);

		setTitle(GUIConstants.TITLE);

		String appHome = System.getProperty("app.home");
		if (appHome != null) {
			File user = new File(appHome);
			File f = new File(user, "config");
			if (!f.exists() && !f.mkdir()) {
				this.isSerializedConfig = false;
				this.config = Config.Builder.buildDefaultConfig();
			} else {
				File configFile = new File(f, "config.properties");
				this.isSerializedConfig = true;
				this.configPath = configFile.toPath();
				if (configFile.exists()) {
					try {
						this.config = ConfigPropertiesSerializator.loadConfig(configFile.toPath());
						ConfigPropertiesSerializator.saveConfig(this.config, configFile.toPath());
					} catch (IOException e) {
						LOGGER.error("Can not read config file", e);
						this.config = Config.Builder.buildDefaultConfig();
					}

				} else {
					this.config = Config.Builder.buildDefaultConfig();
				}
			}
		}

		JMenuBar menuBar = new JMenuBar();
		menuBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setJMenuBar(menuBar);

		aboutPanel = null;
		try {
			aboutPanel = new AboutPanel();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error in reading logo image.", GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
			LOGGER.error("Exception in reading logo image", e);
		}

		try {
			settingsPanel = new SettingsPanel();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(PDFValidationApplication.this, "Error initialising settings panel.", GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
			LOGGER.error("Exception in initialising settings panel", e);
		}

		final JMenuItem sett = new JMenuItem("Settings");
		sett.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (settingsPanel != null && settingsPanel.showDialog(PDFValidationApplication.this, "Settings", config)) {
					Config.Builder builder = new Config.Builder();
					builder.showPassedRules(settingsPanel.isDispPassedRules());
					builder.maxNumberOfFailedChecks(settingsPanel.getFailedChecksNumber());
					builder.maxNumberOfDisplayedFailedChecks(settingsPanel.getFailedChecksDisplayNumber());
					builder.fixMetadataPathFolder(settingsPanel.getFixMetadataDirectory());
					builder.metadataFixerPrefix(settingsPanel.getFixMetadataPrefix());
					PDFValidationApplication.this.config = builder.build();
					PDFValidationApplication.this.checkerPanel.setConfig(PDFValidationApplication.this.config);
					if (PDFValidationApplication.this.isSerializedConfig) {
						try {
							ConfigPropertiesSerializator.saveConfig(PDFValidationApplication.this.config, PDFValidationApplication.this.configPath);
						} catch (IOException e1) {
							LOGGER.error("Can not save config", e1);
						}
					}
				}
			}
		});

		menuBar.add(sett);

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
			LOGGER.error("Exception in creating mini logo", e);
		}

		contentPane.add(logoPanel);

		checkerPanel = null;
		try {
			checkerPanel = new CheckerPanel(config);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(PDFValidationApplication.this, "Error in loading xml or html image.", GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
			LOGGER.error("Exception in loading xml or html image", e);
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
					LOGGER.error("Exception in configuring UI manager", e);
				}
				try {
					PDFValidationApplication frame = new PDFValidationApplication();
					frame.setVisible(true);
				} catch (Exception e) {
					LOGGER.error("Exception", e);
				}
			}
		});
	}

}
