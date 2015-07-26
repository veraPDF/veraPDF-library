package org.verapdf.gui;

import org.apache.log4j.Logger;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.tools.FeaturesCollection;
import org.verapdf.gui.tools.GUIConstants;
import org.verapdf.model.ModelLoader;
import org.verapdf.validation.logic.Validator;
import org.verapdf.validation.report.model.ValidationInfo;

import javax.swing.*;

import java.io.File;

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
        collection = null;
        org.verapdf.model.baselayer.Object root;

        ModelLoader loader = new ModelLoader(pdf.getPath());


        try {
            root = loader.getRoot();

            try {
                info = Validator.validate(root, profile, false);
            } catch (Exception e) {
                parent.errorInValidatingOccur(GUIConstants.ERROR_IN_VALIDATING, e);
            }

        } catch (Exception e) {
            parent.errorInValidatingOccur(GUIConstants.ERROR_IN_PARSING, e);
        }

        try {
            collection = PBFeatureParser.getFeaturesCollection(loader.getPDDocument());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Some error in creating features collection.", GUIConstants.ERROR, JOptionPane.ERROR_MESSAGE);
            LOGGER.error("Exception in creating features collection: ", e);
        }

        return info;
    }

    @Override
    protected void done() {
        parent.validationEnded(collection);
    }

}
