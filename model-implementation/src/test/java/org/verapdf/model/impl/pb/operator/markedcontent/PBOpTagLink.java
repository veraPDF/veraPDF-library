package org.verapdf.model.impl.pb.operator.markedcontent;

import org.junit.Test;
import org.verapdf.model.impl.pb.cos.PBCosName;
import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpTagLink extends PBOperatorTest {

	@Test
	public void testTagLink() {
		testObject(PBOpMarkedContent.TAG, 1, PBCosName.COS_NAME_TYPE);
	}
}
