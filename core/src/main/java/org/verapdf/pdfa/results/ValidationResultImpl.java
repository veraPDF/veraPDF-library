/**
 * 
 */
package org.verapdf.pdfa.results;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.TestAssertion.Status;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@XmlRootElement(name = "validationResult")
final class ValidationResultImpl implements ValidationResult {
	private final static ValidationResultImpl DEFAULT = new ValidationResultImpl();
	@XmlAttribute
	private final PDFAFlavour flavour;
	@XmlAttribute
	private final int totalAssertions;
	@XmlElementWrapper
	@XmlElement(name = "assertion")
	private final Set<TestAssertion> assertions;
	@XmlAttribute
	private final boolean isCompliant;

	private ValidationResultImpl() {
		this(PDFAFlavour.NO_FLAVOUR, Collections.<TestAssertion>emptySet(), false);
	}

	private ValidationResultImpl(final PDFAFlavour flavour, final Set<TestAssertion> assertions,
			final boolean isCompliant) {
		this(flavour, assertions, isCompliant, assertions.size());
	}

	private ValidationResultImpl(final PDFAFlavour flavour, final Set<TestAssertion> assertions,
			final boolean isCompliant, int totalAssertions) {
		super();
		this.flavour = flavour;
		this.assertions = new HashSet<>(assertions);
		this.isCompliant = isCompliant;
		this.totalAssertions = totalAssertions;
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public boolean isCompliant() {
		return this.isCompliant;
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public PDFAFlavour getPDFAFlavour() {
		return this.flavour;
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public int getTotalAssertions() {
		return this.totalAssertions;
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public Set<TestAssertion> getTestAssertions() {
		return Collections.unmodifiableSet(this.assertions);
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.assertions == null) ? 0 : this.assertions.hashCode());
		result = prime * result + ((this.flavour == null) ? 0 : this.flavour.hashCode());
		result = prime * result + (this.isCompliant ? 1231 : 1237);
		result = prime * result + this.totalAssertions;
		return result;
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (!(obj instanceof ValidationResult))
			return false;
		ValidationResult other = (ValidationResult) obj;
		if (this.assertions == null) {
			if (other.getTestAssertions() != null)
				return false;
		} else if (other.getTestAssertions() == null)
			return false;
		else if (!this.assertions.equals(other.getTestAssertions()))
			return false;
		if (this.flavour != other.getPDFAFlavour())
			return false;
		if (this.isCompliant != other.isCompliant())
			return false;
		if (this.totalAssertions != other.getTotalAssertions())
			return false;
		return true;
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public String toString() {
		return "ValidationResult [flavour=" + this.flavour + ", totalAssertions=" + this.totalAssertions //$NON-NLS-1$ //$NON-NLS-2$
				+ ", assertions=" + this.assertions + ", isCompliant=" + this.isCompliant + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	static ValidationResultImpl defaultInstance() {
		return DEFAULT;
	}

	static ValidationResultImpl fromValues(final PDFAFlavour flavour, final Set<TestAssertion> assertions,
			final boolean isCompliant, final int totalChecks) {
		return new ValidationResultImpl(flavour, assertions, isCompliant, totalChecks);
	}

	static ValidationResultImpl fromValidationResult(ValidationResult toConvert) {
		Set<TestAssertion> assertions = toConvert.getTestAssertions();
		return fromValues(toConvert.getPDFAFlavour(), assertions, toConvert.isCompliant(),
				toConvert.getTotalAssertions());
	}

	static ValidationResultImpl stripPassedTests(ValidationResult toStrip) {
		Set<TestAssertion> assertions = toStrip.getTestAssertions();
		return fromValues(toStrip.getPDFAFlavour(), stripPassedTests(assertions),
				toStrip.isCompliant(), toStrip.getTotalAssertions());
	}

	static class Adapter extends XmlAdapter<ValidationResultImpl, ValidationResult> {
		@Override
		public ValidationResult unmarshal(ValidationResultImpl validationResultImpl) {
			return validationResultImpl;
		}

		@Override
		public ValidationResultImpl marshal(ValidationResult validationResult) {
			return (ValidationResultImpl) validationResult;
		}
	}

	static Set<TestAssertion> stripPassedTests(final Set<TestAssertion> toStrip) {
		Set<TestAssertion> strippedSet = new HashSet<>();
		for (TestAssertion test : toStrip) {
			if (test.getStatus() != Status.PASSED)
				strippedSet.add(test);
		}
		return strippedSet;
	}
}
