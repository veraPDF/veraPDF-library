package org.verapdf.model.impl.pb.pd.images;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosRenderingIntent;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.pb.cos.PBCosRenderingIntent;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDXImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDXImage extends PBoxPDXObject implements PDXImage {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDXImage.class);

    public static final String IMAGE_CS = "imageCS";
    public static final String ALTERNATES = "Alternates";
    public static final String INTENT = "Intent";
    public static final String X_IMAGE_TYPE = "PDXImage";

    public PBoxPDXImage(PDImageXObject simplePDObject) {
        super(simplePDObject);
        setType(X_IMAGE_TYPE);
    }

    @Override
    public Boolean getInterpolate() {
        return Boolean.valueOf(((PDImageXObject) simplePDObject)
                .getInterpolate());
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        List<? extends Object> list;

        switch (link) {
        case INTENT:
            list = this.getIntent();
            break;
        case IMAGE_CS:
            list = this.getImageCS();
            break;
        case ALTERNATES:
            list = this.getAlternates();
            break;
        default:
            list = super.getLinkedObjects(link);
            break;
        }

        return list;
    }

    private List<CosRenderingIntent> getIntent() {
        List<CosRenderingIntent> intents = new ArrayList<>(
                MAX_NUMBER_OF_ELEMENTS);
        COSStream imageStream = ((PDImageXObject) simplePDObject)
                .getCOSStream();
        COSName intent = imageStream.getCOSName(COSName.getPDFName(INTENT));
        if (intent != null) {
            intents.add(new PBCosRenderingIntent(intent));
        }
        return intents;
    }

    private List<PDColorSpace> getImageCS() {
        List<PDColorSpace> colorSpaces = new ArrayList<>(1);
        try {
            PDColorSpace buffer = ColorSpaceFactory
                    .getColorSpace(((PDImageXObject) simplePDObject)
                            .getColorSpace());
            if (buffer != null) {
                colorSpaces.add(buffer);
            }
        } catch (IOException e) {
            LOGGER.error(
                    "Problems with color space obtaining from Image XObject. "
                            + e.getMessage(), e);
        }
        return colorSpaces;
    }

    private List<PDXImage> getAlternates() {
        final List<PDXImage> alternates = new ArrayList<>();
        final COSStream imageStream = ((PDImageXObject) simplePDObject)
                .getCOSStream();
        COSBase buffer = imageStream.getItem(COSName.getPDFName(ALTERNATES));
        addAlternates(alternates, buffer);
        return alternates;
    }

    private void addAlternates(List<PDXImage> alternates, COSBase buffer) {
        if (buffer instanceof COSArray) {
            for (COSBase element : (COSArray) buffer) {
                addAlternate(alternates, element);
            }
        } else if (buffer instanceof COSObject) {
            addAlternates(alternates, ((COSObject) buffer).getObject());
        }
    }

    private void addAlternate(List<PDXImage> alternates, COSBase buffer) {
        COSDictionary alternate = getDictionary(buffer);
        if (alternate != null) {
            COSStream alternatesImages = (COSStream) alternate
                    .getDictionaryObject(COSName.IMAGE);
            try {
                if (alternatesImages != null) {
                    final PDStream stream = new PDStream(alternatesImages);
                    final PDResources res = ((PDImageXObject) simplePDObject)
                            .getResources();
                    PDImageXObject imageXObject = new PDImageXObject(stream,
                            res);
                    alternates.add(new PBoxPDXImage(imageXObject));
                }
            } catch (IOException e) {
                LOGGER.error(
                        "Error in creating Alternate XObject. "
                                + e.getMessage(), e);
            }
        }
    }
}
