package org.verapdf.features.pb.objects;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.color.*;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Features object for ColorSpace
 *
 * @author Maksim Bezrukov
 */
public class PBColorSpaceFeaturesObject implements IFeaturesObject {

	private static final String ID = "id";

	private static final Logger LOGGER = Logger
			.getLogger(PBColorSpaceFeaturesObject.class);

	private PDColorSpace colorSpace;
	private String id;
	private String iccProfileChild;
	private String colorSpaceChild;
	private Set<String> pageParents;
	private Set<String> colorSpaceParents;
	private Set<String> patternParents;
	private Set<String> shadingParents;
	private Set<String> xobjectParents;
	private Set<String> fontParents;

	/**
	 * Constructs new colorspace features object
	 *
	 * @param colorSpace        PDColorSpace which represents colorspace for feature report
	 * @param id                id of the object
	 * @param iccProfileChild   id of the iccprofile child
	 * @param colorSpaceChild   id of the colorspace child
	 * @param pageParents       set of page ids which contains the given colorspace as its resources
	 * @param colorSpaceParents set of colorspace ids which contains the given colorspace as its resources
	 * @param patternParents    set of pattern ids which contains the given colorspaceas its resources
	 * @param shadingParents    set of shading ids which contains the given colorspaceas its resources
	 * @param xobjectParents    set of xobject ids which contains the given colorspace as its resources
	 * @param fontParents       set of font ids which contains the given colorspace as its resources
	 */
	public PBColorSpaceFeaturesObject(PDColorSpace colorSpace,
									  String id,
									  String iccProfileChild,
									  String colorSpaceChild,
									  Set<String> pageParents,
									  Set<String> colorSpaceParents,
									  Set<String> patternParents,
									  Set<String> shadingParents,
									  Set<String> xobjectParents,
									  Set<String> fontParents) {
		this.colorSpace = colorSpace;
		this.id = id;
		this.iccProfileChild = iccProfileChild;
		this.colorSpaceChild = colorSpaceChild;
		this.pageParents = pageParents;
		this.colorSpaceParents = colorSpaceParents;
		this.patternParents = patternParents;
		this.shadingParents = shadingParents;
		this.xobjectParents = xobjectParents;
		this.fontParents = fontParents;
	}

	/**
	 * @return DOCUMENT_SECURITY instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.COLORSPACE;
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
		if (colorSpace != null) {
			FeatureTreeNode root = FeatureTreeNode.newRootInstance("colorSpace");

			root.addAttribute(ID, id);
			root.addAttribute("family", colorSpace.getName());

			parseParents(root);

			if (colorSpace instanceof PDCIEDictionaryBasedColorSpace) {
				parseCIEDictionaryBased(root);
			} else if (colorSpace instanceof PDICCBased) {
				PDICCBased icc = (PDICCBased) colorSpace;
				if (colorSpaceChild != null) {
					FeatureTreeNode alt = FeatureTreeNode.newChildInstance("alternate", root);
					alt.addAttribute(ID, colorSpaceChild);
				}
				FeatureTreeNode.newChildInstanceWithValue("components", String.valueOf(icc.getNumberOfComponents()), root);
				if (iccProfileChild != null) {
					FeatureTreeNode prof = FeatureTreeNode.newChildInstance("iccProfile", root);
					prof.addAttribute(ID, iccProfileChild);
				}
			} else if (colorSpace instanceof PDIndexed) {
				parseIndexed(root, collection);
			} else if (colorSpace instanceof PDSeparation) {
				PDSeparation sep = (PDSeparation) colorSpace;
				if (colorSpaceChild != null) {
					FeatureTreeNode alt = FeatureTreeNode.newChildInstance("alternate", root);
					alt.addAttribute(ID, colorSpaceChild);
				}
				PBCreateNodeHelper.addNotEmptyNode("colorantName", sep.getColorantName(), root);
			} else if (colorSpace instanceof PDDeviceN) {
				PDDeviceN devN = (PDDeviceN) colorSpace;
				if (colorSpaceChild != null) {
					FeatureTreeNode alt = FeatureTreeNode.newChildInstance("alternate", root);
					alt.addAttribute(ID, colorSpaceChild);
				}
				if (devN.getColorantNames() != null) {
					parseStringList(devN.getColorantNames(), FeatureTreeNode.newChildInstance("colorantNames", root));
				}
			}

			collection.addNewFeatureTree(FeaturesObjectTypesEnum.COLORSPACE, root);
			return root;
		}

		return null;
	}

	/**
	 * @return null
	 */
	@Override
	public FeaturesData getData() {
		return null;
	}

