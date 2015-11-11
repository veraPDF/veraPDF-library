package org.verapdf.pdfa;

import java.io.InputStream;

import org.verapdf.pdfa.config.ValidatorConfiguration;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.validation.ValidationProfile;

/**
 * A PDFAValidator performs a series of checks on PDF/A documents to verify that
 * the document conforms to a specific PDF/A flavour.
 *
 * Note that the interface makes no provision for configuration of a validator
 * instance. This is left to the implementer although the veraPDF library API
 * provides a {@link ValidatorFactory} interface. This is designed to allow
 * immutable validator instances, meaning there is no methods provided to change
 * the ValidationProfile, or the pre-configured settings.
 *
 * @author Maksim Bezrukov
 */
public interface PDFAValidator {

    /**
     * Returns the ID of the ValidationProfile used by this instance. The ID
     * returned as a String value that also clearly identifies the PDF/A flavour
     * supported by this PDFAValidator as can be seen in the list below:
     * <ul>
     * <li>1a</li>
     * <li>1b</li>
     * <li>2a</li>
     * <li>2b</li>
     * <li>2u</li>
     * <li>3a</li>
     * <li>3b</li>
     * <li>3u</li>
     * </ul>
     *
     * @return the ID of the validating profile as a String
     */
    public String getProfileID();

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
     * @throws IllegalArgumentException
     *             if the toValidate parameter is null PDFAValidationException
     *             if the validation process fails
     */
    public ValidationResult validate(InputStream toValidate);

    /**
     * Returns a {@link ValidatorConfiguration} that holds the configuration of
     * this Validator instance.
     *
     * @return the ValidatorConfiguration for this validator.
     */
    public ValidatorConfiguration getConfiguration();
}
