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

	private JButton okButton;
	private boolean ok;
	JDialog dialog;
	private JTextField numberOfFailed;
	private JTextField numberOfFailedDisplay;
	private JRadioButton valAndFeat;
	private JRadioButton val;
	private JRadioButton feat;
	private JCheckBox hidePassedRules;
	private JTextField thirdPartyProfilePathField;
	private JFileChooser chooser;

	SettingsPanel() throws IOException {
		setBorder(new EmptyBorder(GUIConstants.EMPTYBORDER_INSETS, GUIConstants.EMPTYBORDER_INSETS, GUIConstants.EMPTYBORDER_INSETS, GUIConstants.EMPTYBORDER_INSETS));
		setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		ButtonGroup bGroup = new ButtonGroup();
		panel.setLayout(new GridLayout(8, 2));

		panel.add(new JPanel());
		valAndFeat = new JRadioButton(GUIConstants.VALIDATING_AND_FEATURES);
		bGroup.add(valAndFeat);
		panel.add(valAndFeat);
		panel.add(new JLabel(GUIConstants.PROCESSING_TYPE));
		val = new JRadioButton(GUIConstants.VALIDATING);
		bGroup.add(val);
		panel.add(val);
		panel.add(new JPanel());
		feat = new JRadioButton(GUIConstants.FEATURES);
		bGroup.add(feat);
		panel.add(feat);

		panel.add(new JLabel(GUIConstants.DISPLAY_PASSED_RULES));
		hidePassedRules = new JCheckBox();
		panel.add(hidePassedRules);
		panel.add(new JLabel(GUIConstants.MAX_NUMBER_FAILED_CHECKS));

		numberOfFailed = new JTextField();
		numberOfFailed.addKeyListener(getKeyAdapter(numberOfFailed, false));

		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
		panel1.add(numberOfFailed);
		panel1.add(new JLabel(GUIConstants.MAX_FAILED_CHECKS_SETTING_TIP));
		panel.add(panel1);

		panel.add(new JLabel(GUIConstants.MAX_NUMBER_FAILED_DISPLAYED_CHECKS));

		numberOfFailedDisplay = new JTextField();
		numberOfFailedDisplay.addKeyListener(getKeyAdapter(numberOfFailedDisplay, true));

		JPanel panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
		panel2.add(numberOfFailedDisplay);
		panel2.add(new JLabel(GUIConstants.MAX_FAILED_CHECKS_DISP_SETTING_TIP));
		panel.add(panel2);

		panel.add(new JLabel(GUIConstants.THIRDPARTY_CONFIG_LABEL_TEXT));


		JButton choose = new JButton(GUIConstants.THIRDPARTY_CONFIG_CHOOSE_BUTTON);

		chooser = new JFileChooser();
		File currentDir = new File(
				new File(GUIConstants.DOT).getCanonicalPath());
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
				if (Config.Builder.isValidPathForFeaturesPluginsConfigFilePath(FileSystems.getDefault().getPath(thirdPartyProfilePathField.getText()))) {
					ok = true;
					dialog.setVisible(false);
				} else {
					JOptionPane.showMessageDialog(SettingsPanel.this, "Invalid path for features plugins config file.", "Invalid data", JOptionPane.INFORMATION_MESSAGE);
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

	boolean showDialog(Component parent, String title, Settings settings) {

		ok = false;

		boolean dispPassedRulesBool = settings.isShowPassedRules();
		hidePassedRules.setSelected(dispPassedRulesBool);

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

		int type = settings.getProcessingType();
		switch (type) {
			case 3:
				valAndFeat.setSelected(true);
				break;
			case 1:
				val.setSelected(true);
				break;
			case 2:
				feat.setSelected(true);
				break;
		}

		thirdPartyProfilePathField.setText(settings.getFeaturesPluginsConfigFilePath().toString());


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
		dialog.setSize(650, 281);
		dialog.setVisible(true);

		return ok;
	}

	private static KeyAdapter getKeyAdapter(final JTextField field, final boolean fromZero) {
		return new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if ((field.getText().length() == 6) && field.getSelectedText().length() == 0 &&
						(c != KeyEvent.VK_BACK_SPACE) &&
						(c != KeyEvent.VK_DELETE)) {
					e.consume();
				} else if (c == '0' && ((!fromZero && field.getText().length() == 0) || field.getText().startsWith("0"))) {
					e.consume();
				} else if (!(((c >= '0') && (c <= '9')) ||
						(c == KeyEvent.VK_BACK_SPACE) ||
						(c == KeyEvent.VK_DELETE))) {
					e.consume();
				} else {
					super.keyTyped(e);
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

	int getProcessingType() {
		if (valAndFeat.isSelected()) {
			return GUIConstants.VALIDATING_AND_FEATURES_FLAG;
		} else if (val.isSelected()) {
			return GUIConstants.VALIDATING_FLAG;
		} else {
			return GUIConstants.FEATURES_FLAG;
		}
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
}
