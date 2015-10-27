package org.verapdf.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.verapdf.core.ValidationException;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.tools.FeaturesCollection;
import org.verapdf.gui.config.Config;
import org.verapdf.gui.tools.GUIConstants;
import org.verapdf.metadata.fixer.MetadataFixer;
import org.verapdf.metadata.fixer.MetadataFixerResult;
import org.verapdf.metadata.fixer.impl.pb.FixerConfigImpl;
import org.verapdf.metadata.fixer.utils.FixerConfig;
import org.verapdf.model.ModelLoader;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.Validator;
import org.verapdf.report.XMLReport;

/**
 * Validates PDF in a new threat.
 *
 * @author Maksim Bezrukov
 */
class ValidateWorker extends SwingWorker<ValidationResult, Integer> {

    private static final Logger LOGGER = Logger.getLogger(ValidateWorker.class);
    private static final String TEMP_PREFIX = "veraPDF-";

    private File pdf;
    private PDFAFlavour flavour;
    private CheckerPanel parent;
    private Config settings;
    private File xmlReport = null;
    private File htmlReport = null;
    private int flag;
    private boolean isFixMetadata;

    ValidateWorker(CheckerPanel parent, File pdf, PDFAFlavour flavour,
            Config settings, int flag, boolean isFixMetadata) {
        if (pdf == null || !pdf.isFile() || !pdf.canRead()) {
            throw new IllegalArgumentException(
                    "PDF file doesn't exist or it can not be read");
        }
        this.parent = parent;
        this.pdf = pdf;
        this.flavour = flavour;
        this.settings = settings;
        this.flag = flag;
        this.isFixMetadata = isFixMetadata;
    }

    @Override
    protected ValidationResult doInBackground() {
        this.xmlReport = null;
        this.htmlReport = null;
        ValidationResult result = null;
        FeaturesCollection collection = null;

        try (ModelLoader loader = new ModelLoader(new FileInputStream(this.pdf.getPath()))) {

            if ((this.flag & 1) == 1) {
                result = runValidator(loader.getRoot(), this.settings.isShowPassedRules());

                if (this.isFixMetadata) {
                    FixerConfig fixerConfig = FixerConfigImpl.getFixerConfig(
                            loader.getPDDocument(), result);
                    Path path = this.settings.getFixMetadataPathFolder();
                    MetadataFixerResult fixerResult;

                    if (!path.toString().trim().isEmpty()) {
                        // TODO : what we need do with fixing result?
                        fixerResult = MetadataFixer.fixMetadata(this.settings
                                .getFixMetadataPathFolder().toFile(), this.pdf.getPath(), this.settings
                                .getMetadataFixerPrefix(), fixerConfig);
                    } else {
                        fixerResult = MetadataFixer.fixMetadata(
                                this.pdf,
                                this.settings.getMetadataFixerPrefix(), fixerConfig);
                    }
                }
            }
            if ((this.flag & (1 << 1)) == (1 << 1)) {
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
            writeReport(result, collection);
        } catch (IOException e) {
            this.parent
                    .errorInValidatingOccur(GUIConstants.ERROR_IN_PARSING, e);
        }

        return result;
    }

    private ValidationResult runValidator(
            org.verapdf.model.baselayer.Object root, boolean logSuccess) {
        try {
            // TODO : FIX SETTINGS
            return Validator.validate(this.flavour, root, logSuccess);
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

    private void writeReport(ValidationResult result,
            FeaturesCollection collection) {
        if (result != null || collection != null) {
            try {
                this.xmlReport = File
                        .createTempFile(TEMP_PREFIX, ".xml");
                try (OutputStream fos = new FileOutputStream(this.xmlReport)) {
                    ValidationResults.toXml(result, fos, Boolean.TRUE);
                    XMLReport.writeXMLReport(collection, fos);
                } catch (JAXBException e) {
                    LOGGER.error("Marshalling error serialising JAXB type.", e);
                }
                if (result != null) {
                    try {
                        this.htmlReport = File.createTempFile(
                                TEMP_PREFIX, ".html");
                        this.htmlReport.deleteOnExit();
                        //HTMLReport.writeHTMLReport(this.htmlReport.getPath(),
                        //        result);

                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(this.parent,
                                GUIConstants.ERROR_IN_SAVING_HTML_REPORT,
                                GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
                        LOGGER.error("Exception saving the HTML report", e);
                        this.htmlReport = null;
                    }
                }

            } catch (DatatypeConfigurationException
                    | ParserConfigurationException | IOException
                    | TransformerException e) {
                JOptionPane.showMessageDialog(this.parent,
                        GUIConstants.ERROR_IN_SAVING_XML_REPORT,
                        GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
                LOGGER.error("Exception saving the XML report", e);
                this.xmlReport = null;
            }
        }
    }
}
