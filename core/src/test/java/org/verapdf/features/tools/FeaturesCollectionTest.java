package org.verapdf.features.tools;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

/**
 * @author Maksim Bezrukov
 */
public class FeaturesCollectionTest {

	/**
	 * Test method for {@link org.verapdf.features.tools.FeaturesCollection#hashCode()}.
	 */
	@Test
	public final void testHashCodeAndEquals() {
		EqualsVerifier.forClass(FeaturesCollection.class).suppress(Warning.NULL_FIELDS).verify();
	}
}
