package org.verapdf.gui;

import org.verapdf.features.tools.FeaturesCollection;
import org.verapdf.report.HTMLReport;
import org.verapdf.report.XMLReport;
import org.verapdf.validation.report.model.ValidationInfo;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

/**
 * Panel with functionality for checker.
 *
 * @author Maksim Bezrukov
 */
public class CheckerPanel extends JPanel {

    private static final String XML_LOGO_NAME = "xml-logo.png";
    private static final String HTML_LOGO_NAME = "html-logo.png";
    private static final String CHOOSE_PDF_BUTTON_TEXT = "Choose PDF";
    private static final String PDF_NOT_CHOSEN_TEXT = "PDF file not chosen";
    private static final String CHOOSE_PROFILE_BUTTON_TEXT = "Choose Profile";
    private static final String VALIDATION_PROFILE_NOT_CHOSEN = "Validation profile not chosen";
    private static final String VALIDATE_BUTTON_TEXT = "Validate";
    private static final String VALIDATION_OK = "PDF file is compliant with Validation Profile requirements";
    private static final String VALIDATION_FALSE = "PDF file is not compliant with Validation Profile requirements";
    private static final String SAVE_REPORT_BUTTON_TEXT = "Save XML";
    private static final String VIEW_REPORT_BUTTON_TEXT = "View XML";
    private static final String SAVE_HTML_REPORT_BUTTON_TEXT = "Save HTML";
    private static final String VIEW_HTML_REPORT_BUTTON_TEXT = "View HTML";
    private static final String REPORT = "Report";
    private static final String ERROR = "Error";
    private static final String ERROR_IN_SAVING_HTML_REPORT = "Some error in saving the HTML report.";
    private static final String ERROR_IN_SAVING_XML_REPORT = "Some error in saving the XML report.";


    private JFileChooser pdfChooser;
    private JFileChooser xmlChooser;
    private JFileChooser htmlChooser;
    private File pdfFile;
    private File profile;
    private JTextField chosenPDF;
    private JTextField chosenProfile;
    private JLabel result;
    private ValidationInfo info;
    private File xmlReport;
    private File htmlReport;
    private File image = null;

    private long startTimeOfValidation;
    private long endTimeOfValidation;

    private JButton saveXML;
    private JButton viewXML;
    private JButton saveHTML;
    private JButton viewHTML;

    private JProgressBar progressBar;
    private ValidateWorker validateWorker;

    private Color beforeValidationColor = Color.BLACK;
    private Color validationSuccessColor = new Color(0, 180, 0);
    private Color validationFailedColor = new Color(180, 0, 0);


