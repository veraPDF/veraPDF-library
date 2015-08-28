package org.verapdf.model.impl.pb.external;

import org.junit.Assert;
import org.junit.Test;
import org.verapdf.model.external.ICCProfile;
import org.verapdf.model.impl.BaseTest;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBoxICCProfileTest extends BaseTest {

	public static final String expectedDeviceClass = "mntr";
	public static final String expectedColorSpace = "RGB ";
	public static final double expectedVersion = 2.1;
	public static final Long expectedColorantsCount = Long.valueOf(3);

	@Test
	public void testDeviceClassMethod() {
		Assert.assertEquals(((ICCProfile) actual).getdeviceClass(), expectedDeviceClass);
	}

	@Test
	public void testColorSpaceMethod() {
		Assert.assertEquals(((ICCProfile) actual).getcolorSpace(), expectedColorSpace);
	}

	@Test
	public void testVersionMethod() {
		Assert.assertEquals(((ICCProfile) actual).getversion().doubleValue(), expectedVersion, 0.001);
	}

	@Test
	public void testNMethod() {
		Assert.assertEquals(((ICCProfile) actual).getN(), expectedColorantsCount);
	}

	@Test
	public void testIsValidMethod() {
		Assert.assertTrue(((ICCProfile) actual).getisValid().booleanValue());
	}
}
