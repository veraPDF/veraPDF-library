package org.verapdf.features.pb.objects;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.Set;

/**
 * Feature object for form xobjects
 *
 * @author Maksim Bezrukov
 */
public class PBFormXObjectFeaturesObject implements IFeaturesObject {

	private static final String ID = "id";

	private PDFormXObject formXObject;
	private String id;
	private String groupColorSpaceChild;
	private Set<String> extGStateChild;
	private Set<String> colorSpaceChild;
	private Set<String> patternChild;
	private Set<String> shadingChild;
	private Set<String> xobjectChild;
	private Set<String> fontChild;
	private Set<String> propertiesChild;
	private Set<String> pageParent;
	private Set<String> annotationParent;
	private Set<String> patternParent;
	private Set<String> xobjectParent;
	private Set<String> fontParent;

	/**
	 * Constructs new form xobject features object
	 *
	 * @param formXObject          PDFormXObject which represents form xobject for feature report
	 * @param id                   id of the object
	 * @param groupColorSpaceChild id of the group xobject which contains in the given form xobject
	 * @param extGStateChild       set of external graphics state id which contains in resource dictionary of this xobject
	 * @param colorSpaceChild      set of ColorSpace id which contains in resource dictionary of this xobject
	 * @param patternChild         set of pattern id which contains in resource dictionary of this xobject
	 * @param shadingChild         set of shading id which contains in resource dictionary of this xobject
	 * @param xobjectChild         set of XObject id which contains in resource dictionary of this xobject
	 * @param fontChild            set of font id which contains in resource dictionary of this pattern
	 * @param propertiesChild      set of properties id which contains in resource dictionary of this xobject
	 * @param pageParent           set of page ids which contains the given xobject as its resources
	 * @param annotationParent     set of annotation ids which contains the given xobject in its appearance dictionary
	 * @param patternParent        set of pattern ids which contains the given xobject as its resources
	 * @param xobjectParent        set of xobject ids which contains the given xobject as its resources
	 * @param fontParent           set of font ids which contains the given xobject as its resources
	 */
	public PBFormXObjectFeaturesObject(PDFormXObject formXObject, String id, String groupColorSpaceChild, Set<String> extGStateChild, Set<String> colorSpaceChild, Set<String> patternChild, Set<String> shadingChild, Set<String> xobjectChild, Set<String> fontChild, Set<String> propertiesChild, Set<String> pageParent, Set<String> annotationParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
		this.formXObject = formXObject;
		this.id = id;
		this.groupColorSpaceChild = groupColorSpaceChild;
		this.extGStateChild = extGStateChild;
		this.colorSpaceChild = colorSpaceChild;
		this.patternChild = patternChild;
		this.shadingChild = shadingChild;
		this.xobjectChild = xobjectChild;
		this.fontChild = fontChild;
		this.propertiesChild = propertiesChild;
		this.pageParent = pageParent;
		this.annotationParent = annotationParent;
		this.patternParent = patternParent;
		this.xobjectParent = xobjectParent;
		this.fontParent = fontParent;
	}

