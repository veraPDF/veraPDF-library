package org.verapdf.model.impl.pb.pd.images;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
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

	public static final String X_IMAGE_TYPE = "PDXImage";

    public static final String IMAGE_CS = "imageCS";
    public static final String ALTERNATES = "Alternates";
    public static final String INTENT = "Intent";

    public PBoxPDXImage(PDImage simplePDObject) {
        this(simplePDObject, X_IMAGE_TYPE);
    }

	public PBoxPDXImage(PDImage simplePDObject, final String type) {
		super(simplePDObject, type);
	}

    @Override
    public Boolean getInterpolate() {
        return Boolean.valueOf(((PDImage) this.simplePDObject)
                .getInterpolate());
    }

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case INTENT:
				return this.getIntent();
			case IMAGE_CS:
				return this.getImageCS();
			case ALTERNATES:
				return this.getAlternates();
			default:
				return super.getLinkedObjects(link);
		}
	}

    private List<CosRenderingIntent> getIntent() {
        List<CosRenderingIntent> intents = new ArrayList<>(
                MAX_NUMBER_OF_ELEMENTS);
        COSDictionary imageStream = (COSDictionary) this.simplePDObject
				.getCOSObject();
        COSName intent = imageStream.getCOSName(COSName.getPDFName(INTENT));
        if (intent != null) {
            intents.add(new PBCosRenderingIntent(intent));
        }
        return intents;
    }

    private List<PDColorSpace> getImageCS() {
        List<PDColorSpace> colorSpaces = new ArrayList<>(
				MAX_NUMBER_OF_ELEMENTS);
        try {
            PDColorSpace buffer = ColorSpaceFactory
                    .getColorSpace(((PDImage) this.simplePDObject)
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

    protected List<PDXImage> getAlternates() {
        final List<PDXImage> alternates = new ArrayList<>();
        final COSStream imageStream = ((PDImageXObject) this.simplePDObject)
                .getCOSStream();
        final COSBase buffer = imageStream.getDictionaryObject(COSName
				.getPDFName(ALTERNATES));
        addAlternates(alternates, buffer, ((PDImageXObject) this.simplePDObject)
				.getResources());
        return alternates;
    }

    private static void addAlternates(List<PDXImage> alternates, COSBase buffer,
							   PDResources resources) {
        if (buffer instanceof COSArray) {
            for (COSBase element : (COSArray) buffer) {
				if (element instanceof COSObject) {
					element = ((COSObject) element).getObject();
				}
				if (element instanceof COSDictionary) {
					addAlternate(alternates, (COSDictionary) element,
							resources);
				}
            }
        }
    }

	private static void addAlternate(List<PDXImage> alternates, COSDictionary buffer,
							  PDResources resources) {
		COSStream alternatesImages = (COSStream) buffer
				.getDictionaryObject(COSName.IMAGE);
		try {
			if (alternatesImages != null) {
				final PDStream stream = new PDStream(alternatesImages);
				PDImageXObject imageXObject = new PDImageXObject(stream,
						resources);
				alternates.add(new PBoxPDXImage(imageXObject));
			}
		} catch (IOException e) {
			LOGGER.error(
					"Error in creating Alternate XObject. "
							+ e.getMessage(), e);
		}
	}

}
