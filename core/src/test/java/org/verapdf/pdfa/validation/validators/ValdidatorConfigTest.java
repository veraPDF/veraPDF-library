/**
 * 
 */
package org.verapdf.pdfa.validation.validators;

import static org.junit.Assert.*;

import org.junit.Test;
import org.verapdf.features.config.FeaturesConfig;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 30 Oct 2016:00:46:37
 */

public class ValdidatorConfigTest {

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.validators.ValidatorConfigImpl#hashCode()}.
	 */
	@Test
	public final void testHashCodeAndEquals() {
		EqualsVerifier.forClass(ValidatorConfigImpl.class).verify();
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.validators.ValidatorConfigImpl#defaultInstance()}.
	 */
	@Test
	public final void testDefaultInstance() {
		ValidatorConfig defaultInstance = ValidatorFactory.defaultValidatorConfig();
		assertTrue(defaultInstance == ValidatorFactory.defaultValidatorConfig());
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.validators.ValidatorConfigImpl#fromValues(org.verapdf.pdfa.flavours.PDFAFlavour, boolean, int, int)}.
	 */
	@Test
	public final void testFromValues() {
		ValidatorConfig defaultInstance = ValidatorFactory.defaultValidatorConfig();
		ValidatorConfig fromVals = ValidatorFactory.createValidatorConfig(defaultInstance.getFlavour(),
				defaultInstance.isRecordPasses(), defaultInstance.getMaxFails(), defaultInstance.getMaxFailsPerRule());
		assertTrue(fromVals.equals(defaultInstance));
		assertFalse(fromVals == defaultInstance);
	}

}
