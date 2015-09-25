package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
	 * @throws FeaturesTreeNodeException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException {
		if (imageXObject != null) {
			FeatureTreeNode root = FeatureTreeNode.newRootInstance("image");
			root.addAttribute(ID, id);

			parseParents(root);

			FeatureTreeNode.newChildInstanceWithValue("width", String.valueOf(imageXObject.getWidth()), root);
			FeatureTreeNode.newChildInstanceWithValue("height", String.valueOf(imageXObject.getHeight()), root);

			if (colorSpaceChild != null) {
				FeatureTreeNode shading = FeatureTreeNode.newChildInstance("colorSpace", root);
				shading.addAttribute(ID, colorSpaceChild);
			}

			FeatureTreeNode.newChildInstanceWithValue("bitsPerComponent", String.valueOf(imageXObject.getBitsPerComponent()), root);
			FeatureTreeNode.newChildInstanceWithValue("imageMask", String.valueOf(imageXObject.isStencil()), root);

			if (maskChild != null) {
				FeatureTreeNode mask = FeatureTreeNode.newChildInstance("mask", root);
				mask.addAttribute(ID, maskChild);
			}

			FeatureTreeNode.newChildInstanceWithValue("interpolate", String.valueOf(imageXObject.getInterpolate()), root);
			PBCreateNodeHelper.parseIDSet(alternatesChild, "alternate", "alternates", root);
			if (sMaskChild != null) {
				FeatureTreeNode mask = FeatureTreeNode.newChildInstance("sMask", root);
				mask.addAttribute(ID, sMaskChild);
			}

			if (imageXObject.getCOSStream().getItem(COSName.STRUCT_PARENT) != null) {
				FeatureTreeNode.newChildInstanceWithValue("structParent", String.valueOf(imageXObject.getStructParent()), root);
			}

			try {
				if (imageXObject.getStream().getFilters() != null && !imageXObject.getStream().getFilters().isEmpty()) {
					FeatureTreeNode filters = FeatureTreeNode.newChildInstance("filters", root);
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

	private static byte[] inputStreamToByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = is.read();
		while (reads != -1) {
			baos.write(reads);
			reads = is.read();
		}
		return baos.toByteArray();
	}

	/**
	 * @return null if it can not get image xobject stream and features data of the image in other case.
	 */
	@Override
	public FeaturesData getData() {
		try {
			byte[] stream = inputStreamToByteArray(imageXObject.getCOSStream().getFilteredStream());
			List<byte[]> streams = new ArrayList<>();
			streams.add(stream);
			byte[] metadata = null;
			try {
				metadata = inputStreamToByteArray(imageXObject.getMetadata().getStream().getUnfilteredStream());
			} catch (IOException e) {
				LOGGER.error("Can not get metadata stream for image xobject", e);
			}

			Map<String, Object> properties = new HashMap<>();

			if (imageXObject.getPDStream().getFilters() != null) {
				List<String> filters = new ArrayList<>();
				for (COSName filter : imageXObject.getPDStream().getFilters()) {
					filters.add(filter.getName());
				}
				properties.put("Filter", filters);

				List<COSDictionary> decodeList = getDecodeList(imageXObject.getCOSStream().getDictionaryObject(COSName.DECODE_PARMS));
				List<Map<String, Object>> decodeParms = new ArrayList<>();

				for (int i = 0; i < filters.size(); ++i) {
					String filter = filters.get(i);
					COSDictionary dic = i < decodeList.size() ? decodeList.get(i) : null;
					switch (filter) {
						case "LZWDecode":
							decodeParms.add(getLWZOrFlatFiltersMap(dic, true));
							break;
						case "FlateDecode":
							decodeParms.add(getLWZOrFlatFiltersMap(dic, false));
							break;
						case "CCITTFaxDecode":
							decodeParms.add(getCCITTFaxFiltersMap(dic));
							break;
						case "DCTDecode":
							decodeParms.add(getDCTFiltersMap(dic));
							break;
						case "JBIG2Decode":
							if (dic == null || !(dic.getDictionaryObject(COSName.JBIG2_GLOBALS) instanceof COSStream)) {
								LOGGER.error("JBIG2Decode has no global segments stream in decode params");
								return null;
							} else {
								byte[] global = inputStreamToByteArray(((COSStream) dic.getDictionaryObject(COSName.JBIG2_GLOBALS)).getUnfilteredStream());
								int index = streams.size();
								streams.add(index, global);
								Map<String, Object> map = new HashMap<>();
								map.put("JBIG2Globals", String.valueOf(index));
								decodeParms.add(map);
							}
							break;
						case "Crypt":
							LOGGER.error("An Image has Crypt filter");
							return null;
						default:
							decodeParms.add(null);
					}
				}
				properties.put("DecodeParms", decodeParms);
			}

			putIntegerWithDefault(properties, "Width", imageXObject.getCOSStream().getDictionaryObject(COSName.WIDTH), null);
			putIntegerWithDefault(properties, "Height", imageXObject.getCOSStream().getDictionaryObject(COSName.HEIGHT), null);

			return new FeaturesData(metadata, streams, properties);
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

	private static Map<String, Object> getCCITTFaxFiltersMap(COSDictionary base) {
		Map<String, Object> res = new HashMap<>();

		if (base != null) {
			putIntegerWithDefault(res, "K", base.getDictionaryObject(COSName.K), "0");
			putIntegerWithDefault(res, "EndOfLine", base.getDictionaryObject(COSName.COLORS), "false");
			putIntegerWithDefault(res, "EncodedByteAlign", base.getDictionaryObject(COSName.BITS_PER_COMPONENT), "false");
			putIntegerWithDefault(res, "Columns", base.getDictionaryObject(COSName.COLUMNS), "1728");
			putIntegerWithDefault(res, "Rows", base.getDictionaryObject(COSName.ROWS), "0");
			putIntegerWithDefault(res, "EndOfBlock", base.getDictionaryObject(COSName.getPDFName("EndOfBlock")), "true");
			putIntegerWithDefault(res, "BlackIs1", base.getDictionaryObject(COSName.BLACK_IS_1), "false");
			putIntegerWithDefault(res, "DamagedRowsBeforeError", base.getDictionaryObject(COSName.getPDFName("DamagedRowsBeforeError")), "0");
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

	private static Map<String, Object> getDCTFiltersMap(COSDictionary base) {
		if (base != null && base.getDictionaryObject(COSName.getPDFName("ColorTransform")) != null
				&& base.getDictionaryObject(COSName.getPDFName("ColorTransform")) instanceof COSInteger) {
			Map<String, Object> res = new HashMap<>();
			res.put("ColorTransform", String.valueOf(((COSInteger) (base).getDictionaryObject(COSName.getPDFName("ColorTransform"))).intValue()));
			return res;
		} else {
			return null;
		}
	}

	private static Map<String, Object> getLWZOrFlatFiltersMap(COSDictionary base, boolean isLWZ) {
		Map<String, Object> res = new HashMap<>();

		if (base != null) {
			putIntegerWithDefault(res, "Predictor", base.getDictionaryObject(COSName.PREDICTOR), "1");
			putIntegerWithDefault(res, "Colors", base.getDictionaryObject(COSName.COLORS), "1");
			putIntegerWithDefault(res, "BitsPerComponent", base.getDictionaryObject(COSName.BITS_PER_COMPONENT), "8");
			putIntegerWithDefault(res, "Columns", base.getDictionaryObject(COSName.COLUMNS), "1");
			if (isLWZ) {
				putIntegerWithDefault(res, "EarlyChange", base.getDictionaryObject(COSName.EARLY_CHANGE), "1");
			}
		} else {
			res.put("Predictor", "1");
			res.put("Colors", "1");
			res.put("BitsPerComponent", "8");
			res.put("Columns", "1");
			if (isLWZ) {
				res.put("EarlyChange", "1");
			}
		}

		return res;
	}

	private static void putIntegerWithDefault(Map<String, Object> map, String key, Object value, String defaultValue) {
		if (value instanceof COSInteger) {
			map.put(key, String.valueOf(((COSInteger) value).intValue()));
		} else {
			if (!(defaultValue == null)) {
				map.put(key, defaultValue);
			}
		}
	}

//	private static void putIfNotNull(Map<String, Object> map, String key, Object value) {
//		if (key != null && value != null) {
//			map.put(key, value);
//		}
//	}

	private void parseParents(FeatureTreeNode root) throws FeaturesTreeNodeException {
		if ((pageParent != null && !pageParent.isEmpty()) ||
				(patternParent != null && !patternParent.isEmpty()) ||
				(xobjectParent != null && !xobjectParent.isEmpty()) ||
				(fontParent != null && !fontParent.isEmpty())) {
			FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);

			PBCreateNodeHelper.parseIDSet(pageParent, "page", null, parents);
			PBCreateNodeHelper.parseIDSet(patternParent, "pattern", null, parents);
			PBCreateNodeHelper.parseIDSet(xobjectParent, "xobject", null, parents);
			PBCreateNodeHelper.parseIDSet(fontParent, "font", null, parents);
		}
	}
}
