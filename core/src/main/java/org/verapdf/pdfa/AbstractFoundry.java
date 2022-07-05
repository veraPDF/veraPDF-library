/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
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

public abstract class AbstractFoundry implements VeraPDFFoundry {
	static PDFAFlavour defaultFlavour = PDFAFlavour.PDFA_1_B;

	@Override
	public PDFAValidator createValidator(ValidatorConfig config) {
		if (config.getMaxFails() > 0) {
			return createFailFastValidator(config.getFlavour(), config.getMaxFails());
		}
		return createValidator(config.getFlavour(), config.getMaxNumberOfDisplayedFailedChecks(),
				config.isRecordPasses(), config.showErrorMessages());
	}

	@Override
	public PDFAValidator createValidator(ValidatorConfig config, PDFAFlavour flavour) {
		if (config.getMaxFails() > 0) {
			return createFailFastValidator(flavour, config.getMaxFails());
		}
		return createValidator(flavour, config.getMaxNumberOfDisplayedFailedChecks(),
				config.isRecordPasses(), config.showErrorMessages());
	}

	@Override
	public PDFAValidator createValidator(ValidatorConfig config, ValidationProfile profile) {
		if (config.getMaxFails() > 0) {
			return createFailFastValidator(profile, config.getMaxFails());
		}
		return createValidator(profile, config.getMaxNumberOfDisplayedFailedChecks(),
				config.isRecordPasses(), config.showErrorMessages());
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
	public PDFAValidator createValidator(PDFAFlavour flavour, int maxNumberOfDisplayedFailedChecks,
										 boolean logSuccess, boolean showErrorMessages) {
		return ValidatorFactory.createValidator(flavour, maxNumberOfDisplayedFailedChecks, logSuccess, showErrorMessages);
	}

	@Override
	public PDFAValidator createValidator(ValidationProfile profile, int maxNumberOfDisplayedFailedChecks,
										 boolean logSuccess, boolean showErrorMessages) {
		return ValidatorFactory.createValidator(profile, maxNumberOfDisplayedFailedChecks, logSuccess, showErrorMessages);
	}

	@Override
	public PDFAValidator createFailFastValidator(PDFAFlavour flavour, int maxFailures) {
		return ValidatorFactory.createValidator(flavour, maxFailures);
	}

	@Override
	public PDFAValidator createFailFastValidator(ValidationProfile profile, int maxFailures) {
		return ValidatorFactory.createValidator(profile, maxFailures);
	}

	@Override
	public PDFAFlavour defaultFlavour() {
		return defaultFlavour;
	}

	@Override
	public void close() {

	}
}
