package org.verapdf.features.objects;

import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.tools.CreateNodeHelper;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class InteractiveFormFieldFeaturesObject extends FeaturesObject {

	private static final String INTERACTIVE_FORM_FIELD = "interactiveFormField";
	private static final String NAME = "fullyQualifiedName";
	private static final String VALUE = "value";
	private static final String CHILDREN = "children";

	/**
	 * Constructs new Annotation Feature Object
	 *
	 * @param adapter class represents annotation adapter
	 */
	public InteractiveFormFieldFeaturesObject(InteractiveFormFieldFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	/**
	 * @return INTERACTIVE_FORM_FIELDS instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.INTERACTIVE_FORM_FIELDS;
	}

	/**
	 * Reports all features from the object into the collection
	 *
	 * @return FeatureTreeNode class which represents a root node of the
	 * constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode collectFeatures() throws FeatureParsingException {
		InteractiveFormFieldFeaturesObjectAdapter formFieldAdapter = (InteractiveFormFieldFeaturesObjectAdapter) this.adapter;
		FeatureTreeNode root = FeatureTreeNode.createRootNode(INTERACTIVE_FORM_FIELD);
		createItem(root, formFieldAdapter);
		return root;
	}

	private void createItem(FeatureTreeNode root, InteractiveFormFieldFeaturesObjectAdapter formFieldAdapter) throws FeatureParsingException {
		CreateNodeHelper.addNotEmptyNode(NAME, formFieldAdapter.getFullyQualifiedName(), root);
		CreateNodeHelper.addNotEmptyNode(VALUE, formFieldAdapter.getValue(), root);
		List<InteractiveFormFieldFeaturesObjectAdapter> children = formFieldAdapter.getChildren();
		if (children != null && !children.isEmpty()) {
			FeatureTreeNode childrenRoot = root.addChild(CHILDREN);
			for (InteractiveFormFieldFeaturesObjectAdapter child : children) {
				FeatureTreeNode childRoot = childrenRoot.addChild(INTERACTIVE_FORM_FIELD);
				createItem(childRoot, child);
			}
		}
	}

	/**
	 * @return null
	 */
	@Override
	public FeaturesData getData() {
		return null;
	}

	static List<Feature> getFeaturesList() {
		// Only errors of top level node
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(INTERACTIVE_FORM_FIELD, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}
}
