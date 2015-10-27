package org.verapdf.gui;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.IllegalComponentStateException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.verapdf.core.ProfileException;
import org.verapdf.core.ValidationException;
import org.verapdf.gui.config.Config;
import org.verapdf.gui.tools.GUIConstants;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.validation.profile.parser.LegacyProfileConverter;
import org.verapdf.validation.profile.parser.ValidationProfileParser;
import org.xml.sax.SAXException;

/**
 * Panel with functionality for checker.
 *
 * @author Maksim Bezrukov
 */
class CheckerPanel extends JPanel {

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
	private JLabel resultLabel;
	transient ValidationResult result;
	private File xmlReport;
	private File htmlReport;

	private JComboBox<String> processingType;
	private JCheckBox fixMetadata;

	private boolean isValidationErrorOccurred;

	private JButton validate;
	private JButton saveXML;
	private JButton viewXML;
	private JButton saveHTML;
	private JButton viewHTML;

	private transient Config config;

	JProgressBar progressBar;
	transient ValidateWorker validateWorker;

	CheckerPanel(Config config) throws IOException {

		this.config = config;
		setPreferredSize(new Dimension(GUIConstants.PREFERRED_SIZE_WIDTH,
				GUIConstants.PREFERRED_SIZE_HEIGHT));

		GridBagLayout gbl = new GridBagLayout();
		this.setLayout(gbl);
		GridBagConstraints gbc = new GridBagConstraints();

		this.chosenPDF = new JTextField(GUIConstants.PDF_NOT_CHOSEN_TEXT);
		this.chosenPDF.setEditable(false);
		setGridBagConstraintsParameters(gbc,
				GUIConstants.CHOSENPDF_LABEL_CONSTRAINT_GRIDX,
				GUIConstants.CHOSENPDF_LABEL_CONSTRAINT_GRIDY,
				GUIConstants.CHOSENPDF_LABEL_CONSTRAINT_WEIGHTX,
				GUIConstants.CHOSENPDF_LABEL_CONSTRAINT_WEIGHTY,
				GUIConstants.CHOSENPDF_LABEL_CONSTRAINT_GRIDWIDTH,
				GUIConstants.CHOSENPDF_LABEL_CONSTRAINT_GRIDHEIGHT,
				GridBagConstraints.HORIZONTAL);
		gbl.setConstraints(this.chosenPDF, gbc);
		this.add(this.chosenPDF);

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

		this.chosenProfile = new JTextField(
				GUIConstants.VALIDATION_PROFILE_NOT_CHOSEN);
		this.chosenProfile.setEditable(false);
		setGridBagConstraintsParameters(gbc,
				GUIConstants.CHOSENPROFILE_LABEL_CONSTRAINT_GRIDX,
				GUIConstants.CHOSENPROFILE_LABEL_CONSTRAINT_GRIDY,
				GUIConstants.CHOSENPROFILE_LABEL_CONSTRAINT_WEIGHTX,
				GUIConstants.CHOSENPROFILE_LABEL_CONSTRAINT_WEIGHTY,
				GUIConstants.CHOSENPROFILE_LABEL_CONSTRAINT_GRIDWIDTH,
				GUIConstants.CHOSENPROFILE_LABEL_CONSTRAINT_GRIDHEIGHT,
				GridBagConstraints.HORIZONTAL);
		gbl.setConstraints(this.chosenProfile, gbc);
		this.add(this.chosenProfile);

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

		this.resultLabel = new JLabel();
		this.resultLabel.setForeground(GUIConstants.BEFORE_VALIDATION_COLOR);
		this.resultLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		setGridBagConstraintsParameters(gbc,
				GUIConstants.RESULT_LABEL_CONSTRAINT_GRIDX,
				GUIConstants.RESULT_LABEL_CONSTRAINT_GRIDY,
				GUIConstants.RESULT_LABEL_CONSTRAINT_WEIGHTX,
				GUIConstants.RESULT_LABEL_CONSTRAINT_WEIGHTY,
				GUIConstants.RESULT_LABEL_CONSTRAINT_GRIDWIDTH,
				GUIConstants.RESULT_LABEL_CONSTRAINT_GRIDHEIGHT,
				GridBagConstraints.CENTER);
		gbl.setConstraints(this.resultLabel, gbc);
		this.add(this.resultLabel);

		this.progressBar = new JProgressBar();
		this.progressBar.setIndeterminate(true);
		this.progressBar.setVisible(false);
		setGridBagConstraintsParameters(gbc,
				GUIConstants.PROGRESSBAR_CONSTRAINT_GRIDX,
				GUIConstants.PROGRESSBAR_CONSTRAINT_GRIDY,
				GUIConstants.PROGRESSBAR_CONSTRAINT_WEIGHTX,
				GUIConstants.PROGRESSBAR_CONSTRAINT_WEIGHTY,
				GUIConstants.PROGRESSBAR_CONSTRAINT_GRIDWIDTH,
				GUIConstants.PROGRESSBAR_CONSTRAINT_GRIDHEIGHT,
				GridBagConstraints.HORIZONTAL);
		gbl.setConstraints(this.progressBar, gbc);
		this.add(this.progressBar);

		this.validate = new JButton(GUIConstants.VALIDATE_BUTTON_TEXT);
		this.validate.setEnabled(false);
		setGridBagConstraintsParameters(gbc,
				GUIConstants.VALIDATE_BUTTON_CONSTRAINT_GRIDX,
				GUIConstants.VALIDATE_BUTTON_CONSTRAINT_GRIDY,
				GUIConstants.VALIDATE_BUTTON_CONSTRAINT_WEIGHTX,
				GUIConstants.VALIDATE_BUTTON_CONSTRAINT_WEIGHTY,
				GUIConstants.VALIDATE_BUTTON_CONSTRAINT_GRIDWIDTH,
				GUIConstants.VALIDATE_BUTTON_CONSTRAINT_GRIDHEIGHT,
				GridBagConstraints.HORIZONTAL);
		gbl.setConstraints(this.validate, gbc);
		this.add(this.validate);

		final JLabel processType = new JLabel(GUIConstants.PROCESSING_TYPE);
		setGridBagConstraintsParameters(gbc,
				0,
				3,
				0,
				1,
				1,
				1,
				GridBagConstraints.HORIZONTAL);
		gbl.setConstraints(processType, gbc);
		this.add(processType);

		String[] types = new String[]{GUIConstants.VALIDATING_AND_FEATURES, GUIConstants.VALIDATING, GUIConstants.FEATURES};
		this.processingType = new JComboBox<>(types);
		setGridBagConstraintsParameters(gbc,
				1,
				3,
				0,
				1,
				1,
				1,
				GridBagConstraints.HORIZONTAL);
		gbl.setConstraints(this.processingType, gbc);
		this.add(this.processingType);

		this.fixMetadata = new JCheckBox(GUIConstants.FIX_METADATA_LABEL_TEXT);
		this.fixMetadata.setSelected(false);
		setGridBagConstraintsParameters(gbc,
				2,
				3,
				0,
				1,
				1,
				1,
				GridBagConstraints.HORIZONTAL);
		gbl.setConstraints(this.fixMetadata, gbc);
		this.add(this.fixMetadata);

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

		this.saveXML = new JButton(GUIConstants.SAVE_REPORT_BUTTON_TEXT);
		this.saveXML.setEnabled(false);
		reports.add(this.saveXML);

		this.viewXML = new JButton(GUIConstants.VIEW_REPORT_BUTTON_TEXT);
		this.viewXML.setEnabled(false);
		reports.add(this.viewXML);

		LogoPanel htmlLogo = new LogoPanel(GUIConstants.HTML_LOGO_NAME,
				reports.getBackground(), GUIConstants.HTMLLOGO_BORDER_WIDTH);
		reports.add(htmlLogo);

		this.saveHTML = new JButton(GUIConstants.SAVE_HTML_REPORT_BUTTON_TEXT);
		this.saveHTML.setEnabled(false);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		reports.add(this.saveHTML);

		this.viewHTML = new JButton(GUIConstants.VIEW_HTML_REPORT_BUTTON_TEXT);
		this.viewHTML.setEnabled(false);
		reports.add(this.viewHTML);

		this.pdfChooser = getChooser(GUIConstants.PDF);
		this.xmlChooser = getChooser(GUIConstants.XML);
		this.htmlChooser = getChooser(GUIConstants.HTML);

		choosePDF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CheckerPanel.this.chooseFile(CheckerPanel.this.pdfChooser, GUIConstants.PDF);
			}
		});

		chooseProfile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CheckerPanel.this.chooseFile(CheckerPanel.this.xmlChooser, GUIConstants.XML);
			}
		});


		this.validate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int index = CheckerPanel.this.processingType.getSelectedIndex();
					int flag;
					switch (index) {
						case 0:
							flag = GUIConstants.VALIDATING_AND_FEATURES_FLAG;
							break;
						case 1:
							flag = GUIConstants.VALIDATING_FLAG;
							break;
						case 2:
							flag = GUIConstants.FEATURES_FLAG;
							break;
						default:
							throw new IllegalComponentStateException("Processing type list must contain only 3 values");
					}
					
					org.verapdf.validation.profile.model.ValidationProfile toConvert = ValidationProfileParser
		                    .parseFromFilePath(
		                            "/home/cfw/GitHub/veraPDF/veraPDF-validation-profiles/PDF_A/PDFA-1B.xml",
		                            false);
					ValidationProfile prof = LegacyProfileConverter.fromLegacyProfile(toConvert, PDFAFlavour.PDFA_1_B);
                    CheckerPanel.this.validateWorker = new ValidateWorker(CheckerPanel.this, CheckerPanel.this.pdfFile, prof, CheckerPanel.this.config, flag, CheckerPanel.this.fixMetadata.isSelected());
					CheckerPanel.this.progressBar.setVisible(true);
					CheckerPanel.this.resultLabel.setVisible(false);
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					CheckerPanel.this.validate.setEnabled(false);
					CheckerPanel.this.result = null;
					CheckerPanel.this.isValidationErrorOccurred = false;
					CheckerPanel.this.viewXML.setEnabled(false);
					CheckerPanel.this.saveXML.setEnabled(false);
					CheckerPanel.this.viewHTML.setEnabled(false);
					CheckerPanel.this.saveHTML.setEnabled(false);
					CheckerPanel.this.validateWorker.execute();
				} catch (IllegalArgumentException | ProfileException | ValidationException | ParserConfigurationException | SAXException | IOException | XMLStreamException exep) {
					JOptionPane.showMessageDialog(CheckerPanel.this, exep.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					LOGGER.error(exep);
				}
			}
		});

		this.saveXML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveReport(CheckerPanel.this.xmlChooser, GUIConstants.XML, CheckerPanel.this.xmlReport);
			}
		});

		this.saveHTML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveReport(CheckerPanel.this.htmlChooser, GUIConstants.HTML, CheckerPanel.this.htmlReport);
			}
		});

		this.viewXML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (CheckerPanel.this.xmlReport == null) {
					JOptionPane.showMessageDialog(CheckerPanel.this,
							"XML report hasn't been saved.",
							GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
				} else {
					this.openXMLReport();
				}
			}

			private void openXMLReport() {
				try {
					Desktop.getDesktop().open(CheckerPanel.this.xmlReport);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(CheckerPanel.this,
							"Some error in opening the XML report.",
							GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
					LOGGER.error("Exception in opening the XML report", e1);
				}
			}
		});

		this.viewHTML.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (CheckerPanel.this.htmlReport == null) {
					JOptionPane.showMessageDialog(CheckerPanel.this,
							"HTML report hasn't been saved.",
							GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						Desktop.getDesktop().open(CheckerPanel.this.htmlReport);
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(CheckerPanel.this,
								"Some error in opening the HTML report.",
								GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
						LOGGER.error("Exception in opening the HTML report", e1);
					}
				}
			}
		});

		this.processingType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = CheckerPanel.this.processingType.getSelectedIndex();
				switch (index) {
					case 0:
						CheckerPanel.this.fixMetadata.setEnabled(true);
						break;
					case 1:
						CheckerPanel.this.fixMetadata.setEnabled(true);
						break;
					case 2:
						CheckerPanel.this.fixMetadata.setEnabled(false);
						break;
				}
			}
		});

	}

	void validationEnded(File xmlReport, File htmlReport) {

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.progressBar.setVisible(false);
		this.validate.setEnabled(true);

		if (!this.isValidationErrorOccurred) {
			try {
				this.result = this.validateWorker.get();
				if (this.result == null) {
					this.resultLabel.setForeground(GUIConstants.BEFORE_VALIDATION_COLOR);
					this.resultLabel.setText(GUIConstants.FEATURES_GENERATED_CORRECT);
				} else if (this.result.isCompliant()) {
					this.resultLabel.setForeground(GUIConstants.VALIDATION_SUCCESS_COLOR);
					this.resultLabel.setText(GUIConstants.VALIDATION_OK);
				} else {
					this.resultLabel.setForeground(GUIConstants.VALIDATION_FAILED_COLOR);
					this.resultLabel.setText(GUIConstants.VALIDATION_FALSE);
				}

				this.resultLabel.setVisible(true);

				this.xmlReport = xmlReport;
				this.htmlReport = htmlReport;

				if (xmlReport != null) {
					this.saveXML.setEnabled(true);
					this.viewXML.setEnabled(true);
				}

				if (htmlReport != null) {
					this.saveHTML.setEnabled(true);
					this.viewHTML.setEnabled(true);
				}

			} catch (InterruptedException e) {
				errorInValidatingOccur("Process has interrupted.", e);
			} catch (ExecutionException e) {
				errorInValidatingOccur("Execution exception in processing.", e);
			}
		}

	}

	void errorInValidatingOccur(String message, Throwable e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		this.progressBar.setVisible(false);
		this.isValidationErrorOccurred = true;
		JOptionPane.showMessageDialog(CheckerPanel.this, message,
				GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);

		LOGGER.error("Exception during the validation process", e);

		this.resultLabel.setForeground(GUIConstants.VALIDATION_FAILED_COLOR);
		this.resultLabel.setText(message);
		this.resultLabel.setVisible(true);
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

				this.result = null;
				this.resultLabel.setForeground(GUIConstants.BEFORE_VALIDATION_COLOR);
				this.resultLabel.setText("");
				this.xmlReport = null;
				this.htmlReport = null;
				this.saveXML.setEnabled(false);
				this.viewXML.setEnabled(false);
				this.saveHTML.setEnabled(false);
				this.viewHTML.setEnabled(false);

				switch (extension) {
					case GUIConstants.PDF:
						this.pdfFile = chooser.getSelectedFile();
						this.chosenPDF.setText(this.pdfFile.getAbsolutePath());
						if (this.profile != null) {
							this.validate.setEnabled(true);
						}
						break;
					case GUIConstants.XML:
						this.profile = chooser.getSelectedFile();
						this.chosenProfile.setText(this.profile.getAbsolutePath());
						if (this.pdfFile != null) {
							this.validate.setEnabled(true);
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
		if (report == null) {
			JOptionPane.showMessageDialog(CheckerPanel.this,
					"Validation hasn't been run.", GUIConstants.ERROR,
					JOptionPane.ERROR_MESSAGE);
		} else {
			chooser.setSelectedFile(new File(extension.toLowerCase()
					+ "Report." + extension.toLowerCase()));
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

	void setConfig(Config config) {
		this.config = config;
	}
}
