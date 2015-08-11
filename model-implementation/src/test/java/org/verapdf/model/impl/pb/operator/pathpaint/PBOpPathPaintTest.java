package org.verapdf.model.impl.pb.operator.pathpaint;

import org.verapdf.model.impl.pb.operator.base.PBOperatorTest;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpPathPaintTest extends PBOperatorTest {

	public static final String DEVICE_RGB = "PDDeviceRGB";

	protected void testColorSpaceLink(String link, String type) {
		getObject(link, 1, type);
	}
}
