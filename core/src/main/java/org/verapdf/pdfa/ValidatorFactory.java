/**
 * 
 */
package org.verapdf.pdfa;

import org.verapdf.pdfa.config.ValidatorConfiguration;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.ProfileDirectory;

/**
 * Provisional factory interface for instantiation of veraPDF PDF/A Validator
 * and related types.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public interface ValidatorFactory {
    /**
     * @param validatorFlavour
     *            an enum instance that identifies the specific PDF/A flavour
     *            that will be checked by the returned {@link PDFAValidator}
     * @param config
     *            a {@link ValidatorConfiguration} instance that specifies the
     *            desired configuration of the returned {@link PDFAValidator}
     * @return a {@link PDFAValidator} that can be used to test whether data
     *         streams conform to the PDF/A specifcation associated with the
     *         {@link PDFAFlavour} validatorFlavour.
     */
    public PDFAValidator getValidatorInstance(PDFAFlavour validatorFlavour,
            ValidatorConfiguration config);

    /**
     * @return a {@link MetadataFixer} instance that can be used to repair
     *         simple metadata issues affecting PDF/A documents.
     */
    public MetadataFixer getFixerInstance();

    /**
     * @return a {@link ProfileDirectory} directory instance pre-populated with
     *         a Set of {@link ValidationProfile}s.
     */
    public ProfileDirectory getProfileDirectory();
}
