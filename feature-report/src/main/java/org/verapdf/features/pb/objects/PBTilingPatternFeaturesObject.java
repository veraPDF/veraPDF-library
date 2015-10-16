package org.verapdf.features.pb.objects;

import org.apache.pdfbox.pdmodel.graphics.pattern.PDTilingPattern;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.Set;

/**
 * Feature object for tilling pattern
 *
 * @author Maksim Bezrukov
 */
public class PBTilingPatternFeaturesObject implements IFeaturesObject {

	private static final String ID = "id";

	private PDTilingPattern tilingPattern;
	private String id;
	private Set<String> extGStateChild;
	private Set<String> colorSpaceChild;
	private Set<String> patternChild;
	private Set<String> shadingChild;
	private Set<String> xobjectChild;
	private Set<String> fontChild;
	private Set<String> propertiesChild;
	private Set<String> pageParent;
	private Set<String> patternParent;
	private Set<String> xobjectParent;
	private Set<String> fontParent;

	/**
	 * Constructs new tilling pattern features object
	 *
	 * @param tilingPattern   PDTilingPattern which represents tilling pattern for feature report
	 * @param id              id of the object
	 * @param extGStateChild  set of external graphics state id which contains in resource dictionary of this pattern
	 * @param colorSpaceChild set of ColorSpace id which contains in resource dictionary of this pattern
	 * @param patternChild    set of pattern id which contains in resource dictionary of this pattern
	 * @param shadingChild    set of shading id which contains in resource dictionary of this pattern
	 * @param xobjectChild    set of XObject id which contains in resource dictionary of this pattern
	 * @param fontChild       set of font id which contains in resource dictionary of this pattern
	 * @param propertiesChild set of properties id which contains in resource dictionary of this pattern
	 * @param pageParent      set of page ids which contains the given pattern as its resources
	 * @param patternParent   set of pattern ids which contains the given pattern as its resources
	 * @param xobjectParent   set of xobject ids which contains the given pattern as its resources
	 * @param fontParent      set of font ids which contains the given pattern as its resources
	 */
	public PBTilingPatternFeaturesObject(PDTilingPattern tilingPattern, String id, Set<String> extGStateChild, Set<String> colorSpaceChild, Set<String> patternChild, Set<String> shadingChild, Set<String> xobjectChild, Set<String> fontChild, Set<String> propertiesChild, Set<String> pageParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
		this.tilingPattern = tilingPattern;
		this.id = id;
		this.extGStateChild = extGStateChild;
		this.colorSpaceChild = colorSpaceChild;
		this.patternChild = patternChild;
		this.shadingChild = shadingChild;
		this.xobjectChild = xobjectChild;
		this.fontChild = fontChild;
		this.propertiesChild = propertiesChild;
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
	 * @throws FeaturesTreeNodeException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeaturesCollection collection) throws FeaturesTreeNodeException {
		if (tilingPattern != null) {
			FeatureTreeNode root = FeatureTreeNode.newRootInstance("pattern");
			root.addAttribute(ID, id);
			root.addAttribute("type", "tiling");

			parseParents(root);

			FeatureTreeNode.newChildInstanceWithValue("paintType", String.valueOf(tilingPattern.getPaintType()), root);
			FeatureTreeNode.newChildInstanceWithValue("tilingType", String.valueOf(tilingPattern.getTilingType()), root);

			PBCreateNodeHelper.addBoxFeature("bbox", tilingPattern.getBBox(), root);

			FeatureTreeNode.newChildInstanceWithValue("xStep", String.valueOf(tilingPattern.getXStep()), root);
			FeatureTreeNode.newChildInstanceWithValue("yStep", String.valueOf(tilingPattern.getYStep()), root);

			parseFloatMatrix(tilingPattern.getMatrix().getValues(), FeatureTreeNode.newChildInstance("matrix", root));

			parseResources(root);

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

	private void parseFloatMatrix(float[][] array, FeatureTreeNode parent) throws FeaturesTreeNodeException {
		for (int i = 0; i < array.length; ++i) {
			for (int j = 0; j < array.length - 1; ++j) {
				FeatureTreeNode element = FeatureTreeNode.newChildInstance("element", parent);
				element.addAttribute("row", String.valueOf(i + 1));
				element.addAttribute("column", String.valueOf(j + 1));
				element.addAttribute("value", String.valueOf(array[i][j]));
			}
		}
	}

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

	private void parseResources(FeatureTreeNode root) throws FeaturesTreeNodeException {

		if ((extGStateChild != null && !extGStateChild.isEmpty()) ||
				(colorSpaceChild != null && !colorSpaceChild.isEmpty()) ||
				(patternChild != null && !patternChild.isEmpty()) ||
				(shadingChild != null && !shadingChild.isEmpty()) ||
				(xobjectChild != null && !xobjectChild.isEmpty()) ||
				(fontChild != null && !fontChild.isEmpty()) ||
				(propertiesChild != null && !propertiesChild.isEmpty())) {
			FeatureTreeNode resources = FeatureTreeNode.newChildInstance("resources", root);

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