	/**
	 * @return FORM_XOBJECT instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.FORM_XOBJECT;
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
		if (formXObject != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("xobject");
			root.setAttribute("type", "form");
			root.setAttribute(ID, id);

			parseParents(root);

			PBCreateNodeHelper.addBoxFeature("bbox", formXObject.getBBox(), root);
			parseFloatMatrix(formXObject.getMatrix().getValues(), FeatureTreeNode.createChildNode("matrix", root));

			if (formXObject.getGroup() != null) {
				FeatureTreeNode groupNode = FeatureTreeNode.createChildNode("group", root);
				if (formXObject.getGroup().getSubType() != null) {
					PBCreateNodeHelper.addNotEmptyNode("subtype", formXObject.getGroup().getSubType().getName(), groupNode);
					if ("Transparency".equals(formXObject.getGroup().getSubType().getName())) {
						if (groupColorSpaceChild != null) {
							FeatureTreeNode clr = FeatureTreeNode.createChildNode("colorSpace", groupNode);
							clr.setAttribute(ID, groupColorSpaceChild);
						}

						FeatureTreeNode.createChildNode("isolated", groupNode).setValue(String.valueOf(formXObject.getGroup().isIsolated()));
						FeatureTreeNode.createChildNode("knockout", groupNode).setValue(String.valueOf(formXObject.getGroup().isKnockout()));
					}

				}
			}

			if (formXObject.getCOSStream().getItem(COSName.STRUCT_PARENTS) != null) {
				FeatureTreeNode.createChildNode("structParents", root).setValue(String.valueOf(formXObject.getStructParents()));
			}


			COSBase cosBase = formXObject.getCOSStream().getDictionaryObject(COSName.METADATA);
			if (cosBase instanceof COSStream) {
				PDMetadata meta = new PDMetadata((COSStream) cosBase);
				PBCreateNodeHelper.parseMetadata(meta, "metadata", root, collection);
			}

			parseResources(root);

			collection.addNewFeatureTree(FeaturesObjectTypesEnum.FORM_XOBJECT, root);
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

	private static void parseFloatMatrix(float[][] array, FeatureTreeNode parent) throws FeatureParsingException {
		for (int i = 0; i < array.length; ++i) {
			for (int j = 0; j < array.length - 1; ++j) {
				FeatureTreeNode element = FeatureTreeNode.createChildNode("element", parent);
				element.setAttribute("row", String.valueOf(i + 1));
				element.setAttribute("column", String.valueOf(j + 1));
				element.setAttribute("value", String.valueOf(array[i][j]));
			}
		}
	}

	private void parseParents(FeatureTreeNode root) throws FeatureParsingException {
		if ((pageParent != null && !pageParent.isEmpty()) ||
				(annotationParent != null && !annotationParent.isEmpty()) ||
				(patternParent != null && !patternParent.isEmpty()) ||
				(xobjectParent != null && !xobjectParent.isEmpty()) ||
				(fontParent != null && !fontParent.isEmpty())) {
			FeatureTreeNode parents = FeatureTreeNode.createChildNode("parents", root);

			PBCreateNodeHelper.parseIDSet(pageParent, "page", null, parents);
			PBCreateNodeHelper.parseIDSet(annotationParent, "annotation", null, parents);
			PBCreateNodeHelper.parseIDSet(patternParent, "pattern", null, parents);
			PBCreateNodeHelper.parseIDSet(xobjectParent, "xobject", null, parents);
			PBCreateNodeHelper.parseIDSet(fontParent, "font", null, parents);
		}
	}

	private void parseResources(FeatureTreeNode root) throws FeatureParsingException {

		if ((extGStateChild != null && !extGStateChild.isEmpty()) ||
				(colorSpaceChild != null && !colorSpaceChild.isEmpty()) ||
				(patternChild != null && !patternChild.isEmpty()) ||
				(shadingChild != null && !shadingChild.isEmpty()) ||
				(xobjectChild != null && !xobjectChild.isEmpty()) ||
				(fontChild != null && !fontChild.isEmpty()) ||
				(propertiesChild != null && !propertiesChild.isEmpty())) {
			FeatureTreeNode resources = FeatureTreeNode.createChildNode("resources", root);

			PBCreateNodeHelper.parseIDSet(extGStateChild, "graphicsState", "graphicsStates", resources);
			PBCreateNodeHelper.parseIDSet(colorSpaceChild, "colorSpace", "colorSpaces", resources);
			PBCreateNodeHelper.parseIDSet(patternChild, "pattern", "patterns", resources);
			PBCreateNodeHelper.parseIDSet(shadingChild, "shading", "shadings", resources);
			PBCreateNodeHelper.parseIDSet(xobjectChild, "xobject", "xobjects", resources);
			PBCreateNodeHelper.parseIDSet(fontChild, "font", "fonts", resources);
			PBCreateNodeHelper.parseIDSet(propertiesChild, "propertiesDict", "propertiesDicts", resources);
		}
	}
}
