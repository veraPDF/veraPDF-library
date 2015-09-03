package org.verapdf.model.impl.pb.operator.xobject;

import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;
import org.verapdf.model.impl.pb.pd.images.PBoxPDXForm;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_DoTest extends PBOperatorTest {

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		setUpOperatorsList(PBOp_Do.OP_DO_TYPE, null);
	}

	@Test
	public void testXObjectLink() {
		testObject(PBOp_Do.X_OBJECT, 1, PBoxPDXForm.X_FORM_TYPE);
	}

}
