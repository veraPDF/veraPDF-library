package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.ImageFeaturesData;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.IOException;
import java.util.*;

/**
 * Features object for image xobject
 *
 * @author Maksim Bezrukov
 */
public class PBImageXObjectFeaturesObject implements IFeaturesObject {

	private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger
			.getLogger(PBImageXObjectFeaturesObject.class);

	private static final String ID = "id";

	private PDImageXObject imageXObject;
	private String id;
	private String colorSpaceChild;
	private String maskChild;
	private String sMaskChild;
	private Set<String> alternatesChild;
	private Set<String> pageParent;
	private Set<String> patternParent;
	private Set<String> xobjectParent;
	private Set<String> fontParent;

	/**
	 * Constructs new shading features object
	 *
	 * @param imageXObject    PDImageXObject which represents image xobject for feature report
	 * @param id              id of the object
	 * @param colorSpaceChild colorSpace id which contains in this image xobject
	 * @param maskChild       image xobject id which contains in this image xobject as it's mask
	 * @param sMaskChild      image xobject id which contains in this image xobject as it's smask
	 * @param alternatesChild set of image xobject ids which contains in this image xobject as alternates
	 * @param pageParent      set of page ids which contains the given image xobject as its resources
	 * @param patternParent   set of pattern ids which contains the given image xobject state as its resources
	 * @param xobjectParent   set of xobject ids which contains the given image xobject state as its resources
	 * @param fontParent      set of font ids which contains the given image xobject state as its resources
	 */
	public PBImageXObjectFeaturesObject(PDImageXObject imageXObject, String id, String colorSpaceChild, String maskChild, String sMaskChild, Set<String> alternatesChild, Set<String> pageParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
		this.imageXObject = imageXObject;
		this.id = id;
		this.colorSpaceChild = colorSpaceChild;
		this.maskChild = maskChild;
		this.sMaskChild = sMaskChild;
		this.alternatesChild = alternatesChild;
		this.pageParent = pageParent;
		this.patternParent = patternParent;
		this.xobjectParent = xobjectParent;
		this.fontParent = fontParent;
	}

