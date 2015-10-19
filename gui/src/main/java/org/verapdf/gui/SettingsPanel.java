package org.verapdf.gui;

import org.verapdf.gui.config.Config;
import org.verapdf.gui.tools.GUIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Settings Panel
 *
 * @author Maksim Bezrukov
 */
class SettingsPanel extends JPanel {

	private static final long serialVersionUID = -5688021756073449469L;
	private JButton okButton;
	private boolean ok;
	JDialog dialog;
	private JTextField numberOfFailed;
	private JTextField numberOfFailedDisplay;
	private JCheckBox hidePassedRules;
	private JTextField thirdPartyProfilePathField;
	private JFileChooser chooser;
	private JTextField fixMetadataPrefix;
	private JTextField fixMetadataFolder;
	private JFileChooser folderChooser;

	SettingsPanel() throws IOException {
		setBorder(new EmptyBorder(GUIConstants.EMPTYBORDER_INSETS, GUIConstants.EMPTYBORDER_INSETS, GUIConstants.EMPTYBORDER_INSETS, GUIConstants.EMPTYBORDER_INSETS));
		setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(6, 2));

		panel.add(new JLabel(GUIConstants.DISPLAY_PASSED_RULES));
		hidePassedRules = new JCheckBox();
		panel.add(hidePassedRules);
		panel.add(new JLabel(GUIConstants.MAX_NUMBER_FAILED_CHECKS));

		numberOfFailed = new JTextField();
		numberOfFailed.setTransferHandler(null);
		numberOfFailed.addKeyListener(getKeyAdapter(numberOfFailed, false));
		numberOfFailed.setToolTipText(GUIConstants.MAX_FAILED_CHECKS_SETTING_TIP);
		JPanel panel1 = new JPanel();
		panel1.setLayout(null);
		numberOfFailed.setBounds(0, 0, 65, 23);
		panel1.add(numberOfFailed);
		panel.add(panel1);

		panel.add(new JLabel(GUIConstants.MAX_NUMBER_FAILED_DISPLAYED_CHECKS));

		numberOfFailedDisplay = new JTextField();
		numberOfFailedDisplay.setTransferHandler(null);
		numberOfFailedDisplay.addKeyListener(getKeyAdapter(numberOfFailedDisplay, true));
		numberOfFailedDisplay.setToolTipText(GUIConstants.MAX_FAILED_CHECKS_DISP_SETTING_TIP);
		JPanel panel2 = new JPanel();
		panel2.setLayout(null);
		numberOfFailedDisplay.setBounds(0, 0, 65, 23);
		panel2.add(numberOfFailedDisplay);
		panel.add(panel2);

		panel.add(new JLabel(GUIConstants.FIX_METADATA_PREFIX_LABEL_TEXT));
		fixMetadataPrefix = new JTextField();
		fixMetadataPrefix.setTransferHandler(null);
		panel.add(fixMetadataPrefix);

		panel.add(new JLabel(GUIConstants.SELECTED_PATH_FOR_FIXER_LABEL_TEXT));

		File currentDir = new File(
				new File(GUIConstants.DOT).getCanonicalPath());

