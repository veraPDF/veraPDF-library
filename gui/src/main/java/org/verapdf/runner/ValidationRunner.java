package org.verapdf.runner;

import org.verapdf.core.ValidationException;
import org.verapdf.core.VeraPDFException;
import org.verapdf.model.ModelParser;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.validation.Validator;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
     * @throws IOException 
     */
    public static ValidationResult runValidation(InputStream toValidate) throws VeraPDFException, IOException {
        try (ModelParser loader = new ModelParser(toValidate)) {
            org.verapdf.model.baselayer.Object root;
            try {
                root = loader.getRoot();
            } catch (IOException e) {
                throw new VeraPDFException("IOException when parsing Validation Model.", e);
            }
            ValidationResult result = Validator.validate(PDFAFlavour.PDFA_1_B,
                    root, false);
            ValidationResults.toXml(result, System.out, Boolean.TRUE);
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
