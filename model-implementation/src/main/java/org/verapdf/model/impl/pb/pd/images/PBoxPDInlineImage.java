package org.verapdf.model.impl.pb.pd.images;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.verapdf.model.baselayer.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosIIFilter;
import org.verapdf.model.coslayer.CosRenderingIntent;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.pb.cos.PBCosIIFilter;
import org.verapdf.model.impl.pb.cos.PBCosRenderingIntent;
import org.verapdf.model.impl.pb.pd.PBoxPDObject;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDInlineImage;
import org.verapdf.model.pdlayer.PDXImage;
import org.verapdf.model.pdlayer.PDXObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDInlineImage extends PBoxPDObject implements PDInlineImage {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDInlineImage.class);

	public static final String F = "F";

	public static final String INLINE_IMAGE_TYPE = "PDInlineImage";

	public PBoxPDInlineImage(org.apache.pdfbox.pdmodel.graphics.image.PDInlineImage simplePDObject) {
		super(simplePDObject, INLINE_IMAGE_TYPE);
	}

	@Override
	public Boolean getInterpolate() {
		return Boolean.valueOf(((PDImage) this.simplePDObject)
				.getInterpolate());
	}

	@Override
	public String getSubtype() {
		return null;
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case PBoxPDXImage.INTENT:
				return this.getIntent();
			case PBoxPDXImage.IMAGE_CS:
				return this.getImageCS();
			case PBoxPDXObject.S_MASK:
			case PBoxPDXObject.OPI:
			case PBoxPDXImage.ALTERNATES:
			case PBoxPDXImage.JPX_STREAM:
				return Collections.emptyList();
			case F:
				return getFilters();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<CosIIFilter> getFilters() {
		List<String> filters = ((org.apache.pdfbox.pdmodel.graphics.image.PDInlineImage) this.simplePDObject).getFilters();
		List<CosIIFilter> result = new ArrayList<>();
		if (filters != null) {
			for (String filter : filters) {
				result.add(new PBCosIIFilter(filter));
			}
		}
		return result;
	}

	private List<PDColorSpace> getImageCS() {
		try {
			PDColorSpace buffer = ColorSpaceFactory
					.getColorSpace(((PDImage) this.simplePDObject)
							.getColorSpace());
			if (buffer != null) {
				List<PDColorSpace> colorSpaces =
						new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				colorSpaces.add(buffer);
				return Collections.unmodifiableList(colorSpaces);
			}
		} catch (IOException e) {
			LOGGER.error(
					"Problems with color space obtaining from InlineImage XObject. "
							+ e.getMessage(), e);
		}
		return Collections.emptyList();
	}

	private List<CosRenderingIntent> getIntent() {
		COSDictionary imageStream = (COSDictionary) this.simplePDObject
				.getCOSObject();
		COSName intent = imageStream.getCOSName(COSName.getPDFName(
				PBoxPDXImage.INTENT));
		if (intent != null) {
			List<CosRenderingIntent> intents = new ArrayList<>(
					MAX_NUMBER_OF_ELEMENTS);
			intents.add(new PBCosRenderingIntent(intent));
			return Collections.unmodifiableList(intents);
		}
		return Collections.emptyList();
	}

}
