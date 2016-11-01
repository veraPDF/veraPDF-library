/**
 *
 */
package org.verapdf.pdfa;

import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;
import org.verapdf.pdfa.validation.validators.ValidatorConfig;
import org.verapdf.pdfa.validation.validators.ValidatorFactory;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 26 Oct 2016:21:25:17
 */

abstract class AbstractFoundry implements VeraPDFFoundry {

	@Override
	public PDFAValidator createValidator(ValidatorConfig config) {
		if (config.getMaxFails() > 0)
			return createFailFastValidator(config.getFlavour(), config.getMaxFails());
		return createValidator(config.getFlavour(), config.isRecordPasses());
	}

	@Override
	public PDFAValidator createValidator(ValidatorConfig config, PDFAFlavour flavour) {
		if (config.getMaxFails() > 0)
			return createFailFastValidator(flavour, config.getMaxFails());
		return createValidator(flavour, config.isRecordPasses());
	}

	@Override
	public PDFAValidator createValidator(ValidatorConfig config, ValidationProfile profile) {
		if (config.getMaxFails() > 0)
			return createFailFastValidator(profile, config.getMaxFails());
		return createValidator(profile, config.isRecordPasses());
	}

	@Override
	public PDFAValidator createValidator(PDFAFlavour flavour, boolean logSuccess) {
		return ValidatorFactory.createValidator(flavour, logSuccess);
	}

	@Override
	public PDFAValidator createValidator(ValidationProfile profile, boolean logSuccess) {
		return ValidatorFactory.createValidator(profile, logSuccess);
	}

	@Override
	public PDFAValidator createFailFastValidator(PDFAFlavour flavour, int maxFailures) {
		return ValidatorFactory.createValidator(flavour, maxFailures);
	}

	@Override
	public PDFAValidator createFailFastValidator(ValidationProfile profile, int maxFailures) {
		return ValidatorFactory.createValidator(profile, maxFailures);
	}
}
