package org.verapdf.model.impl.pb.pd.images;

import org.junit.Assert;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.BaseTest;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBoxPDAbstractXObjectTest extends BaseTest {

	protected static final String FILE_RELATIVE_PATH = "pd/InteractiveObjects.pdf";

	@Test
	public abstract void testSubtypeMethod();

	@Test
	public void testSMaskLink() {
		List<? extends Object> sMask = actual.getLinkedObjects(PBoxPDXObject.S_MASK);
		Assert.assertEquals(0, sMask.size());
	}

	@Test
	public void testOPILink() {
		List<? extends Object> opi = actual.getLinkedObjects(PBoxPDXObject.OPI);
		Assert.assertEquals(0, opi.size());
	}

}
