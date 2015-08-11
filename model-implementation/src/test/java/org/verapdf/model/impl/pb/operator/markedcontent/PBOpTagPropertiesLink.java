package org.verapdf.model.impl.pb.operator.markedcontent;

import org.junit.Test;
import org.verapdf.model.impl.pb.cos.PBCosDict;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpTagPropertiesLink extends PBOpTagLink {

	@Test
	public void testPropertiesLink() {
		testLinkToDictionary(PBOpMarkedContent.PROPERTIES, PBCosDict.COS_DICT_TYPE, Long.valueOf(1));
	}
}