		JButton choose2 = new JButton(GUIConstants.THIRDPARTY_CONFIG_CHOOSE_BUTTON);
		folderChooser = new JFileChooser();
		folderChooser.setCurrentDirectory(currentDir);
		folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		choose2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int resultChoose = folderChooser.showOpenDialog(SettingsPanel.this);
				if (resultChoose == JFileChooser.APPROVE_OPTION) {
					if (!folderChooser.getSelectedFile().isDirectory()) {
						JOptionPane.showMessageDialog(SettingsPanel.this,
								"Error. Selected directory doesn't exist.",
								GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
					} else {
						fixMetadataFolder.setText(folderChooser.getSelectedFile().getAbsolutePath());
					}
				}

			}
		});
		fixMetadataFolder = new JTextField();
		fixMetadataFolder.setToolTipText(GUIConstants.SELECTED_PATH_FOR_FIXER_TOOLTIP);
		JPanel panel4 = new JPanel();
		panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));
		panel4.add(fixMetadataFolder);
		panel4.add(choose2);
		panel.add(panel4);

		panel.add(new JLabel(GUIConstants.THIRDPARTY_CONFIG_LABEL_TEXT));
		JButton choose = new JButton(GUIConstants.THIRDPARTY_CONFIG_CHOOSE_BUTTON);

		fixMetadataPrefix.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Config.Builder.isValidFileNameCharacter(e.getKeyChar())) {
					e.consume();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
			}
		});

		chooser = new JFileChooser();
		chooser.setCurrentDirectory(currentDir);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new FileNameExtensionFilter(GUIConstants.XML, GUIConstants.XML));

		choose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int resultChoose = chooser.showOpenDialog(SettingsPanel.this);
				if (resultChoose == JFileChooser.APPROVE_OPTION) {
					if (!chooser.getSelectedFile().exists()) {
						JOptionPane.showMessageDialog(SettingsPanel.this,
								"Error. Selected file doesn't exist.",
								GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
					} else if (!chooser.getSelectedFile().getName().toLowerCase()
							.endsWith(GUIConstants.DOT + GUIConstants.XML.toLowerCase())) {
						JOptionPane.showMessageDialog(
								SettingsPanel.this,
								"Error. Selected file is not in "
										+ GUIConstants.XML.toUpperCase() + " format.",
								GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
					} else {
						thirdPartyProfilePathField.setText(chooser.getSelectedFile().getAbsolutePath());
					}
				}

			}
		});

		thirdPartyProfilePathField = new JTextField();

		JPanel panel3 = new JPanel();
		panel3.setLayout(new BoxLayout(panel3, BoxLayout.X_AXIS));
		panel3.add(thirdPartyProfilePathField);
		panel3.add(choose);
		panel.add(panel3);

		add(panel, BorderLayout.CENTER);

		okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				boolean isEverythingValid = true;
				if (!Config.Builder.isValidFolderPath(FileSystems.getDefault().getPath(fixMetadataFolder.getText()))) {
					isEverythingValid = false;
					JOptionPane.showMessageDialog(SettingsPanel.this, "Invalid path for saving fixed files.", "Invalid data", JOptionPane.INFORMATION_MESSAGE);
				}
				if (!Config.Builder.isValidFilePath(FileSystems.getDefault().getPath(thirdPartyProfilePathField.getText()))) {
					isEverythingValid = false;
					JOptionPane.showMessageDialog(SettingsPanel.this, "Invalid path for features plugins config file.", "Invalid data", JOptionPane.INFORMATION_MESSAGE);
				}
				if (isEverythingValid) {
					ok = true;
					dialog.setVisible(false);
				}
			}
		});

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dialog.setVisible(false);
			}
		});

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	boolean showDialog(Component parent, String title, Config settings) {

		ok = false;

		hidePassedRules.setSelected(settings.isShowPassedRules());

		int numbOfFail = settings.getMaxNumberOfFailedChecks();
		if (numbOfFail == -1) {
			numberOfFailed.setText("");
		} else {
			numberOfFailed.setText(String.valueOf(numbOfFail));
		}

		int numbOfFailDisp = settings.getMaxNumberOfDisplayedFailedChecks();
		if (numbOfFailDisp == -1) {
			numberOfFailedDisplay.setText("");
		} else {
			numberOfFailedDisplay.setText(String.valueOf(numbOfFailDisp));
		}

		thirdPartyProfilePathField.setText(settings.getFeaturesPluginsConfigFilePath().toString());
		fixMetadataPrefix.setText(settings.getMetadataFixerPrefix());
		fixMetadataFolder.setText(settings.getFixMetadataPathFolder().toString());

		Frame owner;
		if (parent instanceof Frame) {
			owner = (Frame) parent;
		} else {
			owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parent);
		}

		if (dialog == null || dialog.getOwner() != owner) {
			dialog = new JDialog(owner, true);
			dialog.setResizable(false);
			dialog.add(this);
			dialog.getRootPane().setDefaultButton(okButton);
			dialog.pack();
			dialog.setTitle(title);
		}

		dialog.setLocation(GUIConstants.SETTINGSDIALOG_COORD_X, GUIConstants.SETTINGSDIALOG_COORD_Y);
		dialog.setSize(650, 241);
		dialog.setVisible(true);

		return ok;
	}

	private static KeyAdapter getKeyAdapter(final JTextField field, final boolean fromZero) {
		return new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if ((field.getText().length() == 6) && ((field.getSelectedText() == null) || (field.getSelectedText().length() == 0)) &&
						(c != KeyEvent.VK_BACK_SPACE) &&
						(c != KeyEvent.VK_DELETE)) {
					e.consume();
				} else if (c == '0' && ((!fromZero && field.getText().length() == 0) || field.getText().startsWith("0"))) {
					e.consume();
				} else if (!(((c >= '0') && (c <= '9')) ||
						(c == KeyEvent.VK_BACK_SPACE) ||
						(c == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (field.getText().startsWith("0")) {
					field.setText(field.getText().replaceFirst("0*", ""));
					if (field.getText().length() == 0) {
						if (fromZero) {
							field.setText("0");
						} else {
							field.setText("");
						}
					}
				}
				super.keyReleased(e);
			}
		};
	}

	boolean isDispPassedRules() {
		return hidePassedRules.isSelected();
	}

	int getFailedChecksNumber() {
		String str = numberOfFailed.getText();
		return str.length() > 0 ? Integer.parseInt(str) : -1;
	}

	int getFailedChecksDisplayNumber() {
		String str = numberOfFailedDisplay.getText();
		return str.length() > 0 ? Integer.parseInt(str) : -1;
	}

	Path getFeaturesPluginConfigPath() {
		return FileSystems.getDefault().getPath(thirdPartyProfilePathField.getText());
	}


	Path getFixMetadataDirectory() {
		return FileSystems.getDefault().getPath(fixMetadataFolder.getText());
	}

	String getFixMetadataPrefix() {
		return fixMetadataPrefix.getText();
	}
}
