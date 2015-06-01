package org.verapdf.gui;

import org.verapdf.validation.report.XMLValidationReport;
import org.verapdf.validation.report.model.ValidationInfo;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

/**
 * Panel with functionality for checker.
 * Created by bezrukov on 5/14/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class CheckerPanel extends JPanel {

    private final static String XML_LOGO_NAME = "xml-logo.png";
    private final static String HTML_LOGO_NAME = "html-logo.png";
    private final static String CHOOSE_PDF_BUTTON_TEXT = "Choose PDF";
    private final static String PDF_NOT_CHOSEN_TEXT = "PDF file not chosen";
    private final static String CHOOSE_PROFILE_BUTTON_TEXT = "Choose Profile";
    private final static String VALIDATION_PROFILE_NOT_CHOSEN = "Validation profile not chosen";
    private final static String VALIDATE_BUTTON_TEXT = "Validate";
    private final static String VALIDATION_OK = "PDF file satisfies the validation profile.";
    private final static String VALIDATION_FALSE = "PDF file does not satisfy the validation profile.";
    private final static String SAVE_REPORT_BUTTON_TEXT = "Save XML";
    private final static String VIEW_REPORT_BUTTON_TEXT = "View XML";
    private final static String SAVE_HTML_REPORT_BUTTON_TEXT = "Save HTML";
    private final static String VIEW_HTML_REPORT_BUTTON_TEXT = "View HTML";
    private final static String REPORT = "Report";


    private JFileChooser chooser;
    private File pdfFile;
    private File profile;
    private JTextField chosenPDF;
    private JTextField chosenProfile;
    private JLabel result;
    private ValidationInfo info;
    private File report;

    private JButton saveXML;
    private JButton viewXML;
    private JButton saveHTML;
    private JButton viewHTML;

    private JProgressBar progressBar;
    private ValidateWorker validateWorker;

    private Color beforeValidationColor = Color.BLACK;
    private Color validationSuccessColor = new Color(0,180,0);
    private Color validationFailedColor = new Color(180,0,0);


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

        LogoPanel xmlLogo = new LogoPanel(XML_LOGO_NAME,reports.getBackground(),0);
        reports.add(xmlLogo);

        saveXML = new JButton(SAVE_REPORT_BUTTON_TEXT);
        saveXML.setEnabled(false);
        reports.add(saveXML);

        viewXML = new JButton(VIEW_REPORT_BUTTON_TEXT);
        viewXML.setEnabled(false);
        reports.add(viewXML);

        LogoPanel htmlLogo = new LogoPanel(HTML_LOGO_NAME,reports.getBackground(),0);
        reports.add(htmlLogo);

        saveHTML = new JButton(SAVE_HTML_REPORT_BUTTON_TEXT);
        saveHTML.setEnabled(false);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        reports.add(saveHTML);

        viewHTML = new JButton(VIEW_HTML_REPORT_BUTTON_TEXT);
        viewHTML.setEnabled(false);
        reports.add(viewHTML);

        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setAcceptAllFileFilterUsed(false);



        choosePDF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooser.setFileFilter(new FileNameExtensionFilter("pdf", "pdf"));
                int resultChoose = chooser.showOpenDialog(CheckerPanel.this);
                if (resultChoose == JFileChooser.APPROVE_OPTION) {
                    pdfFile = chooser.getSelectedFile();
                    chosenPDF.setText(pdfFile.getPath());
                    if (info != null) {
                        info = null;
                        result.setForeground(beforeValidationColor);
                        result.setText("");
                    }
                    if (report != null) {
                        report = null;
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
        });

        chooseProfile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooser.setFileFilter(new FileNameExtensionFilter("xml", "xml"));
                int resultChoose = chooser.showOpenDialog(CheckerPanel.this);
                if (resultChoose == JFileChooser.APPROVE_OPTION) {
                    profile = chooser.getSelectedFile();
                    chosenProfile.setText(profile.getPath());
                    if (info != null) {
                        info = null;
                        result.setForeground(beforeValidationColor);
                        result.setText("");
                    }
                    if (report != null) {
                        report = null;
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
        });

        validate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                progressBar.setVisible(true);
                result.setVisible(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                validate.setEnabled(false);

                validateWorker = new ValidateWorker(CheckerPanel.this, pdfFile, profile);
                validateWorker.execute();
            }
        });

        saveXML.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (info == null) {
                    JOptionPane.showMessageDialog(CheckerPanel.this, "Validation hasn't been run.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    chooser.setSelectedFile(new File("report.xml"));
                    int resultChoose = chooser.showSaveDialog(CheckerPanel.this);
                    if (resultChoose == JFileChooser.APPROVE_OPTION) {
                        File temp = chooser.getSelectedFile();

                        if (!(temp.getPath().endsWith(".xml")))
                            temp = new File(temp.getPath() + ".xml");

                        try {
                            Files.copy(report.toPath(), temp.toPath());
                        } catch (IOException e1) {
                            JOptionPane.showMessageDialog(CheckerPanel.this, "Some error in saving the report.", "Error", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        });

        viewXML.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (report == null){
                    JOptionPane.showMessageDialog(CheckerPanel.this, "Report hasn't been saved.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    try {
                        Desktop.getDesktop().open(report);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(CheckerPanel.this, "Some error in opening the report.", "Error", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

    }

    /**
     * Method to notify panel that validation was done.
     */
    public void validationEnded(){
        try {
            info = validateWorker.get();
            if (info.getResult().isCompliant()) {
                result.setForeground(validationSuccessColor);
                result.setText(VALIDATION_OK);
            } else {
                result.setForeground(validationFailedColor);
                result.setText(VALIDATION_FALSE);
            }
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(CheckerPanel.this, "Validation has interrupted.", "Error", JOptionPane.INFORMATION_MESSAGE);
        } catch (ExecutionException e) {
            JOptionPane.showMessageDialog(CheckerPanel.this, "Execution exception in validating.", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        progressBar.setVisible(false);
        result.setVisible(true);

        try {
            File dir = new File("./temp/");
            dir.mkdir();
            report = new File("./temp/tempReport.xml");
            XMLValidationReport.writeXMLValidationReport(info, report.getPath());

            saveXML.setEnabled(true);
            viewXML.setEnabled(true);

        } catch (Exception e1) {
            JOptionPane.showMessageDialog(CheckerPanel.this, "Some error in saving the report.", "Error", JOptionPane.INFORMATION_MESSAGE);
        }

    }

}
