/**
 * 
 */
package org.verapdf.pdfa.validation.validators;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 26 Oct 2016:00:11:25
 */
@XmlRootElement(name = "validatorConfig")
class ValidatorConfigImpl implements ValidatorConfig {
	private final static ValidatorConfigImpl DEFAULT = new ValidatorConfigImpl();
	@XmlAttribute
	private final boolean recordPasses;
	@XmlAttribute
	private final int maxFails;
	@XmlAttribute
	private final int maxFailsPerRule;
	@XmlAttribute
	private final PDFAFlavour flavour;

	private ValidatorConfigImpl() {
		this(false, -1, 100, PDFAFlavour.NO_FLAVOUR);
	}

	private ValidatorConfigImpl(final boolean recordPasses, final int maxFails, final int maxFailsPerRule,
			final PDFAFlavour flavour) {
		super();
		this.recordPasses = recordPasses;
		this.maxFails = maxFails;
		this.maxFailsPerRule = maxFailsPerRule;
		this.flavour = flavour;
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
	 * @see org.verapdf.pdfa.validation.validators.ValidatorConfig#getMaxFailsPerRule()
	 */
	@Override
	public int getMaxFailsPerRule() {
		return this.maxFailsPerRule;
	}

	/**
	 * @see org.verapdf.pdfa.validation.validators.ValidatorConfig#getFlavour()
	 */
	@Override
	public PDFAFlavour getFlavour() {
		return this.flavour;
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
		result = prime * result + this.maxFailsPerRule;
		result = prime * result + (this.recordPasses ? 1231 : 1237);
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
		if (this.maxFailsPerRule != other.maxFailsPerRule) {
			return false;
		}
		if (this.recordPasses != other.recordPasses) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ValidatorConfigImpl [recordPasses=" + this.recordPasses + ", maxFails=" + this.maxFails
				+ ", maxFailsPerRule=" + this.maxFailsPerRule + ", flavour=" + this.flavour + "]";
	}

	static ValidatorConfigImpl defaultInstance() {
		return DEFAULT;
	}

	static ValidatorConfigImpl fromValues(final boolean recordPasses, final int maxFails, final int maxFailsPerRule,
			final PDFAFlavour flavour) {
		return new ValidatorConfigImpl(recordPasses, maxFails, maxFailsPerRule, flavour);
	}
}
