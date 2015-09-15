package org.verapdf.gui;

import org.verapdf.gui.tools.GUIConstants;
import org.verapdf.gui.tools.SettingsHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Properties;

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

	/**
	 * Settings panel
	 */
	public SettingsPanel() {
		setBorder(new EmptyBorder(GUIConstants.EMPTYBORDER_INSETS, GUIConstants.EMPTYBORDER_INSETS, GUIConstants.EMPTYBORDER_INSETS, GUIConstants.EMPTYBORDER_INSETS));

		setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 2));

		panel.add(new JLabel(GUIConstants.PROCESSING_TYPE));

		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));
		panel.add(radioPanel);

		ButtonGroup bGroup = new ButtonGroup();
		valAndFeat = new JRadioButton(GUIConstants.VALIDATING_AND_FEATURES);
		bGroup.add(valAndFeat);
		radioPanel.add(valAndFeat);
		val = new JRadioButton(GUIConstants.VALIDATING);
		bGroup.add(val);
		radioPanel.add(val);
		feat = new JRadioButton(GUIConstants.FEATURES);
		bGroup.add(feat);
		radioPanel.add(feat);

		panel.add(new JLabel(GUIConstants.DISPLAY_PASSED_RULES));
		hidePassedRules = new JCheckBox();
		panel.add(hidePassedRules);
		panel.add(new JLabel(GUIConstants.MAX_NUMBER_FAILED_CHECKS));
		numberOfFailed = new JTextField();
		numberOfFailed.addKeyListener(getKeyAdapter(numberOfFailed, false));
		panel.add(numberOfFailed);
		panel.add(new JLabel(GUIConstants.MAX_NUMBER_FAILED_DISPLAYED_CHECKS));
		numberOfFailedDisplay = new JTextField();
		numberOfFailedDisplay.addKeyListener(getKeyAdapter(numberOfFailedDisplay, true));
		panel.add(numberOfFailedDisplay);

		add(panel, BorderLayout.CENTER);

		okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				ok = true;
				dialog.setVisible(false);
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

	/**
	 * Shows about dialog
	 *
	 * @param parent parent component of the dialog
	 * @param title  title of the dialog
	 */
	public boolean showDialog(Component parent, String title, Properties prop) {

		ok = false;

		boolean dispPassedRulesBool = SettingsHelper.isDispPassedRules(prop);
		hidePassedRules.setSelected(dispPassedRulesBool);

		int numbOfFail = SettingsHelper.getNumbOfFail(prop);
		if (numbOfFail == -1) {
			numberOfFailed.setText("");
		} else {
			numberOfFailed.setText(String.valueOf(numbOfFail));
		}

		int numbOfFailDisp = SettingsHelper.getNumbOfFailDisp(prop);
		if (numbOfFailDisp == -1) {
			numberOfFailedDisplay.setText("");
		} else {
			numberOfFailedDisplay.setText(String.valueOf(numbOfFailDisp));
		}

		int type = SettingsHelper.getProcessingType(prop);
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
		dialog.setVisible(true);

		return ok;
	}

	private static KeyAdapter getKeyAdapter(final JTextField field, final boolean fromZero) {
		return new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if ((field.getText().length() == 6) &&
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

	/**
	 * @return integer that indicates selected processing type.
	 */
	public int getProcessingType() {
		if (valAndFeat.isSelected()) {
			return GUIConstants.VALIDATING_AND_FEATURES_FLAG;
		} else if (val.isSelected()) {
			return GUIConstants.VALIDATING_FLAG;
		} else {
			return GUIConstants.FEATURES_FLAG;
		}
	}

	/**
	 * @return true if desplay passed pules option selected
	 */
	public boolean isDispPassedRules() {
		return hidePassedRules.isSelected();
	}

	/**
	 * @return selected number for maximum fail checks for a rule. If not selected returns -1
	 */
	public int getFailedChecksNumber() {
		String str = numberOfFailed.getText();
		return str.length() > 0 ? Integer.parseInt(str) : -1;
	}

	/**
	 * @return selected number for maximum displayed fail checks for a rule. If not selected returns -1
	 */
	public int getFailedChecksDisplayNumber() {
		String str = numberOfFailedDisplay.getText();
		return str.length() > 0 ? Integer.parseInt(str) : -1;
	}
}
