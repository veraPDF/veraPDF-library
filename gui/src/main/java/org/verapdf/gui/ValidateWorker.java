package org.verapdf.gui;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.verapdf.core.ValidationException;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.tools.FeaturesCollection;
import org.verapdf.gui.config.Config;
import org.verapdf.gui.tools.GUIConstants;
import org.verapdf.metadata.fixer.MetadataFixer;
import org.verapdf.metadata.fixer.MetadataFixerResultImpl;
import org.verapdf.metadata.fixer.impl.pb.FixerConfigImpl;
import org.verapdf.metadata.fixer.utils.FileGenerator;
import org.verapdf.metadata.fixer.utils.FixerConfig;
import org.verapdf.model.ModelLoader;
import org.verapdf.pdfa.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.pdfa.validation.Validator;
import org.verapdf.report.HTMLReport;
import org.verapdf.report.XMLReport;
import org.xml.sax.SAXException;

/**
 * Validates PDF in a new threat.
 *
 * @author Maksim Bezrukov
 */
class ValidateWorker extends SwingWorker<ValidationResult, Integer> {

    private static final Logger LOGGER = Logger.getLogger(ValidateWorker.class);

    private File pdf;
    private ValidationProfile profile;
    private CheckerPanel parent;
    private Config settings;
    private File xmlReport = null;
    private File htmlReport = null;
    private int flag;
    private boolean isFixMetadata;

    private long startTimeOfValidation;
    private long endTimeOfValidation;

    ValidateWorker(CheckerPanel parent, File pdf, ValidationProfile profile,
            Config settings, int flag, boolean isFixMetadata) {
        if (pdf == null || !pdf.isFile() || !pdf.canRead()) {
            throw new IllegalArgumentException(
                    "PDF file doesn't exist or it can not be read");
        }
        if (profile == null) {
            throw new IllegalArgumentException(
                    "Profile doesn't exist or it can not be read");
        }
        this.parent = parent;
        this.pdf = pdf;
        this.profile = profile;
        this.settings = settings;
        this.flag = flag;
        this.isFixMetadata = isFixMetadata;
    }

    @Override
    protected ValidationResult doInBackground() {
        xmlReport = null;
        htmlReport = null;
        ValidationResult info = null;
        FeaturesCollection collection = null;

        startTimeOfValidation = System.currentTimeMillis();

        try (ModelLoader loader = new ModelLoader(new FileInputStream(this.pdf.getPath()))) {

            if ((flag & 1) == 1) {
                info = runValidator(loader.getRoot());

                if (this.isFixMetadata) {
                    this.fixMetadata(info, loader);
                }
            }
            if ((flag & (1 << 1)) == (1 << 1)) {
                try {
                    collection = PBFeatureParser.getFeaturesCollection(loader
                            .getPDDocument());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this.parent,
                            "Some error in creating features collection.",
                            GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
                    LOGGER.error("Exception in creating features collection: ",
                            e);
                }
            }
            endTimeOfValidation = System.currentTimeMillis();
            writeReports(info, collection);
        } catch (IOException e) {
            this.parent
                    .errorInValidatingOccur(GUIConstants.ERROR_IN_PARSING, e);
        }

        return info;
    }

    private void fixMetadata(ValidationResult info, ModelLoader loader)
            throws IOException {
        FixerConfig fixerConfig = FixerConfigImpl.getFixerConfig(
                loader.getPDDocument(), info);
        Path path = settings.getFixMetadataPathFolder();
        File tempFile = File.createTempFile("fixedTempFile", ".pdf");
        tempFile.deleteOnExit();
        OutputStream tempOutput = new BufferedOutputStream(
                new FileOutputStream(tempFile));
        MetadataFixerResult fixerResult = MetadataFixer.fixMetadata(
                tempOutput, fixerConfig);
         if (fixerResult.getRepairStatus().equals(
                MetadataFixerResult.RepairStatus.SUCCESS)
                || fixerResult.getRepairStatus().equals(
                        MetadataFixerResult.RepairStatus.ID_REMOVED)) {
            File resFile;
            boolean flag = true;
            while (flag) {
                if (!path.toString().trim().isEmpty()) {
                    resFile = FileGenerator.createOutputFile(settings
                            .getFixMetadataPathFolder().toFile(), this.pdf.getName(), settings
                            .getMetadataFixerPrefix());
                } else {
                    resFile = FileGenerator.createOutputFile(this.pdf,
                            settings.getMetadataFixerPrefix());
                }

                try {
                    Files.copy(tempFile.toPath(), resFile.toPath());
                    flag = false;
                } catch (FileAlreadyExistsException e) {
                    LOGGER.error(e);
                }
            }
        }
    }

    private ValidationResult runValidator(org.verapdf.model.baselayer.Object root) {
        try {
            return Validator.validate(this.profile, root, false);
        } catch (ValidationException e) {

            this.parent.errorInValidatingOccur(
                    GUIConstants.ERROR_IN_VALIDATING, e);
        }
        return null;
    }

    @Override
    protected void done() {
        this.parent.validationEnded(this.xmlReport, this.htmlReport);
    }

    private void writeReports(ValidationResult info, FeaturesCollection collection) {
        if (info != null || collection != null) {
            try {
                xmlReport = File
                        .createTempFile("veraPDF-tempXMLReport", ".xml");
                xmlReport.deleteOnExit();
                XMLReport.writeXMLReport(info, collection, new FileInputStream(xmlReport));
                if (info != null) {
                    try {
                        htmlReport = File.createTempFile(
                                "veraPDF-tempHTMLReport", ".html");
                        htmlReport.deleteOnExit();
                        HTMLReport.writeHTMLReport(htmlReport.getPath(),
                                xmlReport, profile);

                    } catch (IOException | TransformerException e) {
                        JOptionPane.showMessageDialog(this.parent,
                                GUIConstants.ERROR_IN_SAVING_HTML_REPORT,
                                GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
                        LOGGER.error("Exception saving the HTML report", e);
                        htmlReport = null;
                    }
                }

            } catch (DatatypeConfigurationException
                    | ParserConfigurationException | IOException
                    | TransformerException e) {
                JOptionPane.showMessageDialog(this.parent,
                        GUIConstants.ERROR_IN_SAVING_XML_REPORT,
                        GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
                LOGGER.error("Exception saving the XML report", e);
                xmlReport = null;
            }
        }
    }
}
