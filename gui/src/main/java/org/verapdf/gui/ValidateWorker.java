package org.verapdf.gui;

import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.tools.FeaturesCollection;
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

    private static final String ERROR = "Error";
    private static final String ERROR_IN_PARSING = "Some error in parsing pdf.";
    private static final String ERROR_IN_VALIDATING = "Some error in validating.";

    private File pdf;
    private File profile;
    private CheckerPanel parent;
    private ValidationInfo info;
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
        info = null;
        collection = null;
        org.verapdf.model.baselayer.Object root;

        ModelLoader loader = new ModelLoader(pdf.getPath());


        try {
            root = loader.getRoot();

            try {
                info = Validator.validate(root, profile, false);
            } catch (Exception e) {
                parent.errorInValidatingOccur(ERROR_IN_VALIDATING);
            }

        } catch (Exception e) {
            parent.errorInValidatingOccur(ERROR_IN_PARSING);
        }

        try {
            collection = PBFeatureParser.getFeaturesCollection(loader.getPDDocument());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Some error in creating features collection.", ERROR, JOptionPane.ERROR_MESSAGE);
        }

        return info;
    }

    @Override
    protected void done() {
        parent.validationEnded(collection);
    }

}
