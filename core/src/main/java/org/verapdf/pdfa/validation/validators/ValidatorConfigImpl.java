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
package org.verapdf.pdfa.validation.validators;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.verapdf.pdfa.Foundries;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.Objects;
import java.util.logging.Level;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 26 Oct 2016:00:11:25
 */
@XmlRootElement(name = "validatorConfig")
final class ValidatorConfigImpl implements ValidatorConfig {
	private static final ValidatorConfigImpl defaultConfig = new ValidatorConfigImpl();
	private final String password;
	@XmlAttribute
	private final PDFAFlavour flavour;
	@XmlAttribute
	private final PDFAFlavour defaultFlavour;
	@XmlAttribute
	private final boolean recordPasses;
	@XmlAttribute
	private final int maxFails;
	@XmlAttribute
	private final boolean debug;
	@XmlAttribute
	private final boolean showErrorMessages;
	@XmlAttribute
	private final boolean isLogsEnabled;
	@XmlAttribute
	private final String loggingLevel;
	@XmlAttribute
	private final int maxNumberOfDisplayedFailedChecks;

	private ValidatorConfigImpl() {
		this(PDFAFlavour.NO_FLAVOUR, false, -1, false, false, Level.WARNING, "");
	}

	private ValidatorConfigImpl(final PDFAFlavour flavour, final boolean recordPasses, final int maxFails, boolean debug,
								boolean isLogsEnabled, Level loggingLevel, String password) {
		this(flavour, PDFAFlavour.PDFA_1_B, recordPasses, maxFails, debug, isLogsEnabled, loggingLevel,
		     BaseValidator.DEFAULT_MAX_NUMBER_OF_DISPLAYED_FAILED_CHECKS, !Foundries.defaultParserIsPDFBox(), password);
	}

	private ValidatorConfigImpl(final PDFAFlavour flavour, final PDFAFlavour defaultFlavour, final boolean recordPasses,
	                            final int maxFails, final boolean debug, final boolean isLogsEnabled,
								final Level loggingLevel, final int maxNumberOfDisplayedFailedChecks,
								final boolean showErrorMessages, final String password) {
		super();
		this.flavour = flavour;
		this.defaultFlavour = defaultFlavour;
		this.recordPasses = recordPasses;
		this.maxFails = maxFails;
		this.debug = debug;
		this.isLogsEnabled = isLogsEnabled;
		this.loggingLevel = loggingLevel.toString();
		this.maxNumberOfDisplayedFailedChecks = maxNumberOfDisplayedFailedChecks;
		this.showErrorMessages = showErrorMessages && !Foundries.defaultParserIsPDFBox();
		this.password = password;
	}

	/**
	 * @see org.verapdf.pdfa.validation.validators.ValidatorConfig#isRecordPasses()
	 */
	@Override
	public boolean isRecordPasses() {
		return this.recordPasses;
	}

	/**
	 * @see org.verapdf.pdfa.validation.validators.ValidatorConfig#getMaxFails()
	 */
	@Override
	public int getMaxFails() {
		return this.maxFails;
	}

	/**
	 * @see org.verapdf.pdfa.validation.validators.ValidatorConfig#getFlavour()
	 */
	@Override
	public PDFAFlavour getFlavour() {
		return this.flavour;
	}

	@Override
	public PDFAFlavour getDefaultFlavour() {
		return this.defaultFlavour;
	}

	@Override
	public boolean isDebug() {
		return this.debug;
	}

	@Override
	public boolean isLogsEnabled() {
		return this.isLogsEnabled;
	}

	@Override
	public boolean showErrorMessages() {
		return this.showErrorMessages && !Foundries.defaultParserIsPDFBox();
	}

	@Override
	public Level getLoggingLevel() {
		return Level.parse(this.loggingLevel);
	}

	@Override
	public int getMaxNumberOfDisplayedFailedChecks() {
		return maxNumberOfDisplayedFailedChecks;
	}

	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.flavour == null) ? 0 : this.flavour.hashCode());
		result = prime * result + this.maxFails;
		result = prime * result + (this.recordPasses ? 1231 : 1237);
		result = prime * result + (this.debug ? 1231 : 1237);
		result = prime * result + (this.isLogsEnabled ? 1231 : 1237);
		result = prime * result + (this.showErrorMessages ? 1231 : 1237);
		result = prime * result + ((this.loggingLevel == null) ? 0 : this.loggingLevel.hashCode());
		result = prime * result + ((this.password == null) ? 0 : this.password.hashCode());
		result = prime * result + this.maxNumberOfDisplayedFailedChecks;
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ValidatorConfigImpl)) {
			return false;
		}
		ValidatorConfigImpl other = (ValidatorConfigImpl) obj;
		if (this.flavour != other.flavour) {
			return false;
		}
		if (this.maxFails != other.maxFails) {
			return false;
		}
		if (this.recordPasses != other.recordPasses) {
			return false;
		}
		if (this.maxNumberOfDisplayedFailedChecks != other.maxNumberOfDisplayedFailedChecks) {
			return false;
		}
		if (this.debug != other.debug) {
			return false;
		}
		if (this.showErrorMessages != other.showErrorMessages) {
			return false;
		}
		if (this.isLogsEnabled != other.isLogsEnabled) {
			return false;
		}
		if (!Objects.equals(this.password, other.password)) {
			return false;
		}
		return Objects.equals(this.loggingLevel, other.loggingLevel);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ValidatorConfigImpl [recordPasses=" + this.recordPasses + ", maxFails=" + this.maxFails + ", flavour="
				+ this.flavour + "]";
	}

	static ValidatorConfig defaultInstance() {
		return defaultConfig;
	}

	static ValidatorConfig fromValues(final PDFAFlavour flavour, final boolean recordPasses, final int maxFails, final boolean debug,
									  final boolean isLogsEnabled, final Level loggingLevel, final String password) {
		return new ValidatorConfigImpl(flavour, recordPasses, maxFails, debug, isLogsEnabled, loggingLevel, password);
	}

	static ValidatorConfig fromValues(final PDFAFlavour flavour, final PDFAFlavour defaultFlavour, final boolean recordPasses,
	                                  final int maxFails, final boolean debug, final boolean isLogsEnabled,
									  final Level loggingLevel, final int maxNumberOfDisplayedFailedChecks,
									  final boolean showErrorMessages, final String password) {
		return new ValidatorConfigImpl(flavour, defaultFlavour, recordPasses, maxFails, debug, isLogsEnabled,
				loggingLevel, maxNumberOfDisplayedFailedChecks, showErrorMessages, password);
	}

	static class Adapter extends XmlAdapter<ValidatorConfigImpl, ValidatorConfig> {
		@Override
		public ValidatorConfig unmarshal(ValidatorConfigImpl validationConfigImpl) {
			return validationConfigImpl;
		}

		@Override
		public ValidatorConfigImpl marshal(ValidatorConfig validationResult) {
			return (ValidatorConfigImpl) validationResult;
		}
	}
}
