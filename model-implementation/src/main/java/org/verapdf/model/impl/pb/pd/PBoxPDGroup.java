package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDGroup extends PBoxPDObject implements PDGroup {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDGroup.class);

    /**
     * String name for colour space
     */
    public static final String COLOR_SPACE = "colorSpace";
    /**
     * String name for a PDGroup type
     */
	public static final String GROUP_TYPE = "PDGroup";

    private static final int MAX_COLOR_SPACES = 1;

    /**
     * @param simplePDObject
     *            a {@link org.apache.pdfbox.pdmodel.graphics.form.PDGroup
     *            simplePDObject} used to create this instance
     */
    public PBoxPDGroup(org.apache.pdfbox.pdmodel.graphics.form.PDGroup simplePDObject) {
		super(simplePDObject);
		setType(GROUP_TYPE);
	}

	@Override
	public String getS() {
		return ((org.apache.pdfbox.pdmodel.graphics.form.PDGroup) simplePDObject).getSubType().getName();
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case COLOR_SPACE:
				list = this.getColorSpace();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<PDColorSpace> getColorSpace() {
		List<PDColorSpace> colorSpaces = new ArrayList<>(MAX_COLOR_SPACES);
		try {
			org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace pbColorSpace =
					((org.apache.pdfbox.pdmodel.graphics.form.PDGroup) simplePDObject).getColorSpace();
			PDColorSpace colorSpace = ColorSpaceFactory.getColorSpace(pbColorSpace);
			if (colorSpace != null) {
				colorSpaces.add(colorSpace);
			}
		} catch (IOException e) {
			LOGGER.error("Problems with color space obtaining on group. " + e.getMessage(), e);
		}
		return colorSpaces;
	}
}
