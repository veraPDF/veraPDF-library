package org.verapdf.features.objects;

import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Maksim Bezrukov
 */
public class ActionFeaturesObject extends FeaturesObject {

	private static final String ACTION = "action";
	private static final String TYPE = "type";
	private static final String LOCATION = "location";

	public ActionFeaturesObject(ActionFeaturesObjectAdapter adapter) {
		super(adapter);
	}

	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.ACTION;
	}

	@Override
	public FeatureTreeNode reportFeatures(FeatureExtractionResult collection) throws FeatureParsingException {
		this.errors.clear();
		if (this.adapter.isPDFObjectPresent()) {
			ActionFeaturesObjectAdapter actionAdapter = (ActionFeaturesObjectAdapter) this.adapter;
			String type = actionAdapter.getType();
			if (type == null) {
				return null;
			}
			FeatureTreeNode root = findRootForType(collection, type);
			if (root == null) {
				root = createNewRoot(collection, type);
			}
			addLocation(root, actionAdapter.getLocation());
			this.errors.addAll(this.adapter.getErrors());
			if (!this.errors.isEmpty()) {
				for (String error : this.errors) {
					ErrorsHelper.addErrorIntoCollection(collection, root, error);
				}
			}
			return root;
		}
		return null;
	}

	private void addLocation(FeatureTreeNode root, ActionFeaturesObjectAdapter.Location location) throws FeatureParsingException {
		if (location != null) {
			List<FeatureTreeNode> children = root.getChildren();
			for (FeatureTreeNode child : children) {
				if (LOCATION.equals(child.getName()) && location.getText().equals(child.getValue())) {
					return;
				}
			}
			FeatureTreeNode locationNode = root.addChild(LOCATION);
			locationNode.setValue(location.getText());
		}
	}

	private FeatureTreeNode createNewRoot(FeatureExtractionResult collection, String type) {
		FeatureTreeNode root = FeatureTreeNode.createRootNode(ACTION);
		root.setAttribute(TYPE, type);
		collection.addNewFeatureTree(getType(), root);
		return root;
	}

	private FeatureTreeNode findRootForType(FeatureExtractionResult collection, String type) {
		List<FeatureTreeNode> featureTreesForType = collection.getFeatureTreesForType(FeatureObjectType.ACTION);
		for (FeatureTreeNode node : featureTreesForType) {
			Map<String, String> attributes = node.getAttributes();
			String attrType = attributes.get(TYPE);
			if (type.equals(attrType)) {
				return node;
			}
		}
		return null;
	}

	@Override
	public FeaturesData getData() {
		return null;
	}

	static List<Feature> getFeaturesList() {
		// All fields are present
		List<Feature> featuresList = new ArrayList<>();
		featuresList.add(new Feature("Type",
				generateAttributeXPath(ACTION, TYPE), Feature.FeatureType.STRING));
		featuresList.add(new Feature("Location",
				generateAttributeXPath(ACTION, LOCATION), Feature.FeatureType.STRING));

		featuresList.add(new Feature("Error IDs",
				generateAttributeXPath(ACTION, ErrorsHelper.ERRORID), Feature.FeatureType.STRING));
		return featuresList;
	}
}
