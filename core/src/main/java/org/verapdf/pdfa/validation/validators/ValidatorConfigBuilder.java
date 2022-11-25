package org.verapdf.pdfa.validation.validators;

import org.verapdf.pdfa.Foundries;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.logging.Level;

public class ValidatorConfigBuilder {

	private PDFAFlavour flavour = PDFAFlavour.NO_FLAVOUR;
	private PDFAFlavour defaultFlavour = PDFAFlavour.PDFA_1_B;
	private boolean recordPasses = false;
	private int maxFails = -1;
	private boolean debug = false;
	private boolean showErrorMessages = !Foundries.defaultParserIsPDFBox();
	private boolean isLogsEnabled = false;
	private Level loggingLevel = Level.WARNING;
	private int maxNumberOfDisplayedFailedChecks = BaseValidator.DEFAULT_MAX_NUMBER_OF_DISPLAYED_FAILED_CHECKS;
	private String password = "";
	private boolean showProgress = false;

	public ValidatorConfigBuilder password(String password) {
		this.password = password;
		return this;
	}

	public ValidatorConfigBuilder flavour(PDFAFlavour flavour) {
		this.flavour = flavour;
		return this;
	}

	public ValidatorConfigBuilder defaultFlavour(PDFAFlavour defaultFlavour) {
		this.defaultFlavour = defaultFlavour;
		return this;
	}

	public ValidatorConfigBuilder recordPasses(boolean recordPasses) {
		this.recordPasses = recordPasses;
		return this;
	}

	public ValidatorConfigBuilder maxFails(int maxFails) {
		this.maxFails = maxFails;
		return this;
	}

	public ValidatorConfigBuilder debug(boolean debug) {
		this.debug = debug;
		return this;
	}

	public ValidatorConfigBuilder showErrorMessages(boolean showErrorMessages) {
		this.showErrorMessages = showErrorMessages;
		return this;
	}

	public ValidatorConfigBuilder isLogsEnabled(boolean isLogsEnabled) {
		this.isLogsEnabled = isLogsEnabled;
		return this;
	}

	public ValidatorConfigBuilder loggingLevel(Level loggingLevel) {
		this.loggingLevel = loggingLevel;
		return this;
	}

	public ValidatorConfigBuilder maxNumberOfDisplayedFailedChecks(int maxNumberOfDisplayedFailedChecks) {
		this.maxNumberOfDisplayedFailedChecks = maxNumberOfDisplayedFailedChecks;
		return this;
	}

	public ValidatorConfigBuilder showProgress(boolean showProgress) {
		this.showProgress = showProgress;
		return this;
	}

	public static ValidatorConfigBuilder defaultBuilder() {
		return new ValidatorConfigBuilder();
	}

	public ValidatorConfig build() {
		return ValidatorConfigImpl.fromValues(flavour, defaultFlavour, recordPasses, maxFails, debug, isLogsEnabled,
				loggingLevel, maxNumberOfDisplayedFailedChecks, showErrorMessages, password, showProgress);
	}
}
