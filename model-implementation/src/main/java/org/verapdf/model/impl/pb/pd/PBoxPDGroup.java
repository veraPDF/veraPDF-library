package org.verapdf.model.impl.pb.pd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDGroup;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDGroup extends PBoxPDObject implements PDGroup {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDGroup.class);

	public static final String GROUP_TYPE = "PDGroup";

	public static final String COLOR_SPACE = "colorSpace";

    public PBoxPDGroup(
            org.apache.pdfbox.pdmodel.graphics.form.PDGroup simplePDObject) {
        super(simplePDObject, GROUP_TYPE);
    }

    @Override
    public String getS() {
        return ((org.apache.pdfbox.pdmodel.graphics.form.PDGroup) this.simplePDObject)
                .getSubType().getName();
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (COLOR_SPACE.equals(link)) {
            return this.getColorSpace();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDColorSpace> getColorSpace() {
        try {
            org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace pbColorSpace =
					((org.apache.pdfbox.pdmodel.graphics.form.PDGroup) this.simplePDObject)
                    .getColorSpace();
            PDColorSpace colorSpace = ColorSpaceFactory
                    .getColorSpace(pbColorSpace);
            if (colorSpace != null) {
				List<PDColorSpace> colorSpaces = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				colorSpaces.add(colorSpace);
				return Collections.unmodifiableList(colorSpaces);
            }
        } catch (IOException e) {
            LOGGER.error(
                    "Problems with color space obtaining on group. "
                            + e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
