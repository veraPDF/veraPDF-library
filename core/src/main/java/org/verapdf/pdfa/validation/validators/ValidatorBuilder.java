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
