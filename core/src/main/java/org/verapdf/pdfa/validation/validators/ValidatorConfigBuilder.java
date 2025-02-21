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

import org.verapdf.extensions.ExtensionObjectType;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.EnumSet;
import java.util.logging.Level;

public class ValidatorConfigBuilder {

	private PDFAFlavour flavour = PDFAFlavour.NO_FLAVOUR;
	private PDFAFlavour defaultFlavour = PDFAFlavour.ARLINGTON1_4;
	private boolean recordPasses = false;
	private int maxFails = -1;
	private boolean debug = false;
	private boolean showErrorMessages = true;
	private boolean isLogsEnabled = false;
	private Level loggingLevel = Level.WARNING;
	private int maxNumberOfDisplayedFailedChecks = BaseValidator.DEFAULT_MAX_NUMBER_OF_DISPLAYED_FAILED_CHECKS;
	private String password = "";
	private boolean showProgress = false;
	private boolean nonPDFExtension = false;
	private EnumSet<ExtensionObjectType> enabledExtensions = EnumSet.noneOf(ExtensionObjectType.class);

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

	public ValidatorConfigBuilder nonPDFExtension(boolean nonPDFExtension) {
		this.nonPDFExtension = nonPDFExtension;
		return this;
	}

	public ValidatorConfigBuilder enabledExtensions(EnumSet<ExtensionObjectType> enabledExtensions) {
		this.enabledExtensions = enabledExtensions;
		return this;
	}

	public static ValidatorConfigBuilder defaultBuilder() {
		return new ValidatorConfigBuilder();
	}

	public ValidatorConfig build() {
		return ValidatorConfigImpl.fromValues(flavour, defaultFlavour, recordPasses, maxFails, debug, isLogsEnabled,
				loggingLevel, maxNumberOfDisplayedFailedChecks, showErrorMessages, password, showProgress, nonPDFExtension,
				enabledExtensions);
	}
}
