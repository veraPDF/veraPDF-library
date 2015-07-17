package org.verapdf.model.impl.pb.external;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.verapdf.model.external.ICCProfile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxICCInputProfileTest extends PBoxICCProfileTest {

	public static final int EXPECTED_VERSION = 0x02;
	public static final int EXPECTED_SUBVERSION = 0x1A;

	private static ByteArrayInputStream inputStream;

	@BeforeClass
	public static void setUp() throws IOException, URISyntaxException {
		TYPE = "ICCInputProfile";
		ID = null;

		setUpActualObject();
	}

	private static void setUpActualObject() throws IOException {
		byte[] bytes = new byte[16];

		bytes[PBoxICCProfile.VERSION_BYTE] = EXPECTED_VERSION;
		bytes[PBoxICCProfile.SUBVERSION_BYTE] = EXPECTED_SUBVERSION;

		for (int i = PBoxICCProfile.DEVICE_CLASS_OFFSET; i < PBoxICCProfile.DEVICE_CLASS_OFFSET + 4; i++) {
			bytes[i] = (byte) PBoxICCProfileTest.expectedDeviceClass.charAt(i - PBoxICCProfile.DEVICE_CLASS_OFFSET);
		}

		inputStream = new ByteArrayInputStream(bytes);
		actual = new PBoxICCInputProfile(inputStream, Long.valueOf(3));
	}

	@Override
	@Test
	public void testColorSpaceMethod() {
		Assert.assertNull(((ICCProfile) actual).getcolorSpace());
	}

	@AfterClass
	public static void tearDown() throws IOException {
		TYPE = null;
		ID = null;
		actual = null;

		inputStream.close();
	}
}
