package org.verapdf.model.impl.pb.pd.font;

import org.junit.Test;

/**
 * @author Timur Kamalov
 */
public abstract class PBoxPDType0FontTest extends PBoxPDFontTest {

	@Test
	public abstract void testAreRegistryOrderingCompatible();

	@Test
	public abstract void testEncoding();

	@Test
	public abstract void testDescendantFonts();

}
