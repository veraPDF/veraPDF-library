package org.verapdf.model.impl.pb.pd.pattern;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.baselayer.Object;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDShadingPatternTest extends PBoxPDPatternTest {

	@BeforeClass
	public static void setUp() throws URISyntaxException, IOException {
		setUp(PBoxPDShadingPattern.SHADING_PATTERN_TYPE, "PSHADE");
	}

	@Test
	public void testShadingLink() {
		List<? extends Object> shadings = actual
				.getLinkedObjects(PBoxPDShadingPattern.SHADING);
		Assert.assertEquals(1, shadings.size());
		Assert.assertEquals(PBoxPDShading.SHADING_TYPE,
				shadings.get(0).getObjectType());
	}

}
