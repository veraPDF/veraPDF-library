package org.verapdf.model.impl.pb.pd.font;

import org.junit.Test;
import org.verapdf.model.impl.BaseTest;

/**
 * @author Timur Kamalov
 */
public abstract class PBoxPDFontTest extends BaseTest {

	protected static final String FILE_RELATIVE_PATH = "pd/Fonts.pdf";

	@Test
	public abstract void testSubtypeMethod();

	@Test
	public abstract void testBaseFont();

}
