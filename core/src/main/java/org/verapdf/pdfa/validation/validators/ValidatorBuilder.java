/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf.pdfa.validation.validators;

import org.verapdf.pdfa.PDFAValidator;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.profiles.Profiles;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;

public class ValidatorBuilder {
	private ValidationProfile profile = null;
	private int maxNumberOfDisplayedFailedChecks = BaseValidator.DEFAULT_MAX_NUMBER_OF_DISPLAYED_FAILED_CHECKS;
	private boolean logPassedChecks = false;
	private boolean showErrorMessages = true;
	private boolean showProgress = false;
	private int maxFails = -1;

	public ValidatorBuilder config(ValidatorConfig config) {
		return maxFails(config.getMaxFails()).logPassedChecks(config.isRecordPasses())
				.maxNumberOfDisplayedFailedChecks(config.getMaxNumberOfDisplayedFailedChecks())
				.showErrorMessages(config.showErrorMessages()).showProgress(config.getShowProgress());
	}

	public ValidatorBuilder profile(ValidationProfile profile) {
		if (profile == null) {
			throw new IllegalArgumentException("Parameter (ValidationProfile profile) cannot be null.");
		}
		this.profile = profile;
		return this;
	}

	public ValidatorBuilder flavour(PDFAFlavour flavour) {
		if (flavour == null) {
			throw new IllegalArgumentException("Parameter (PDFAFlavour flavour) cannot be null.");
		}
		this.profile = Profiles.getVeraProfileDirectory().getValidationProfileByFlavour(flavour);
		return this;
	}

	public ValidatorBuilder maxNumberOfDisplayedFailedChecks(int maxNumberOfDisplayedFailedChecks) {
		this.maxNumberOfDisplayedFailedChecks = maxNumberOfDisplayedFailedChecks;
		return this;
	}

	public ValidatorBuilder logPassedChecks(boolean logPassedChecks) {
		this.logPassedChecks = logPassedChecks;
		return this;
	}

	public ValidatorBuilder showErrorMessages(boolean showErrorMessages) {
		this.showErrorMessages = showErrorMessages;
		return this;
	}

	public ValidatorBuilder showProgress(boolean showProgress) {
		this.showProgress = showProgress;
		return this;
	}

	public ValidatorBuilder maxFails(int maxFails) {
		this.maxFails = maxFails;
		return this;
	}

	public static ValidatorBuilder defaultBuilder() {
		return new ValidatorBuilder();
	}

	public PDFAValidator build() {
		if (maxFails > 0) {
			return new FastFailValidator(profile, logPassedChecks, maxFails, showErrorMessages, showProgress,
					maxNumberOfDisplayedFailedChecks);
		}
		return new BaseValidator(profile, maxNumberOfDisplayedFailedChecks, logPassedChecks, showErrorMessages, showProgress);
	}
}
