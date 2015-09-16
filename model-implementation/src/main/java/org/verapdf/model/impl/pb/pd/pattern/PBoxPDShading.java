package org.verapdf.model.impl.pb.pd.pattern;

import org.apache.log4j.Logger;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.pb.pd.PBoxPDResources;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDShading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDShading extends PBoxPDResources implements PDShading {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDShading.class);

    public static final String SHADING_TYPE = "PDShading";

    public static final String COLOR_SPACE = "colorSpace";

    public PBoxPDShading(
            org.apache.pdfbox.pdmodel.graphics.shading.PDShading simplePDObject) {
        super(simplePDObject, SHADING_TYPE);
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
            org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace cs =
					((org.apache.pdfbox.pdmodel.graphics.shading.PDShading) this.simplePDObject)
                    .getColorSpace();
            if (cs != null) {
				List<PDColorSpace> colorSpaces =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				colorSpaces.add(ColorSpaceFactory.getColorSpace(cs));
				return Collections.unmodifiableList(colorSpaces);
            }
        } catch (IOException e) {
            LOGGER.error("Problems with color space obtaining from shading. "
                    + e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