	/**
	 * @return IMAGE_XOBJECT instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.IMAGE_XOBJECT;
	}

	/**
	 * Reports featurereport into collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeatureParsingException {
		if (imageXObject != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("xobject");
			root.setAttribute("type", "image");
			root.setAttribute(ID, id);

			parseParents(root);

			FeatureTreeNode.createChildNode("width", root).setValue(String.valueOf(imageXObject.getWidth()));
			FeatureTreeNode.createChildNode("height", root).setValue(String.valueOf(imageXObject.getHeight()));

			if (colorSpaceChild != null) {
				FeatureTreeNode shading = FeatureTreeNode.createChildNode("colorSpace", root);
				shading.setAttribute(ID, colorSpaceChild);
			}

			FeatureTreeNode.createChildNode("bitsPerComponent", root).setValue(String.valueOf(imageXObject.getBitsPerComponent()));
			FeatureTreeNode.createChildNode("imageMask", root).setValue(String.valueOf(imageXObject.isStencil()));

			if (maskChild != null) {
				FeatureTreeNode mask = FeatureTreeNode.createChildNode("mask", root);
				mask.setAttribute(ID, maskChild);
			}

			FeatureTreeNode.createChildNode("interpolate", root).setValue(String.valueOf(imageXObject.getInterpolate()));
			PBCreateNodeHelper.parseIDSet(alternatesChild, "alternate", "alternates", root);
			if (sMaskChild != null) {
				FeatureTreeNode mask = FeatureTreeNode.createChildNode("sMask", root);
				mask.setAttribute(ID, sMaskChild);
			}

			if (imageXObject.getCOSStream().getItem(COSName.STRUCT_PARENT) != null) {
				FeatureTreeNode.createChildNode("structParent", root).setValue(String.valueOf(imageXObject.getStructParent()));
			}

			try {
				if (imageXObject.getStream().getFilters() != null && !imageXObject.getStream().getFilters().isEmpty()) {
					FeatureTreeNode filters = FeatureTreeNode.createChildNode("filters", root);
					for (COSName name : imageXObject.getStream().getFilters()) {
						PBCreateNodeHelper.addNotEmptyNode("filter", name.getName(), filters);
					}
				}
			} catch (IOException e) {
				LOGGER.info(e);
			}

			PBCreateNodeHelper.parseMetadata(imageXObject.getMetadata(), "metadata", root, collection);

			collection.addNewFeatureTree(FeaturesObjectTypesEnum.IMAGE_XOBJECT, root);
			return root;
		}

		return null;
	}

	/**
	 * @return null if it can not get image xobject stream and features data of the image in other case.
	 */
	@Override
	public FeaturesData getData() {
		try {
			byte[] stream = PBCreateNodeHelper.inputStreamToByteArray(imageXObject.getCOSStream().getFilteredStream());
			byte[] metadata = null;
			if (imageXObject.getMetadata() != null) {
				try {
					metadata = PBCreateNodeHelper.inputStreamToByteArray(imageXObject.getMetadata().getStream().getUnfilteredStream());
				} catch (IOException e) {
					LOGGER.error("Can not get metadata stream for image xobject", e);
				}
			}

			List<ImageFeaturesData.Filter> filters = new ArrayList<>();
			if (imageXObject.getPDStream().getFilters() != null) {
				List<String> filtersNames = new ArrayList<>();
				for (COSName filter : imageXObject.getPDStream().getFilters()) {
					filtersNames.add(filter.getName());
				}

				List<COSDictionary> decodeList = getDecodeList(imageXObject.getCOSStream().getDictionaryObject(COSName.DECODE_PARMS));

				for (int i = 0; i < filtersNames.size(); ++i) {
					String filter = filtersNames.get(i);
					COSDictionary dic = i < decodeList.size() ? decodeList.get(i) : null;
					switch (filter) {
						case "LZWDecode":
							filters.add(ImageFeaturesData.Filter.newInstance(filter, getLZWOrFlatFiltersMap(dic, true), null));
							break;
						case "FlateDecode":
							filters.add(ImageFeaturesData.Filter.newInstance(filter, getLZWOrFlatFiltersMap(dic, false), null));
							break;
						case "CCITTFaxDecode":
							filters.add(ImageFeaturesData.Filter.newInstance(filter, getCCITTFaxFiltersMap(dic), null));
							break;
						case "DCTDecode":
							filters.add(ImageFeaturesData.Filter.newInstance(filter, getDCTFiltersMap(dic), null));
							break;
						case "JBIG2Decode":
							byte[] global = null;
							if (dic != null && dic.getDictionaryObject(COSName.JBIG2_GLOBALS) instanceof COSStream) {
								global = PBCreateNodeHelper.inputStreamToByteArray(((COSStream) dic.getDictionaryObject(COSName.JBIG2_GLOBALS)).getUnfilteredStream());
							}
							filters.add(ImageFeaturesData.Filter.newInstance(filter, new HashMap<String, String>(), global));
							break;
						case "Crypt":
							if (!(dic != null && COSName.IDENTITY.equals(dic.getCOSName(COSName.NAME)))) {
								LOGGER.error("An Image has a Crypt filter");
								return null;
							}
						default:
							filters.add(ImageFeaturesData.Filter.newInstance(filter, new HashMap<String, String>(), null));
					}
				}
			}

			Integer width = getIntegerWithDefault(imageXObject.getCOSStream().getDictionaryObject(COSName.WIDTH), null);
			Integer height = getIntegerWithDefault(imageXObject.getCOSStream().getDictionaryObject(COSName.HEIGHT), null);

			return ImageFeaturesData.newInstance(metadata, stream, width, height, filters);
		} catch (IOException e) {
			LOGGER.error("Error in obtaining features data for fonts", e);
			return null;
		}
	}

	private static List<COSDictionary> getDecodeList(COSBase base) {
		List<COSDictionary> res = new ArrayList<>();

		if (base instanceof COSDictionary) {
			res.add((COSDictionary) base);
		} else if (base instanceof COSArray) {
			for (COSBase baseElem : (COSArray) base) {
				if (baseElem instanceof COSDictionary) {
					res.add((COSDictionary) baseElem);
				} else {
					res.add(null);
				}
			}
		}

		return res;
	}

