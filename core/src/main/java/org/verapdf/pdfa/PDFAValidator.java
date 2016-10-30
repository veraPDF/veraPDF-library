package org.verapdf.pdfa;

import org.verapdf.core.ValidationException;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;

/**
 * A PDFAValidator performs a series of checks on PDF/A documents to verify that
 * the document conforms to a specific PDF/A flavour.
 *
 * Note that the interface makes no provision for configuration of a validator
 * instance. This is left to the implementer although the veraPDF library API
 * provides a {@link Validators} class. This is designed to allow
 * immutable validator instances, meaning there is no methods provided to change
 * the ValidationProfile, or the pre-configured settings.
 *
 * @author Maksim Bezrukov
 */
public interface PDFAValidator {

    /**
     * Returns the complete {@link ValidationProfile} enforced by this PDFAValidator.
     *
     * @return this PDFAValidator instance's ValiationProfile
     */
    public ValidationProfile getProfile();

    /**
     * This method validates an InputStream, presumably believed to be a PDF/A
     * document of a specific flavour that matches the ValidationProfile
     * supported by the PDFAValidator instance.
     *
     * @param toValidate
     *            a {@link java.io.InputStream} to be validated
     * @return a {@link ValidationResult} containing the result of valdiation
     *         and details of failed checks and possibly passed checks,
     *         dependant upon configuration.
     * @throws ValidationException 
     * @throws ModelParsingException 
     * @throws IllegalArgumentException
     *             if the toValidate parameter is null PDFAValidationException
     *             if the validation process fails
     */
    public ValidationResult validate(PDFParser toValidate) throws ValidationException;
}
