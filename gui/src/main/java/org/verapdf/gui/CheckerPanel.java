package org.verapdf.gui;

import org.apache.log4j.Logger;
import org.verapdf.gui.tools.GUIConstants;
import org.verapdf.report.HTMLReport;
import org.verapdf.validation.report.model.ValidationInfo;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutionException;

/**
 * Panel with functionality for checker.
 *
 * @author Maksim Bezrukov
 */
public class CheckerPanel extends JPanel {

	/**
	 * ID for serialisation
	 */
	private static final long serialVersionUID = 1290058869994329766L;

	private static final Logger LOGGER = Logger.getLogger(CheckerPanel.class);

	private JFileChooser pdfChooser;
	private JFileChooser xmlChooser;
	private JFileChooser htmlChooser;
	private File pdfFile;
	private File profile;
	private JTextField chosenPDF;
	private JTextField chosenProfile;
	private JLabel result;
	transient ValidationInfo info;
	private File xmlReport;
	private File htmlReport;
	private File image = new File("./temp/" + HTMLReport.getLogoImageName());

	private boolean isValidationErrorOccurred;

	private JButton validate;
	private JButton saveXML;
	private JButton viewXML;
	private JButton saveHTML;
	private JButton viewHTML;

	JProgressBar progressBar;
	transient ValidateWorker validateWorker;