	private static Map<String, String> getCCITTFaxFiltersMap(COSDictionary base) {
		Map<String, String> res = new HashMap<>();
		if (base != null) {
			putIntegerAsStringWithDefault(res, "K", base.getDictionaryObject(COSName.K), 0);
			putBooleanAsStringWithDefault(res, "EndOfLine", base.getDictionaryObject(COSName.COLORS), false);
			putBooleanAsStringWithDefault(res, "EncodedByteAlign", base.getDictionaryObject(COSName.BITS_PER_COMPONENT), false);
			putIntegerAsStringWithDefault(res, "Columns", base.getDictionaryObject(COSName.COLUMNS), 1728);
			putIntegerAsStringWithDefault(res, "Rows", base.getDictionaryObject(COSName.ROWS), 0);
			putBooleanAsStringWithDefault(res, "EndOfBlock", base.getDictionaryObject(COSName.getPDFName("EndOfBlock")), true);
			putBooleanAsStringWithDefault(res, "BlackIs1", base.getDictionaryObject(COSName.BLACK_IS_1), false);
			putIntegerAsStringWithDefault(res, "DamagedRowsBeforeError", base.getDictionaryObject(COSName.getPDFName("DamagedRowsBeforeError")), 0);
		} else {
			res.put("K", "0");
			res.put("EndOfLine", "false");
			res.put("EncodedByteAlign", "false");
			res.put("Columns", "1728");
			res.put("Rows", "0");
			res.put("EndOfBlock", "true");
			res.put("BlackIs1", "false");
			res.put("DamagedRowsBeforeError", "0");
		}

		return res;
	}

	private static Map<String, String> getDCTFiltersMap(COSDictionary base) {
		Map<String, String> res = new HashMap<>();
		if (base != null && base.getDictionaryObject(COSName.getPDFName("ColorTransform")) != null
				&& base.getDictionaryObject(COSName.getPDFName("ColorTransform")) instanceof COSInteger) {
			res.put("ColorTransform", String.valueOf(((COSInteger) (base).getDictionaryObject(COSName.getPDFName("ColorTransform"))).intValue()));
		}
		return res;
	}

	private static Map<String, String> getLZWOrFlatFiltersMap(COSDictionary base, boolean isLZW) {
		Map<String, String> res = new HashMap<>();
		if (base != null) {
			putIntegerAsStringWithDefault(res, "Predictor", base.getDictionaryObject(COSName.PREDICTOR), 1);
			putIntegerAsStringWithDefault(res, "Colors", base.getDictionaryObject(COSName.COLORS), 1);
			putIntegerAsStringWithDefault(res, "BitsPerComponent", base.getDictionaryObject(COSName.BITS_PER_COMPONENT), 8);
			putIntegerAsStringWithDefault(res, "Columns", base.getDictionaryObject(COSName.COLUMNS), 1);
			if (isLZW) {
				putIntegerAsStringWithDefault(res, "EarlyChange", base.getDictionaryObject(COSName.EARLY_CHANGE), 1);
			}
		} else {
			res.put("Predictor", "1");
			res.put("Colors", "1");
			res.put("BitsPerComponent", "8");
			res.put("Columns", "1");
			if (isLZW) {
				res.put("EarlyChange", "1");
			}
		}
		return res;
	}

	private static Integer getIntegerWithDefault(Object value, Integer defaultValue) {
		if (value instanceof COSInteger) {
			return ((COSInteger) value).intValue();
		} else {
			return defaultValue;
		}
	}

	private static void putIntegerAsStringWithDefault(Map<String, String> map, String key, Object value, Integer defaultValue) {
		if (value instanceof COSInteger) {
			map.put(key, String.valueOf(((COSInteger) value).intValue()));
		} else {
			if (defaultValue != null) {
				map.put(key, defaultValue.toString());
			}
		}
	}

	private static void putBooleanAsStringWithDefault(Map<String, String> map, String key, Object value, Boolean defaultValue) {
		if (value instanceof COSBoolean) {
			map.put(key, String.valueOf(((COSBoolean) value).getValue()));
		} else {
			if (defaultValue != null) {
				map.put(key, defaultValue.toString());
			}
		}
	}

	private void parseParents(FeatureTreeNode root) throws FeatureParsingException {
		if ((pageParent != null && !pageParent.isEmpty()) ||
				(patternParent != null && !patternParent.isEmpty()) ||
				(xobjectParent != null && !xobjectParent.isEmpty()) ||
				(fontParent != null && !fontParent.isEmpty())) {
			FeatureTreeNode parents = FeatureTreeNode.createChildNode("parents", root);

			PBCreateNodeHelper.parseIDSet(pageParent, "page", null, parents);
			PBCreateNodeHelper.parseIDSet(patternParent, "pattern", null, parents);
			PBCreateNodeHelper.parseIDSet(xobjectParent, "xobject", null, parents);
			PBCreateNodeHelper.parseIDSet(fontParent, "font", null, parents);
		}
	}
}
