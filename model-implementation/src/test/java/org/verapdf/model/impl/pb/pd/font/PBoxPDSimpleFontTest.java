package org.verapdf.model.impl.pb.pd.font;

import org.junit.Test;

/**
 * @author Timur Kamalov
 */
public abstract class PBoxPDSimpleFontTest extends PBoxPDFontTest {

	@Test
	public abstract void testWidthsSize();

	@Test
	public abstract void testLastChar();

	@Test
	public abstract void testFirstChar();

	@Test
	public abstract void testIsStandard();

	@Test
	public abstract void testEncoding();

}