	private void parseIndexed(FeatureTreeNode root, FeaturesCollection collection) throws FeaturesTreeNodeException {
		PDIndexed index = (PDIndexed) colorSpace;

		if (colorSpaceChild != null) {
			FeatureTreeNode alt = FeatureTreeNode.newChildInstance("base", root);
			alt.addAttribute(ID, colorSpaceChild);
		}

		if (index.getCOSObject() instanceof COSArray) {
			FeatureTreeNode hival = FeatureTreeNode.newChildInstance("hival",
					root);
			if (((COSArray) index.getCOSObject()).size() > 3 &&
					((COSArray) index.getCOSObject()).getObject(2) instanceof COSNumber) {
				hival.setValue(String.valueOf(((COSNumber) ((COSArray) index.getCOSObject()).getObject(2)).intValue()));
			} else {
				ErrorsHelper.addErrorIntoCollection(collection,
						hival,
						"Indexed color space has no element hival or hival is not a number");
			}

			FeatureTreeNode lookup = FeatureTreeNode.newChildInstance("lookup",
					root);
			if (((COSArray) index.getCOSObject()).size() > 4) {
				byte[] lookupData = null;
				COSBase lookupTable = ((COSArray) index.getCOSObject()).getObject(3);
				if (lookupTable instanceof COSString) {
					lookupData = ((COSString) lookupTable).getBytes();
				} else if (lookupTable instanceof COSStream) {
					try {
						lookupData = (new PDStream((COSStream) lookupTable)).getByteArray();
					} catch (IOException e) {
						LOGGER.info(e);
						ErrorsHelper.addErrorIntoCollection(collection,
								lookup,
								e.getMessage());
					}
				} else {
					ErrorsHelper.addErrorIntoCollection(collection,
							lookup,
							"Indexed color space has element lookup but it is not a String or a stream");
				}

				if (lookupData != null) {
					lookup.setValue(DatatypeConverter.printHexBinary(lookupData));
				}
			} else {
				ErrorsHelper.addErrorIntoCollection(collection,
						lookup,
						"Indexed color space has no element lookup");
			}
		} else {
			ErrorsHelper.addErrorIntoCollection(collection,
					root,
					"Indexed color space is not an array");
		}
	}


	private void parseCIEDictionaryBased(FeatureTreeNode root) throws FeaturesTreeNodeException {
		PDCIEDictionaryBasedColorSpace cie = (PDCIEDictionaryBasedColorSpace) colorSpace;

		parseTristimulus(cie.getWhitepoint(), FeatureTreeNode.newChildInstance("whitePoint", root));
		parseTristimulus(cie.getBlackPoint(), FeatureTreeNode.newChildInstance("blackPoint", root));

		if (cie instanceof PDCalGray) {
			PDCalGray calGray = (PDCalGray) cie;
			FeatureTreeNode.newChildInstanceWithValue("gamma", String.valueOf(calGray.getGamma()), root);
		} else if (cie instanceof PDCalRGB) {
			PDCalRGB calRGB = (PDCalRGB) cie;
			FeatureTreeNode gamma = FeatureTreeNode.newChildInstance("gamma", root);
			PDGamma pdGamma = calRGB.getGamma();
			FeatureTreeNode.newChildInstanceWithValue("red", String.valueOf(pdGamma.getR()), gamma);
			FeatureTreeNode.newChildInstanceWithValue("green", String.valueOf(pdGamma.getG()), gamma);
			FeatureTreeNode.newChildInstanceWithValue("blue", String.valueOf(pdGamma.getB()), gamma);
			parseFloatArray(calRGB.getMatrix(), FeatureTreeNode.newChildInstance("matrix", root));
		} else if (cie instanceof PDLab) {
			PDLab lab = (PDLab) cie;
			FeatureTreeNode range = FeatureTreeNode.newChildInstance("range", root);
			range.addAttribute("aMin", String.valueOf(lab.getARange().getMin()));
			range.addAttribute("aMax", String.valueOf(lab.getARange().getMax()));
			range.addAttribute("bMin", String.valueOf(lab.getBRange().getMin()));
			range.addAttribute("bMax", String.valueOf(lab.getBRange().getMax()));
		}

	}

	private void parseFloatArray(float[] array, FeatureTreeNode parent) throws FeaturesTreeNodeException {
		for (int i = 0; i < array.length; ++i) {
			FeatureTreeNode element = FeatureTreeNode.newChildInstance("element", parent);
			element.addAttribute("number", String.valueOf(i));
			element.addAttribute("value", String.valueOf(array[i]));
		}
	}

	private void parseStringList(List<String> array, FeatureTreeNode parent) throws FeaturesTreeNodeException {
		for (int i = 0; i < array.size(); ++i) {
			FeatureTreeNode element = FeatureTreeNode.newChildInstance("element", parent);
			element.addAttribute("number", String.valueOf(i));
			element.addAttribute("value", String.valueOf(array.get(i)));
		}
	}

	private void parseTristimulus(PDTristimulus tris, FeatureTreeNode curNode) {
		curNode.addAttribute("x", String.valueOf(tris.getX()));
		curNode.addAttribute("y", String.valueOf(tris.getY()));
		curNode.addAttribute("z", String.valueOf(tris.getZ()));
	}

	private void parseParents(FeatureTreeNode root) throws FeaturesTreeNodeException {
		if ((pageParents != null && !pageParents.isEmpty()) ||
				(colorSpaceParents != null && !colorSpaceParents.isEmpty()) ||
				(patternParents != null && !patternParents.isEmpty()) ||
				(xobjectParents != null && !xobjectParents.isEmpty()) ||
				(shadingParents != null && !shadingParents.isEmpty()) ||
				(fontParents != null && !fontParents.isEmpty())) {
			FeatureTreeNode parents = FeatureTreeNode.newChildInstance("parents", root);

			PBCreateNodeHelper.parseIDSet(pageParents, "page", null, parents);
			PBCreateNodeHelper.parseIDSet(colorSpaceParents, "colorSpace", null, parents);
			PBCreateNodeHelper.parseIDSet(patternParents, "pattern", null, parents);
			PBCreateNodeHelper.parseIDSet(shadingParents, "shading", null, parents);
			PBCreateNodeHelper.parseIDSet(xobjectParents, "xobject", null, parents);
			PBCreateNodeHelper.parseIDSet(fontParents, "font", null, parents);
		}
	}
}
