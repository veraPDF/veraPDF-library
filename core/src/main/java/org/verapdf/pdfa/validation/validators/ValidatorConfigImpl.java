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

import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 26 Oct 2016:00:11:25
 */
@XmlRootElement(name = "validatorConfig")
final class ValidatorConfigImpl implements ValidatorConfig {
	private final static ValidatorConfigImpl defaultConfig = new ValidatorConfigImpl();
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

	private ValidatorConfigImpl() {
		this(PDFAFlavour.NO_FLAVOUR, false, -1, false);
	}

	private ValidatorConfigImpl(final PDFAFlavour flavour, final boolean recordPasses, final int maxFails, boolean debug) {
		super();
		this.flavour = flavour;
		this.defaultFlavour = PDFAFlavour.PDFA_1_B;
		this.recordPasses = recordPasses;
		this.maxFails = maxFails;
		this.debug = debug;
	}

	private ValidatorConfigImpl(final PDFAFlavour flavour, final PDFAFlavour defaultFlavour, final boolean recordPasses,
	                            final int maxFails, boolean debug) {
		super();
		this.flavour = flavour;
		this.defaultFlavour = defaultFlavour;
		this.recordPasses = recordPasses;
		this.maxFails = maxFails;
		this.debug = debug;
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
		if (this.debug != other.debug) {
			return false;
		}
		return true;
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

	static ValidatorConfig fromValues(final PDFAFlavour flavour, final boolean recordPasses, final int maxFails, final boolean debug) {
		return new ValidatorConfigImpl(flavour, recordPasses, maxFails, debug);
	}

	static ValidatorConfig fromValues(final PDFAFlavour flavour, final PDFAFlavour defaultFlavour, final boolean recordPasses,
	                                  final int maxFails, final boolean debug) {
		return new ValidatorConfigImpl(flavour, defaultFlavour, recordPasses, maxFails, debug);
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
