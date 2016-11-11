/**
 * 
 */
package org.verapdf.pdfa.validation.profiles;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@SuppressWarnings("static-method")
public class ReferenceImplTest {
	private final static String DEFAULT_REFERENCE_STRING = "Reference [specification=specification, clause=clause]";

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.ReferenceImpl#equals(java.lang.Object)}.
	 */
	@Test
	public final void testEqualsObject() {
		EqualsVerifier.forClass(ReferenceImpl.class).verify();
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.ReferenceImpl#toString()}.
	 */
	@Test
	public final void testToString() {
		assertTrue(Profiles.defaultReference().toString().equals(DEFAULT_REFERENCE_STRING));
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.ReferenceImpl#fromValues(String, String)}.
	 */
	@Test
	public final void testFromValues() {
		// Get an equivalent to the default instance
		Reference reference = Profiles.referenceFromValues("specification", "clause");
		Reference defaultInstance = Profiles.defaultReference();
		// Equivalent is NOT the same object as default instance
		assertFalse(reference == defaultInstance);
		// But it is equal
		assertTrue(reference.equals(defaultInstance));
	}

	/**
	 * Test method for
	 * {@link org.verapdf.pdfa.validation.ReferenceImpl#fromReference(org.verapdf.pdfa.validation.Reference)}.
	 */
	@Test
	public final void testFromReference() {
		Reference defaultInstance = Profiles.defaultReference();
		// Get an equivalent to the default instance
		Reference reference = ReferenceImpl.fromValues(defaultInstance.getSpecification(), defaultInstance.getClause());
		// Equivalent is NOT the same object as default instance
		assertFalse(reference == defaultInstance);
		// But it is equal
		assertTrue(reference.equals(defaultInstance));
	}
}
