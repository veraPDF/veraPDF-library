package org.verapdf.gui;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.verapdf.exceptions.validationlogic.MultiplyGlobalVariableNameException;
import org.verapdf.exceptions.validationlogic.NullLinkException;
import org.verapdf.exceptions.validationlogic.NullLinkNameException;
import org.verapdf.exceptions.validationlogic.NullLinkedObjectException;
import org.verapdf.exceptions.validationprofileparser.IncorrectImportPathException;
import org.verapdf.exceptions.validationprofileparser.MissedHashTagException;
import org.verapdf.exceptions.validationprofileparser.WrongSignatureException;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.tools.FeaturesCollection;
import org.verapdf.gui.tools.GUIConstants;
import org.verapdf.model.ModelLoader;
import org.verapdf.validation.logic.Validator;
import org.verapdf.validation.report.model.ValidationInfo;
import org.xml.sax.SAXException;

/**
 * Validates PDF in a new threat.
 *
 * @author Maksim Bezrukov
 */
public class ValidateWorker extends SwingWorker<ValidationInfo, Integer> {

    private static final Logger LOGGER = Logger.getLogger(ValidateWorker.class);

    private File pdf;
    private File profile;
    private CheckerPanel parent;
    private FeaturesCollection collection;

    /**
     * Creates new validate worker
     *
     * @param parent  - parent component
     * @param pdf     - pdf file for validating
     * @param profile - validation profile for validating
     */
    public ValidateWorker(CheckerPanel parent, File pdf, File profile) {
        this.parent = parent;
        this.pdf = pdf;
        this.profile = profile;
    }

    @Override
    protected ValidationInfo doInBackground() {
        ValidationInfo info = null;
        this.collection = null;

        ModelLoader loader = new ModelLoader(this.pdf.getPath());


        try {
            org.verapdf.model.baselayer.Object root = loader.getRoot();
            info = runValidator(root);
        } catch (IOException e) {
            this.parent.errorInValidatingOccur(GUIConstants.ERROR_IN_PARSING, e);
        }


        try {
            this.collection = PBFeatureParser.getFeaturesCollection(loader.getPDDocument());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this.parent, "Some error in creating features collection.", GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
            LOGGER.error("Exception in creating features collection: ", e);
        }

        return info;
    }
    
    private ValidationInfo runValidator(org.verapdf.model.baselayer.Object root) {
        try {
            return Validator.validate(root, this.profile, false);
        } catch (IOException | IncorrectImportPathException | NullLinkNameException | NullLinkException | NullLinkedObjectException | MissedHashTagException | WrongSignatureException | MultiplyGlobalVariableNameException | ParserConfigurationException | SAXException | XMLStreamException e) {
            this.parent.errorInValidatingOccur(GUIConstants.ERROR_IN_VALIDATING, e);
        }
        return null;
    }

    @Override
    protected void done() {
        this.parent.validationEnded(this.collection);
    }

}
