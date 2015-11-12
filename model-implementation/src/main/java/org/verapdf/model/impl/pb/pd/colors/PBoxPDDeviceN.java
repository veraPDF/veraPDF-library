package org.verapdf.model.impl.pb.pd.colors;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceNAttributes;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosUnicodeName;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.pb.cos.PBCosUnicodeName;
import org.verapdf.model.pdlayer.PDColorSpace;
import org.verapdf.model.pdlayer.PDDeviceN;
import org.verapdf.model.pdlayer.PDSeparation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * DeviceN color space
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDeviceN extends PBoxPDColorSpace implements PDDeviceN {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDDeviceN.class);

	public static final String DEVICE_N_TYPE = "PDDeviceN";

	public static final String ALTERNATE = "alternate";
	public static final String COLORANT_NAMES = "colorantNames";
	public static final String COLORANTS = "Colorants";

	public static final int COLORANT_NAMES_POSITION = 1;

	private final boolean areColorantsPresent;

	public PBoxPDDeviceN(
			org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN simplePDObject) {
		super(simplePDObject, DEVICE_N_TYPE);
		this.areColorantsPresent = this.areColorantsPresent(simplePDObject);
	}

	private boolean areColorantsPresent(
			org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN simplePDObject) {
		PDDeviceNAttributes attributes = simplePDObject.getAttributes();
		if (attributes != null) {
			COSDictionary attrDict = attributes.getCOSDictionary();
			COSBase colorantsDict = attrDict.getDictionaryObject(COSName.COLORANTS);
			if (colorantsDict instanceof COSDictionary) {
				COSArray array = (COSArray) simplePDObject.getCOSObject();
				COSBase colorantsArray = array.get(1);

				if (colorantsArray instanceof COSArray) {
					return this.areColorantsPresent((COSDictionary) colorantsDict, colorantsArray);
				}
			}
		}
		return false;
	}

	private boolean areColorantsPresent(COSDictionary colorantsDict, COSBase colorantsArray) {
		Set<COSName> colorantDictionaryEntries = colorantsDict.keySet();
		for (int i = 0; i < ((COSArray) colorantsArray).size(); i++) {
			COSBase object = ((COSArray) colorantsArray).getObject(i);
			if (object instanceof COSName &&
					!colorantDictionaryEntries.contains(object)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Boolean getareColorantsPresent() {
		return Boolean.valueOf(this.areColorantsPresent);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case ALTERNATE:
				return this.getAlternate();
			case COLORANT_NAMES:
				return this.getColorantNames();
			case COLORANTS:
				return this.getColorants();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<PDColorSpace> getAlternate() {
		try {
			org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace alternateColorSpace =
					((org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN) this.simplePDObject)
							.getAlternateColorSpace();
			PDColorSpace space = ColorSpaceFactory.getColorSpace(alternateColorSpace);
			if (space != null) {
				List<PDColorSpace> colorSpace = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				colorSpace.add(space);
				return Collections.unmodifiableList(colorSpace);
			}
		} catch (IOException e) {
			LOGGER.error("Can not get alternate color space from DeviceN. ", e);
		}
		return Collections.emptyList();
	}

	private List<CosUnicodeName> getColorantNames() {
		COSArray array = (COSArray) this.simplePDObject.getCOSObject();
		COSBase colorants = array.getObject(COLORANT_NAMES_POSITION);
		if (colorants instanceof COSArray) {
			ArrayList<CosUnicodeName> list = new ArrayList<>(((COSArray) colorants).size());
			for (COSBase colorant : (COSArray) colorants) {
				if (colorant instanceof COSName) {
					list.add(new PBCosUnicodeName((COSName) colorant));
				}
			}
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

	private List<PDSeparation> getColorants() {
		PDDeviceNAttributes attributes =
				((org.apache.pdfbox.pdmodel.graphics.color.PDDeviceN) this.simplePDObject).getAttributes();
		if (attributes != null) {
			COSDictionary dictionary = attributes.getCOSDictionary();
			COSBase colorantsDict = dictionary.getDictionaryObject(COSName.COLORANTS);
			if (colorantsDict instanceof COSDictionary) {
				return this.getColorants((COSDictionary) colorantsDict);
			}
		}
		return Collections.emptyList();
	}

	private List<PDSeparation> getColorants(COSDictionary colorantsDict) {
		ArrayList<PDSeparation> list = new ArrayList<>(colorantsDict.size());
		for (COSBase value : colorantsDict.getValues()) {
			try {
				org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace colorSpace =
						org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace.create(value);
				if (colorSpace instanceof org.apache.pdfbox.pdmodel.graphics.color.PDSeparation) {
					list.add(new PBoxPDSeparation((org.apache.pdfbox.pdmodel.graphics.color.PDSeparation) colorSpace));
				}
			} catch (IOException e) {
				LOGGER.warn("Problems with color space obtain.", e);
			}
		}
		return Collections.unmodifiableList(list);
	}
}
