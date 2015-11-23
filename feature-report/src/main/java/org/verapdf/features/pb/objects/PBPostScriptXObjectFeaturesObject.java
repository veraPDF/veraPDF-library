package org.verapdf.features.pb.objects;

import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.pb.tools.PBCreateNodeHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.util.Set;

/**
 * Features object for postscript xobject
 *
 * @author Maksim Bezrukov
 */
public class PBPostScriptXObjectFeaturesObject implements IFeaturesObject {

	private String id;
	private Set<String> pageParent;
	private Set<String> patternParent;
	private Set<String> xobjectParent;
	private Set<String> fontParent;

	/**
	 * Constructs new tilling pattern features object
	 *
	 * @param id            id of the object
	 * @param pageParent    set of page ids which contains the given xobject as its resources
	 * @param patternParent set of pattern ids which contains the given xobject as its resources
	 * @param xobjectParent set of xobject ids which contains the given xobject as its resources
	 * @param fontParent    set of font ids which contains the given xobject as its resources
	 */
	public PBPostScriptXObjectFeaturesObject(String id, Set<String> pageParent, Set<String> patternParent, Set<String> xobjectParent, Set<String> fontParent) {
		this.id = id;
		this.pageParent = pageParent;
		this.patternParent = patternParent;
		this.xobjectParent = xobjectParent;
		this.fontParent = fontParent;
	}

	/**
	 * @return POSTSCRIPT_XOBJECT instance of the FeaturesObjectTypesEnum enumeration
	 */
	@Override
	public FeaturesObjectTypesEnum getType() {
		return FeaturesObjectTypesEnum.POSTSCRIPT_XOBJECT;
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
		FeatureTreeNode root = FeatureTreeNode.createRootNode("xobject");
		root.setAttribute("type", "postscript");
		root.setAttribute("id", id);
		parseParents(root);

		collection.addNewFeatureTree(FeaturesObjectTypesEnum.POSTSCRIPT_XOBJECT, root);
		return root;
	}

	/**
	 * @return null
	 */
	@Override
	public FeaturesData getData() {
		return null;
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
