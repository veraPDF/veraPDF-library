package org.verapdf.gui;

import org.verapdf.exceptions.featurereport.FeatureValueException;
import org.verapdf.features.pb.PBFeatureParser;
import org.verapdf.features.tools.FeaturesCollection;
import org.verapdf.model.ModelLoader;
import org.verapdf.validation.logic.Validator;
import org.verapdf.validation.report.model.ValidationInfo;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Validates PDF in a new threat.
 *
 * @author Maksim Bezrukov
 */
public class ValidateWorker extends SwingWorker<ValidationInfo, Integer> {

    private static final String ERROR = "Error";

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
        org.verapdf.model.baselayer.Object root = null;

        ModelLoader loader = new ModelLoader(pdf.getPath());


        try {
            root = loader.getRoot();
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(parent, "Some error in parsing pdf.", ERROR, JOptionPane.ERROR_MESSAGE);
            parent.errorInValidatingOccur();
        }

        try {
            info = Validator.validate(root, profile, false);
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(parent, "Some error in validating.", ERROR, JOptionPane.ERROR_MESSAGE);
            parent.errorInValidatingOccur();
        }

        try {
            collection = PBFeatureParser.getFeaturesCollection(loader.getPDDocument());
        } catch (FeatureValueException e) {
            JOptionPane.showMessageDialog(parent, e.getMessage(), ERROR, JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Some error in creating features collection.", ERROR, JOptionPane.ERROR_MESSAGE);
        }

        return info;
    }

    @Override
    protected void done() {
        parent.validationEnded(collection);
    }

}
