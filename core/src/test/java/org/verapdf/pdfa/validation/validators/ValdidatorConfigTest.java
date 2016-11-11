/**
 * 
 */
package org.verapdf.pdfa.validation.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 30 Oct 2016:00:46:37
 */

@SuppressWarnings("static-method")
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
		ValidatorConfig defaultInstance = ValidatorFactory.defaultConfig();
		assertTrue(defaultInstance == ValidatorFactory.defaultConfig());
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.validators.ValidatorConfigImpl#fromValues(org.verapdf.pdfa.flavours.PDFAFlavour, boolean, int, int)}.
	 */
	@Test
	public final void testFromValues() {
		ValidatorConfig defaultInstance = ValidatorFactory.defaultConfig();
		ValidatorConfig fromVals = ValidatorFactory.createConfig(defaultInstance.getFlavour(),
				defaultInstance.isRecordPasses(), defaultInstance.getMaxFails());
		assertTrue(fromVals.equals(defaultInstance));
		assertFalse(fromVals == defaultInstance);
	}

}
