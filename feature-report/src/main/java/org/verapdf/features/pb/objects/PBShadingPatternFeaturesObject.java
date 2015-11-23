package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern;
import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.Set;

/**
 * Features object for shading pattern
 *
 * @author Maksim Bezrukov
 */
public class PBShadingPatternFeaturesObject implements IFeaturesObject {

	private static final String ID = "id";

	private PDShadingPattern shadingPattern;
	private String id;
	private String shadingChild;
	private String extGStateChild;
	private Set<String> pageParent;
	private Set<String> patternParent;
	private Set<String> xobjectParent;
	private Set<String> fontParent;

	/**
	 * Constructs new tilling pattern features object
	 *
	 * @param shadingPattern PDShadingPattern which represents shading pattern for feature report
	 * @param id             id of the object
	 * @param extGStateChild external graphics state id which contains in this shading pattern
	 * @param shadingChild   shading id which contains in this shading pattern
	 * @param pageParent     set of page ids which contains the given pattern as its resources
	 * @param patternParent  set of pattern ids which contains the given pattern as its resources
	 * @param xobjectParent  set of xobject ids which contains the given pattern as its resources
	 * @param fontParent     set of font ids which contains the given pattern as its resources
	 */
	public PBShadingPatternFeaturesObject(PDShadingPattern shadingPattern, String id, String shadingChild, String extGStateChild, Set<String> pageParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
		this.shadingPattern = shadingPattern;
		this.id = id;
		this.shadingChild = shadingChild;
		this.extGStateChild = extGStateChild;
		this.pageParent = pageParent;
		this.patternParent = patternParent;
		this.xobjectParent = xobjectParent;
		this.fontParent = fontParent;
	}

	/**
	 * @return PATTERN instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.PATTERN;
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
		if (shadingPattern != null) {
			FeatureTreeNode root = FeatureTreeNode.createRootNode("pattern");
			root.setAttribute(ID, id);
			root.setAttribute("type", "shading");

			parseParents(root);

			if (shadingChild != null) {
				FeatureTreeNode shading = FeatureTreeNode.createChildNode("shading", root);
				shading.setAttribute(ID, shadingChild);
			}

			parseFloatMatrix(shadingPattern.getMatrix().getValues(), FeatureTreeNode.createChildNode("matrix", root));

			if (extGStateChild != null) {
				FeatureTreeNode exGState = FeatureTreeNode.createChildNode("graphicsState", root);
				exGState.setAttribute(ID, extGStateChild);
			}

			collection.addNewFeatureTree(FeaturesObjectTypesEnum.PATTERN, root);
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

	private void parseFloatMatrix(float[][] array, FeatureTreeNode parent) throws FeatureParsingException {
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
