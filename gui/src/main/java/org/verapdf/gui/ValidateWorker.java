package org.verapdf.gui;

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

    private File pdf;
    private File profile;
    private CheckerPanel parent;

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
        ValidationInfo result = null;
        org.verapdf.model.baselayer.Object root = null;

        try {
            root = ModelLoader.getRoot(pdf.getPath());
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(parent, "Some error in parsing pdf.", "Error", JOptionPane.ERROR_MESSAGE);
            parent.errorInValidatingOccur();
        }

        try {
            result = Validator.validate(root, profile, false);
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(parent, "Some error in validating.", "Error", JOptionPane.ERROR_MESSAGE);
            parent.errorInValidatingOccur();
        }

        return result;
    }

    @Override
    protected void done() {
        parent.validationEnded();
    }

}
