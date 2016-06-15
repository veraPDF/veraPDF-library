package org.verapdf.pdfa.results;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

/**
 * @author Maksim Bezrukov
 */
public class MetadataFixerResultTest {

	/**
	 * Test method for {@link org.verapdf.pdfa.results.MetadataFixerResultImpl#hashCode()}.
	 */
	@Test
	public final void testHashCodeAndEquals() {
		EqualsVerifier.forClass(MetadataFixerResultImpl.class).suppress(Warning.NULL_FIELDS).verify();
	}
}
