package org.verapdf.runner;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.verapdf.config.VeraPdfTaskConfig;
import org.verapdf.core.ValidationException;
import org.verapdf.metadata.fixer.MetadataFixer;
import org.verapdf.metadata.fixer.MetadataFixerResult;
import org.verapdf.metadata.fixer.impl.pb.FixerConfigImpl;
import org.verapdf.metadata.fixer.utils.FixerConfig;
import org.verapdf.model.ModelLoader;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.validation.logic.Validator;

public class ValidationRunner {

    private ValidationRunner() {
        // Disable default constructor
    }

    /**
     * Helper method to run validation
     * 
     * @param config
     *            validation task configuration
     * @return the validation result
     * @throws JAXBException
     */
    public static ValidationResult runValidation(VeraPdfTaskConfig config)
            throws JAXBException {
        try (ModelLoader loader = new ModelLoader(config.getInput().getPath())) {
            org.verapdf.model.baselayer.Object root = loader.getRoot();
            ValidationResult info = Validator.validate(PDFAFlavour.PDFA_1_B,
                    root);
            if (config.getIncrementalSave() != null
                    && !config.getIncrementalSave().trim().isEmpty()) {
                FixerConfig fixerConfig =
                FixerConfigImpl.getFixerConfig(loader.getPDDocument(), info);
                // TODO : what we need do with fixing result?
                MetadataFixerResult fixerResult =
                MetadataFixer.fixMetadata(loader.getFile(), fixerConfig);
            }
            return info;
            // TODO: Better exception handling, we need a policy and this isn't
            // it.
            // Carl to think a little harder and tidy up, it's not a new idea
            // I'm after,
            // more a case of ensuring we use the best of 2 methods.
        } catch (IOException | ValidationException e) {
            // error while parsing validation profile
            e.printStackTrace();
            System.err.println(e);
        }
        return null;
    }

}
