package org.verapdf.model.impl.pb.operator.markedcontent;

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
public class PBOp_BMCTest extends PBOpTagPropertiesLink {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_BMC.OP_BMC_TYPE, null);
	}

	@Override
	@Test
	public void testPropertiesLink() {
		List<? extends Object> linkedObjects = actual.getLinkedObjects(PBOpMarkedContent.PROPERTIES);
		Assert.assertEquals(0, linkedObjects.size());
	}
}