    /**
     * Creates the Panel.
     */
    public CheckerPanel() throws IOException {

        setPreferredSize(new Dimension(450, 200));

        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridheight = 1;

        chosenPDF = new JTextField(PDF_NOT_CHOSEN_TEXT);
        chosenPDF.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 3;
        gbc.weighty = 1;
        gbc.gridwidth = 3;
        gbl.setConstraints(chosenPDF, gbc);
        this.add(chosenPDF);

        JButton choosePDF = new JButton(CHOOSE_PDF_BUTTON_TEXT);
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.gridwidth = 1;
        gbl.setConstraints(choosePDF, gbc);
        this.add(choosePDF);

        chosenProfile = new JTextField(VALIDATION_PROFILE_NOT_CHOSEN);
        chosenProfile.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 3;
        gbc.weighty = 1;
        gbc.gridwidth = 3;
        gbl.setConstraints(chosenProfile, gbc);
        this.add(chosenProfile);

        JButton chooseProfile = new JButton(CHOOSE_PROFILE_BUTTON_TEXT);
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.gridwidth = 1;
        gbl.setConstraints(chooseProfile, gbc);
        this.add(chooseProfile);

        result = new JLabel();
        result.setForeground(beforeValidationColor);
        gbc.fill = GridBagConstraints.CENTER;
        result.setHorizontalTextPosition(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 3;
        gbc.weighty = 1;
        gbc.gridwidth = 3;
        gbl.setConstraints(result, gbc);
        this.add(result);

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 3;
        gbc.weighty = 1;
        gbc.gridwidth = 3;
        gbl.setConstraints(progressBar, gbc);
        this.add(progressBar);

        final JButton validate = new JButton(VALIDATE_BUTTON_TEXT);
        validate.setEnabled(false);
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.gridwidth = 1;
        gbl.setConstraints(validate, gbc);
        this.add(validate);

        JPanel reports = new JPanel();
        reports.setBorder(BorderFactory.createTitledBorder(REPORT));
        reports.setLayout(new GridLayout(2, 3));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 4;
        gbc.weighty = 3;
        gbc.gridwidth = 4;
        gbl.setConstraints(reports, gbc);
        this.add(reports);

        LogoPanel xmlLogo = new LogoPanel(XML_LOGO_NAME, reports.getBackground(), 4);
        reports.add(xmlLogo);

        saveXML = new JButton(SAVE_REPORT_BUTTON_TEXT);
        saveXML.setEnabled(false);
        reports.add(saveXML);

        viewXML = new JButton(VIEW_REPORT_BUTTON_TEXT);
        viewXML.setEnabled(false);
        reports.add(viewXML);

        LogoPanel htmlLogo = new LogoPanel(HTML_LOGO_NAME, reports.getBackground(), 4);
        reports.add(htmlLogo);

        saveHTML = new JButton(SAVE_HTML_REPORT_BUTTON_TEXT);
        saveHTML.setEnabled(false);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        reports.add(saveHTML);

        viewHTML = new JButton(VIEW_HTML_REPORT_BUTTON_TEXT);
        viewHTML.setEnabled(false);
        reports.add(viewHTML);

        pdfChooser = new JFileChooser();
        pdfChooser.setCurrentDirectory(new File("."));
        pdfChooser.setAcceptAllFileFilterUsed(false);
        pdfChooser.setFileFilter(new FileNameExtensionFilter("pdf", "pdf"));

        xmlChooser = new JFileChooser();
        xmlChooser.setCurrentDirectory(new File("."));
        xmlChooser.setAcceptAllFileFilterUsed(false);
        xmlChooser.setFileFilter(new FileNameExtensionFilter("xml", "xml"));

        htmlChooser = new JFileChooser();
        htmlChooser.setCurrentDirectory(new File("."));
        htmlChooser.setAcceptAllFileFilterUsed(false);
        htmlChooser.setFileFilter(new FileNameExtensionFilter("html", "html"));


        choosePDF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int resultChoose = pdfChooser.showOpenDialog(CheckerPanel.this);
                if (resultChoose == JFileChooser.APPROVE_OPTION) {

                    if (!pdfChooser.getSelectedFile().exists()) {
                        JOptionPane.showMessageDialog(CheckerPanel.this, "Error. Selected file doesn't exist.", ERROR, JOptionPane.ERROR_MESSAGE);
                    } else if (!pdfChooser.getSelectedFile().getName().endsWith(".pdf")) {
                        JOptionPane.showMessageDialog(CheckerPanel.this, "Error. Selected file is not in PDF format.", ERROR, JOptionPane.ERROR_MESSAGE);
                    } else {

                        pdfFile = pdfChooser.getSelectedFile();
                        chosenPDF.setText(pdfFile.getAbsolutePath());
                        if (info != null) {
                            info = null;
                            result.setForeground(beforeValidationColor);
                            result.setText("");
                        }
                        if (xmlReport != null) {
                            xmlReport = null;
                            saveXML.setEnabled(false);
                            viewXML.setEnabled(false);
                            saveHTML.setEnabled(false);
                            viewHTML.setEnabled(false);
                        }
                        if (profile != null) {
                            validate.setEnabled(true);
                        }
                    }
                }
            }
        });

        chooseProfile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int resultChoose = xmlChooser.showOpenDialog(CheckerPanel.this);
                if (resultChoose == JFileChooser.APPROVE_OPTION) {
                    if (!xmlChooser.getSelectedFile().exists()) {
                        JOptionPane.showMessageDialog(CheckerPanel.this, "Error. Selected file doesn't exist.", ERROR, JOptionPane.ERROR_MESSAGE);
                    } else if (!xmlChooser.getSelectedFile().getName().endsWith(".xml")) {
                        JOptionPane.showMessageDialog(CheckerPanel.this, "Error. Selected file is not in XML format.", ERROR, JOptionPane.ERROR_MESSAGE);
                    } else {
                        profile = xmlChooser.getSelectedFile();
                        chosenProfile.setText(profile.getAbsolutePath());
                        if (info != null) {
                            info = null;
                            result.setForeground(beforeValidationColor);
                            result.setText("");
                        }
                        if (xmlReport != null) {
                            xmlReport = null;
                            saveXML.setEnabled(false);
                            viewXML.setEnabled(false);
                            saveHTML.setEnabled(false);
                            viewHTML.setEnabled(false);
                        }
                        if (pdfFile != null) {
                            validate.setEnabled(true);
                        }
                    }
                }
            }
        });

        validate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                progressBar.setVisible(true);
                result.setVisible(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                validate.setEnabled(false);

                validateWorker = new ValidateWorker(CheckerPanel.this, pdfFile, profile);

                startTimeOfValidation = System.currentTimeMillis();

                validateWorker.execute();
            }
        });

        saveXML.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (info == null) {
                    JOptionPane.showMessageDialog(CheckerPanel.this, "Validation hasn't been run.", ERROR, JOptionPane.ERROR_MESSAGE);
                } else {
                    xmlChooser.setSelectedFile(new File("xmlReport.xml"));
                    int resultChoose = xmlChooser.showSaveDialog(CheckerPanel.this);
                    if (resultChoose == JFileChooser.APPROVE_OPTION) {
                        File temp = xmlChooser.getSelectedFile();

                        if (!(temp.getPath().endsWith(".xml"))) {
                            temp = new File(temp.getPath() + ".xml");
                        }

                        try {
                            Files.copy(xmlReport.toPath(), temp.toPath());
                        } catch (IOException e1) {
                            JOptionPane.showMessageDialog(CheckerPanel.this, ERROR_IN_SAVING_XML_REPORT, ERROR, JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        viewXML.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (xmlReport == null) {
                    JOptionPane.showMessageDialog(CheckerPanel.this, "XML report hasn't been saved.", ERROR, JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        Desktop.getDesktop().open(xmlReport);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(CheckerPanel.this, "Some error in opening the XML report.", ERROR, JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        saveHTML.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (info == null) {
                    JOptionPane.showMessageDialog(CheckerPanel.this, "Validation hasn't been run.", ERROR, JOptionPane.ERROR_MESSAGE);
                } else {
                    htmlChooser.setSelectedFile(new File("htmlReport.html"));
                    int resultChoose = htmlChooser.showSaveDialog(CheckerPanel.this);
                    if (resultChoose == JFileChooser.APPROVE_OPTION) {
                        File temp = htmlChooser.getSelectedFile();

                        if (!(temp.getPath().endsWith(".html"))) {
                            temp = new File(temp.getPath() + ".html");
                        }

                        try {
                            Files.copy(htmlReport.toPath(), temp.toPath());
                        } catch (IOException e1) {
                            JOptionPane.showMessageDialog(CheckerPanel.this, ERROR_IN_SAVING_HTML_REPORT, ERROR, JOptionPane.ERROR_MESSAGE);
                        }
                        try {
                            File newImage = new File(temp.getParentFile(), image.getName());
                            Files.copy(image.toPath(), newImage.toPath());
                        } catch (IOException e1) {
                            JOptionPane.showMessageDialog(CheckerPanel.this, "Some error in saving logo image for HTML report.", ERROR, JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        viewHTML.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (htmlReport == null) {
                    JOptionPane.showMessageDialog(CheckerPanel.this, "HTML report hasn't been saved.", ERROR, JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        Desktop.getDesktop().open(htmlReport);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(CheckerPanel.this, "Some error in opening the HTML report.", ERROR, JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

    }

    /**
     * Method to notify panel that validation was done.
     */
    public void validationEnded(FeaturesCollection collection) {
        endTimeOfValidation = System.currentTimeMillis();

        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        progressBar.setVisible(false);

        try {
            info = validateWorker.get();
            if (info.getResult().isCompliant()) {
                result.setForeground(validationSuccessColor);
                result.setText(VALIDATION_OK);
            } else {
                result.setForeground(validationFailedColor);
                result.setText(VALIDATION_FALSE);
            }

            result.setVisible(true);
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(CheckerPanel.this, "Validation has interrupted.", ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        } catch (ExecutionException e) {
            JOptionPane.showMessageDialog(CheckerPanel.this, "Execution exception in validating.", ERROR, JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File dir = new File("./temp/");
            if (!dir.exists() && !dir.mkdir()) {
                throw new IOException("Can not create temporary directory.");
            }
            xmlReport = new File("./temp/tempXMLReport.xml");
            XMLReport.writeXMLReport(info, collection, xmlReport.getPath(), endTimeOfValidation - startTimeOfValidation);

            saveXML.setEnabled(true);
            viewXML.setEnabled(true);

            try {
                htmlReport = new File("./temp/tempHTMLReport.html");
                HTMLReport.wrightHTMLReport(htmlReport.getPath(), xmlReport, profile);

                if (image == null) {
                    image = new File("./temp/" + HTMLReport.getLogoImageName());
                }

                saveHTML.setEnabled(true);
                viewHTML.setEnabled(true);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(CheckerPanel.this, ERROR_IN_SAVING_HTML_REPORT, ERROR, JOptionPane.ERROR_MESSAGE);
            } catch (TransformerException e) {
                JOptionPane.showMessageDialog(CheckerPanel.this, ERROR_IN_SAVING_HTML_REPORT, ERROR, JOptionPane.ERROR_MESSAGE);
            }

        } catch (DatatypeConfigurationException e) {
            JOptionPane.showMessageDialog(CheckerPanel.this, ERROR_IN_SAVING_XML_REPORT, ERROR, JOptionPane.ERROR_MESSAGE);
        } catch (ParserConfigurationException e) {
            JOptionPane.showMessageDialog(CheckerPanel.this, ERROR_IN_SAVING_XML_REPORT, ERROR, JOptionPane.ERROR_MESSAGE);
        } catch (TransformerException e) {
            JOptionPane.showMessageDialog(CheckerPanel.this, ERROR_IN_SAVING_XML_REPORT, ERROR, JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(CheckerPanel.this, ERROR_IN_SAVING_XML_REPORT, ERROR, JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Method to notify panel that some error occurs at validating
     */
    public void errorInValidatingOccur() {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        progressBar.setVisible(false);
    }

}
