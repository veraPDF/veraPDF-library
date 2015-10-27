package org.verapdf.runner;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.verapdf.config.VeraPdfTaskConfig;
import org.verapdf.core.ValidationException;
import org.verapdf.core.VeraPDFException;
import org.verapdf.metadata.fixer.MetadataFixer;
import org.verapdf.metadata.fixer.MetadataFixerResult;
import org.verapdf.metadata.fixer.impl.pb.FixerConfigImpl;
import org.verapdf.metadata.fixer.utils.FixerConfig;
import org.verapdf.model.ModelLoader;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.Validator;

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
     * @throws VeraPDFException 
     */
    public static ValidationResult runValidation(VeraPdfTaskConfig config) throws VeraPDFException {
        try (ModelLoader loader = new ModelLoader(config.getInput().getPath())) {
            org.verapdf.model.baselayer.Object root;
            try {
                root = loader.getRoot();
            } catch (IOException e) {
                throw new VeraPDFException("IOException when parsing Validation Model.", e);
            }
            ValidationResult result = Validator.validate(PDFAFlavour.PDFA_1_B,
                    root);
            ValidationResults.toXml(result, System.out, Boolean.TRUE);
            if (config.getIncrementalSave() != null
                    && !config.getIncrementalSave().trim().isEmpty()) {
                FixerConfig fixerConfig;
                try {
                    fixerConfig = FixerConfigImpl.getFixerConfig(loader.getPDDocument(), result);
                } catch (IOException e) {
                    throw new VeraPDFException("IOException when parsing Validation Model.", e);
                }
                // TODO : what we need do with fixing result?
                MetadataFixerResult fixerResult =
                MetadataFixer.fixMetadata(loader.getFile(), fixerConfig);
            }
            return result;
            // TODO: Better exception handling, we need a policy and this isn't
            // it.
            // Carl to think a little harder and tidy up, it's not a new idea
            // I'm after,
            // more a case of ensuring we use the best of 2 methods.
        } catch (ValidationException | FileNotFoundException | JAXBException e) {
            throw new VeraPDFException("Exception when validating.", e);
        }
    }

}
