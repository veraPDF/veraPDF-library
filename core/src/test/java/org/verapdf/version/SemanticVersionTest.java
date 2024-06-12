/**
 * 
 */
package org.verapdf.version;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 25 May 2017:22:31:17
 */

@SuppressWarnings("static-method")
public class SemanticVersionTest {
	private static final String v1_0_0 = "1.0.0"; //$NON-NLS-1$
	private static final String[] v1_0_0_strings = { "1", "0", "0" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	private static final int[] v1_0_0_ints = { 1, 0, 0 };
	private static final int[] v1_0_0_shortints = { 1, 0 };
	private static final int[] v1_0_0_longints = { 1, 0, 1, 1 };
	private static final String v1_0_0pdf = "1.0.0-PDFBOX"; //$NON-NLS-1$
	private static final String v1_0_0snap = "1.0.0-SNAPSHOT"; //$NON-NLS-1$
	/**
	 * Test method for {@link VersionNumberImpl#hashCode()}.
	 */
	@Test
	public final void testHashCodeAndEquals() {
		EqualsVerifier.forClass(VersionNumberImpl.class).verify();
	}

	/**
	 * Test method for {@link VersionNumberImpl#fromString(java.lang.String)}.
	 */
	@Test
	public final void testVersionNumberFromString() {
		SemanticVersionNumber vNum = VersionNumberImpl.fromString(v1_0_0);
		SemanticVersionNumber pdfNum = VersionNumberImpl.fromString(v1_0_0pdf);
		assertEquals(vNum, pdfNum);
	}

	/**
	 * Test method for {@link VersionNumberImpl#fromString(java.lang.String)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testVersionNumberFromNullString() {
		VersionNumberImpl.fromString(null);
	}


	/**
	 * Test method for {@link VersionNumberImpl#fromString(java.lang.String)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testVersionNumberFromEmptyString() {
		VersionNumberImpl.fromString(""); //$NON-NLS-1$
	}

	/**
	 * Test method for {@link VersionNumberImpl#fromStrings(java.lang.String[])}.
	 */
	@Test
	public final void testVersionNumberImplStringArray() {
		SemanticVersionNumber vNum = VersionNumberImpl.fromString(v1_0_0);
		SemanticVersionNumber stringsNum = VersionNumberImpl.fromStrings(v1_0_0_strings);
		assertEquals(vNum, stringsNum);
	}

	/**
	 * Test method for {@link VersionNumberImpl#fromStrings(java.lang.String[])}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testVersionNumberImplStringArrayNull() {
		VersionNumberImpl.fromStrings(null);
	}

	/**
	 * Test method for {@link VersionNumberImpl#fromStrings(java.lang.String[])}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testVersionNumberImplStringArrayBad() {
		String [] badStrings = { "1", "stuff", "0" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		VersionNumberImpl.fromStrings(badStrings);
	}

	/**
	 * Test method for {@link VersionNumberImpl#fromInts(int[])}.
	 */
	@Test
	public final void testVersionNumberImplIntArray() {
		SemanticVersionNumber vNum = VersionNumberImpl.fromString(v1_0_0);
		SemanticVersionNumber intsNum = VersionNumberImpl.fromInts(v1_0_0_ints);
		assertEquals(vNum, intsNum);
	}

	/**
	 * Test method for {@link VersionNumberImpl#fromInts(int[])}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testVersionNumberImplIntArrayNull() {
		VersionNumberImpl.fromInts(null);
	}

	/**
	 * Test method for {@link VersionNumberImpl#fromInts(int[])}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testVersionNumberImplIntArrayShort() {
		VersionNumberImpl.fromInts(v1_0_0_shortints);
	}

	/**
	 * Test method for {@link VersionNumberImpl#fromInts(int[])}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testVersionNumberImplIntArrayLong() {
		VersionNumberImpl.fromInts(v1_0_0_longints);
	}

	/**
	 * Test method for {@link VersionNumberImpl#VersionNumberImpl(int, int, int)}.
	 */
	@Test
	public final void testVersionNumberImplIntIntInt() {
		SemanticVersionNumber vNum = VersionNumberImpl.fromString(v1_0_0);
		SemanticVersionNumber intsNum = VersionNumberImpl.fromInts(1, 0, 0);
		assertEquals(vNum, intsNum);
	}

	/**
	 * Test method for {@link VersionNumberImpl#VersionNumberImpl(int, int, int)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testVersionNumberImplIntIntIntMinusMajor() {
		VersionNumberImpl.fromInts(-1, 0, 0);
	}

	/**
	 * Test method for {@link VersionNumberImpl#VersionNumberImpl(int, int, int)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testVersionNumberImplIntIntIntMinusMinor() {
		VersionNumberImpl.fromInts(1, -1, 0);
	}

	/**
	 * Test method for {@link VersionNumberImpl#VersionNumberImpl(int, int, int)}.
	 */
	@Test(expected=IllegalArgumentException.class)
	public final void testVersionNumberImplIntIntIntMinusRev() {
		VersionNumberImpl.fromInts(1, 0, -1);
	}

	/**
	 * Test method for {@link VersionNumberImpl#getVersionString()}.
	 */
	@Test
	public final void testGetVersionString() {
		SemanticVersionNumber vNum = VersionNumberImpl.fromString(v1_0_0);
		assertEquals(v1_0_0, vNum.getVersionString());
		SemanticVersionNumber pdfNum = VersionNumberImpl.fromString(v1_0_0pdf);
		assertEquals(v1_0_0, pdfNum.getVersionString());
		SemanticVersionNumber snapNum = VersionNumberImpl.fromString(v1_0_0snap);
		assertEquals(v1_0_0, snapNum.getVersionString());
	}

	/**
	 * Test method for {@link VersionNumberImpl#compareTo(SemanticVersionNumber)}.
	 */
	@Test
	public final void testCompareTo() {
		SemanticVersionNumber vNum = VersionNumberImpl.fromString(v1_0_0);
		SemanticVersionNumber verNum = VersionNumberImpl.fromString(v1_0_0pdf);
		assertEquals(vNum, verNum);
		assertEquals(0, vNum.compareTo(verNum));
		SemanticVersionNumber lessThan = VersionNumberImpl.fromInts(0,90,100);
		assertNotEquals(vNum, lessThan);
		assertTrue(vNum.compareTo(lessThan) > 0);
		assertTrue(lessThan.compareTo(vNum) < 0);
		SemanticVersionNumber greaterThan = VersionNumberImpl.fromInts(1,0,1);
		assertNotEquals(vNum, greaterThan);
		assertTrue(vNum.compareTo(greaterThan) < 0);
		assertTrue(greaterThan.compareTo(vNum) > 0);
		greaterThan = VersionNumberImpl.fromInts(2,0,0);
		assertNotEquals(vNum, greaterThan);
		assertTrue(vNum.compareTo(greaterThan) < 0);
		assertTrue(greaterThan.compareTo(vNum) > 0);
		greaterThan = VersionNumberImpl.fromInts(1,2,0);
		assertNotEquals(vNum, greaterThan);
		assertTrue(vNum.compareTo(greaterThan) < 0);
		assertTrue(greaterThan.compareTo(vNum) > 0);
	}

}
