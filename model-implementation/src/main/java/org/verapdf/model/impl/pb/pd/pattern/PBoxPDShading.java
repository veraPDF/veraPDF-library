package org.verapdf.model.impl.pb.pd.pattern;

import org.apache.log4j.Logger;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.pb.pd.PBoxPDResources;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDShading;

import java.io.IOException;
import java.util.ArrayList;
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
        super(simplePDObject);
        setType(SHADING_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (COLOR_SPACE.equals(link)) {
            return getColorSpace();
        }
        return super.getLinkedObjects(link);
    }

    private List<PDColorSpace> getColorSpace() {
        List<PDColorSpace> colorSpaces = new ArrayList<>(1);
        try {
            org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace cs = ((org.apache.pdfbox.pdmodel.graphics.shading.PDShading) simplePDObject)
                    .getColorSpace();
            if (cs != null) {
                colorSpaces.add(ColorSpaceFactory.getColorSpace(cs));
            }
        } catch (IOException e) {
            LOGGER.error("Problems with color space obtaining from shading. "
                    + e.getMessage(), e);
        }
        return colorSpaces;
    }
}