	/**
	 * Creates the Panel.
	 *
	 * @throws IOException when there's a problem reading an image from the input stream
	 */
	public CheckerPanel() throws IOException {

		setPreferredSize(new Dimension(GUIConstants.PREFERRED_SIZE_WIDTH,
				GUIConstants.PREFERRED_SIZE_HEIGHT));

		GridBagLayout gbl = new GridBagLayout();
		this.setLayout(gbl);
		GridBagConstraints gbc = new GridBagConstraints();

		chosenPDF = new JTextField(GUIConstants.PDF_NOT_CHOSEN_TEXT);
		chosenPDF.setEditable(false);
		setGridBagConstraintsParameters(gbc,
				GUIConstants.CHOSENPDF_LABEL_CONSTRAINT_GRIDX,
				GUIConstants.CHOSENPDF_LABEL_CONSTRAINT_GRIDY,
				GUIConstants.CHOSENPDF_LABEL_CONSTRAINT_WEIGHTX,
				GUIConstants.CHOSENPDF_LABEL_CONSTRAINT_WEIGHTY,
				GUIConstants.CHOSENPDF_LABEL_CONSTRAINT_GRIDWIDTH,
				GUIConstants.CHOSENPDF_LABEL_CONSTRAINT_GRIDHEIGHT,
				GridBagConstraints.HORIZONTAL);
		gbl.setConstraints(chosenPDF, gbc);
		this.add(chosenPDF);

		JButton choosePDF = new JButton(GUIConstants.CHOOSE_PDF_BUTTON_TEXT);
		setGridBagConstraintsParameters(gbc,
				GUIConstants.CHOOSEPDF_BUTTON_CONSTRAINT_GRIDX,
				GUIConstants.CHOOSEPDF_BUTTON_CONSTRAINT_GRIDY,
				GUIConstants.CHOOSEPDF_BUTTON_CONSTRAINT_WEIGHTX,
				GUIConstants.CHOOSEPDF_BUTTON_CONSTRAINT_WEIGHTY,
				GUIConstants.CHOOSEPDF_BUTTON_CONSTRAINT_GRIDWIDTH,
				GUIConstants.CHOOSEPDF_BUTTON_CONSTRAINT_GRIDHEIGHT,
				GridBagConstraints.HORIZONTAL);
		gbl.setConstraints(choosePDF, gbc);
		this.add(choosePDF);

		chosenProfile = new JTextField(
				GUIConstants.VALIDATION_PROFILE_NOT_CHOSEN);
		chosenProfile.setEditable(false);
		setGridBagConstraintsParameters(gbc,
				GUIConstants.CHOSENPROFILE_LABEL_CONSTRAINT_GRIDX,
				GUIConstants.CHOSENPROFILE_LABEL_CONSTRAINT_GRIDY,
				GUIConstants.CHOSENPROFILE_LABEL_CONSTRAINT_WEIGHTX,
				GUIConstants.CHOSENPROFILE_LABEL_CONSTRAINT_WEIGHTY,
				GUIConstants.CHOSENPROFILE_LABEL_CONSTRAINT_GRIDWIDTH,
				GUIConstants.CHOSENPROFILE_LABEL_CONSTRAINT_GRIDHEIGHT,
				GridBagConstraints.HORIZONTAL);
		gbl.setConstraints(chosenProfile, gbc);
		this.add(chosenProfile);

		JButton chooseProfile = new JButton(
				GUIConstants.CHOOSE_PROFILE_BUTTON_TEXT);
		setGridBagConstraintsParameters(gbc,
				GUIConstants.CHOOSEPROFILE_BUTTON_CONSTRAINT_GRIDX,
				GUIConstants.CHOOSEPROFILE_BUTTON_CONSTRAINT_GRIDY,
				GUIConstants.CHOOSEPROFILE_BUTTON_CONSTRAINT_WEIGHTX,
				GUIConstants.CHOOSEPROFILE_BUTTON_CONSTRAINT_WEIGHTY,
				GUIConstants.CHOOSEPROFILE_BUTTON_CONSTRAINT_GRIDWIDTH,
				GUIConstants.CHOOSEPROFILE_BUTTON_CONSTRAINT_GRIDHEIGHT,
				GridBagConstraints.HORIZONTAL);
		gbl.setConstraints(chooseProfile, gbc);
		this.add(chooseProfile);

		result = new JLabel();
		result.setForeground(GUIConstants.BEFORE_VALIDATION_COLOR);
		result.setHorizontalTextPosition(SwingConstants.CENTER);
		setGridBagConstraintsParameters(gbc,
				GUIConstants.RESULT_LABEL_CONSTRAINT_GRIDX,
				GUIConstants.RESULT_LABEL_CONSTRAINT_GRIDY,
				GUIConstants.RESULT_LABEL_CONSTRAINT_WEIGHTX,
				GUIConstants.RESULT_LABEL_CONSTRAINT_WEIGHTY,
				GUIConstants.RESULT_LABEL_CONSTRAINT_GRIDWIDTH,
				GUIConstants.RESULT_LABEL_CONSTRAINT_GRIDHEIGHT,
				GridBagConstraints.CENTER);
		gbl.setConstraints(result, gbc);
		this.add(result);

		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setVisible(false);
		setGridBagConstraintsParameters(gbc,
				GUIConstants.PROGRESSBAR_CONSTRAINT_GRIDX,
				GUIConstants.PROGRESSBAR_CONSTRAINT_GRIDY,
				GUIConstants.PROGRESSBAR_CONSTRAINT_WEIGHTX,
				GUIConstants.PROGRESSBAR_CONSTRAINT_WEIGHTY,
				GUIConstants.PROGRESSBAR_CONSTRAINT_GRIDWIDTH,
				GUIConstants.PROGRESSBAR_CONSTRAINT_GRIDHEIGHT,
				GridBagConstraints.HORIZONTAL);
		gbl.setConstraints(progressBar, gbc);
		this.add(progressBar);

		validate = new JButton(GUIConstants.VALIDATE_BUTTON_TEXT);
		validate.setEnabled(false);
		setGridBagConstraintsParameters(gbc,
				GUIConstants.VALIDATE_BUTTON_CONSTRAINT_GRIDX,
				GUIConstants.VALIDATE_BUTTON_CONSTRAINT_GRIDY,
				GUIConstants.VALIDATE_BUTTON_CONSTRAINT_WEIGHTX,
				GUIConstants.VALIDATE_BUTTON_CONSTRAINT_WEIGHTY,
				GUIConstants.VALIDATE_BUTTON_CONSTRAINT_GRIDWIDTH,
				GUIConstants.VALIDATE_BUTTON_CONSTRAINT_GRIDHEIGHT,
				GridBagConstraints.HORIZONTAL);
		gbl.setConstraints(validate, gbc);
		this.add(validate);

		JPanel reports = new JPanel();
		reports.setBorder(BorderFactory.createTitledBorder(GUIConstants.REPORT));
		reports.setLayout(new GridLayout(
				GUIConstants.REPORT_PANEL_LINES_NUMBER,
				GUIConstants.REPORT_PANEL_COLUMNS_NUMBER));
		setGridBagConstraintsParameters(gbc,
				GUIConstants.REPORT_PANEL_CONSTRAINT_GRIDX,
				GUIConstants.REPORT_PANEL_CONSTRAINT_GRIDY,
				GUIConstants.REPORT_PANEL_CONSTRAINT_WEIGHTX,
				GUIConstants.REPORT_PANEL_CONSTRAINT_WEIGHTY,
				GUIConstants.REPORT_PANEL_CONSTRAINT_GRIDWIDTH,
				GUIConstants.REPORT_PANEL_CONSTRAINT_GRIDHEIGHT,
				GridBagConstraints.HORIZONTAL);
		gbl.setConstraints(reports, gbc);
		this.add(reports);

		LogoPanel xmlLogo = new LogoPanel(GUIConstants.XML_LOGO_NAME,
				reports.getBackground(), GUIConstants.XMLLOGO_BORDER_WIDTH);
		reports.add(xmlLogo);

		saveXML = new JButton(GUIConstants.SAVE_REPORT_BUTTON_TEXT);
		saveXML.setEnabled(false);
		reports.add(saveXML);

		viewXML = new JButton(GUIConstants.VIEW_REPORT_BUTTON_TEXT);
		viewXML.setEnabled(false);
		reports.add(viewXML);

		LogoPanel htmlLogo = new LogoPanel(GUIConstants.HTML_LOGO_NAME,
				reports.getBackground(), GUIConstants.HTMLLOGO_BORDER_WIDTH);
		reports.add(htmlLogo);

		saveHTML = new JButton(GUIConstants.SAVE_HTML_REPORT_BUTTON_TEXT);
		saveHTML.setEnabled(false);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		reports.add(saveHTML);

		viewHTML = new JButton(GUIConstants.VIEW_HTML_REPORT_BUTTON_TEXT);
		viewHTML.setEnabled(false);
		reports.add(viewHTML);

		pdfChooser = getChooser(GUIConstants.PDF);
		xmlChooser = getChooser(GUIConstants.XML);
		htmlChooser = getChooser(GUIConstants.HTML);

		choosePDF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CheckerPanel.this.chooseFile(pdfChooser, GUIConstants.PDF);
			}
		});

		chooseProfile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CheckerPanel.this.chooseFile(xmlChooser, GUIConstants.XML);
			}
		});

		validate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				progressBar.setVisible(true);
				result.setVisible(false);
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				validate.setEnabled(false);
				info = null;
				isValidationErrorOccurred = false;

				validateWorker = new ValidateWorker(CheckerPanel.this, pdfFile,
						profile);
				validateWorker.execute();
			}
		});

		saveXML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveReport(xmlChooser, GUIConstants.XML, xmlReport);
			}
		});

		saveHTML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveReport(htmlChooser, GUIConstants.HTML, htmlReport);
			}
		});

		viewXML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (xmlReport == null) {
					JOptionPane.showMessageDialog(CheckerPanel.this,
							"XML report hasn't been saved.",
							GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
				} else {
					this.openXMLReport();
				}
			}

			private void openXMLReport() {
				try {
					Desktop.getDesktop().open(xmlReport);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(CheckerPanel.this,
							"Some error in opening the XML report.",
							GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
					LOGGER.error("Exception in opening the XML report", e1);
				}
			}
		});

		viewHTML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (htmlReport == null) {
					JOptionPane.showMessageDialog(CheckerPanel.this,
							"HTML report hasn't been saved.",
							GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						Desktop.getDesktop().open(htmlReport);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(CheckerPanel.this,
								"Some error in opening the HTML report.",
								GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
						LOGGER.error("Exception in opening the HTML report", e1);
					}
				}
			}
		});

	}

	/**
	 * Method to notify panel that validation was done.
	 *
	 * @param xmlReport  xml report file
	 * @param htmlReport html report file
	 */
	public void validationEnded(File xmlReport, File htmlReport) {

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		progressBar.setVisible(false);
		validate.setEnabled(true);

		if (!isValidationErrorOccurred) {
			try {
				info = validateWorker.get();
				if (info.getResult().isCompliant()) {
					result.setForeground(GUIConstants.VALIDATION_SUCCESS_COLOR);
					result.setText(GUIConstants.VALIDATION_OK);
				} else {
					result.setForeground(GUIConstants.VALIDATION_FAILED_COLOR);
					result.setText(GUIConstants.VALIDATION_FALSE);
				}

				result.setVisible(true);
			} catch (InterruptedException e) {
				errorInValidatingOccur("Validation has interrupted.", e);
			} catch (ExecutionException e) {
				errorInValidatingOccur("Execution exception in validating.", e);
			}
		}

		this.xmlReport = xmlReport;
		this.htmlReport = htmlReport;

		if (xmlReport != null) {
			saveXML.setEnabled(true);
			viewXML.setEnabled(true);
		}

		if (htmlReport != null) {
			saveHTML.setEnabled(true);
			viewHTML.setEnabled(true);
		}

	}

	/**
	 * Method to notify panel that some error occurs at validating
	 *
	 * @param message a message to be displayed
	 * @param e       the {@link Throwable} thrown during the validation process
	 */
	public void errorInValidatingOccur(String message, Throwable e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		progressBar.setVisible(false);
		isValidationErrorOccurred = true;
		JOptionPane.showMessageDialog(CheckerPanel.this, message,
				GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);

		LOGGER.error("Exception during the validation process", e);

		result.setForeground(GUIConstants.VALIDATION_FAILED_COLOR);
		result.setText(message);
		result.setVisible(true);
	}

	private static JFileChooser getChooser(String type) throws IOException {
		JFileChooser res = new JFileChooser();
		File currentDir = new File(
				new File(GUIConstants.DOT).getCanonicalPath());
		res.setCurrentDirectory(currentDir);
		res.setAcceptAllFileFilterUsed(false);
		res.setFileFilter(new FileNameExtensionFilter(type, type));
		return res;
	}

	private static void setGridBagConstraintsParameters(GridBagConstraints gbc,
														int gridx, int gridy, int weightx, int weighty, int gridwidth,
														int gridheight, int fill) {
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.fill = fill;
	}

	private void chooseFile(JFileChooser chooser, String extension) {
		int resultChoose = chooser.showOpenDialog(CheckerPanel.this);
		if (resultChoose == JFileChooser.APPROVE_OPTION) {

			if (!chooser.getSelectedFile().exists()) {
				JOptionPane.showMessageDialog(CheckerPanel.this,
						"Error. Selected file doesn't exist.",
						GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
			} else if (!chooser.getSelectedFile().getName().toLowerCase()
					.endsWith(GUIConstants.DOT + extension.toLowerCase())) {
				JOptionPane.showMessageDialog(
						CheckerPanel.this,
						"Error. Selected file is not in "
								+ extension.toUpperCase() + " format.",
						GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
			} else {

				info = null;
				result.setForeground(GUIConstants.BEFORE_VALIDATION_COLOR);
				result.setText("");
				xmlReport = null;
				htmlReport = null;
				saveXML.setEnabled(false);
				viewXML.setEnabled(false);
				saveHTML.setEnabled(false);
				viewHTML.setEnabled(false);

				switch (extension) {
					case GUIConstants.PDF:
						pdfFile = chooser.getSelectedFile();
						chosenPDF.setText(pdfFile.getAbsolutePath());
						if (profile != null) {
							validate.setEnabled(true);
						}
						break;
					case GUIConstants.XML:
						profile = chooser.getSelectedFile();
						chosenProfile.setText(profile.getAbsolutePath());
						if (pdfFile != null) {
							validate.setEnabled(true);
						}
						break;
					default:
						// This method used only for previous two cases.
						// So do nothing.
				}
			}
		}
	}

	private void saveReport(JFileChooser chooser, String extension, File report) {
		if (info == null) {
			JOptionPane.showMessageDialog(CheckerPanel.this,
					"Validation hasn't been run.", GUIConstants.ERROR,
					JOptionPane.ERROR_MESSAGE);
		} else {
			chooser.setSelectedFile(new File(extension.toLowerCase()
					+ "Report.html"));
			int resultChoose = chooser.showSaveDialog(CheckerPanel.this);
			if (resultChoose == JFileChooser.APPROVE_OPTION) {
				File temp = chooser.getSelectedFile();

				if (!(temp.getName().toLowerCase().endsWith(GUIConstants.DOT
						+ extension.toLowerCase()))) {
					temp = new File(temp.getPath() + GUIConstants.DOT
							+ extension.toLowerCase());
				}

				try {
					try {
						Files.copy(report.toPath(), temp.toPath());
						if (GUIConstants.HTML.equalsIgnoreCase(extension
								.toLowerCase())) {
							File newImage = new File(temp.getParentFile(),
									image.getName());
							Files.copy(image.toPath(), newImage.toPath(),
									StandardCopyOption.REPLACE_EXISTING);
						}
					} catch (FileAlreadyExistsException e1) {
						LOGGER.debug(
								"File already exists, conform overwrite with user",
								e1);
						int resultOption = JOptionPane
								.showConfirmDialog(
										CheckerPanel.this,
										extension.toUpperCase()
												+ " file with the same name already exists. Do you want to overwrite it?",
										"", JOptionPane.YES_NO_OPTION);
						if (resultOption == JOptionPane.YES_OPTION) {
							Files.copy(report.toPath(), temp.toPath(),
									StandardCopyOption.REPLACE_EXISTING);
							if (GUIConstants.HTML.equalsIgnoreCase(extension)) {
								File newImage = new File(temp.getParentFile(),
										image.getName());
								Files.copy(image.toPath(), newImage.toPath(),
										StandardCopyOption.REPLACE_EXISTING);
							}
						}
					}
				} catch (IOException e) {
					JOptionPane.showMessageDialog(CheckerPanel.this,
							GUIConstants.ERROR_IN_SAVING_HTML_REPORT,
							GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
					LOGGER.error("Exception saving " + extension.toUpperCase()
							+ " report", e);
				}
			}
		}
	}
}
