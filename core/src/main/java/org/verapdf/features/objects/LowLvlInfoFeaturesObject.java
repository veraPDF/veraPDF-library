package org.verapdf.features.objects;

import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.FeatureExtractionResult;
import org.verapdf.features.FeatureObjectType;
import org.verapdf.features.FeaturesData;
import org.verapdf.features.IFeaturesObject;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Feature object for low level info part of the features report
 *
 * @author Maksim Bezrukov
 */
public class LowLvlInfoFeaturesObject implements IFeaturesObject {

	private static List<Feature> featuresList;

	private LowLvlInfoFeaturesObjectAdapter adapter;

	/**
	 * Constructs new low level info feature object.
	 *
	 * @param adapter low lvl info adapter class represents document object
	 */
	public LowLvlInfoFeaturesObject(LowLvlInfoFeaturesObjectAdapter adapter) {
		this.adapter = adapter;
	}

	/**
	 * @return LOW_LVL_INFO instance of the FeatureObjectType enumeration
	 */
	@Override
	public FeatureObjectType getType() {
		return FeatureObjectType.LOW_LEVEL_INFO;
	}

	/**
	 * Reports all features from the object into the collection
	 *
	 * @param collection collection for feature report
	 * @return FeatureTreeNode class which represents a root node of the
	 * constructed collection tree
	 * @throws FeatureParsingException occurs when wrong features tree node constructs
	 */
	@Override
	public FeatureTreeNode reportFeatures(FeatureExtractionResult collection) throws FeatureParsingException {
		FeatureTreeNode root = FeatureTreeNode.createRootNode("lowLevelInfo");

		root.addChild("indirectObjectsNumber")
				.setValue(String.valueOf(adapter.getIndirectObjectsNumber()));

		String creationId = adapter.getCreationId();
		String modificationId = adapter.getModificationId();

		if (creationId != null || modificationId != null) {
			FeatureTreeNode documentId = root.addChild("documentId");
			if (creationId != null) {
				documentId.setAttribute("creationId", creationId);
			}
			if (modificationId != null) {
				documentId.setAttribute("modificationId", modificationId);
			}
		}

		Set<String> filters = adapter.getFilters();

		if (!filters.isEmpty()) {
			FeatureTreeNode filtersNode = root.addChild("filters");

			for (String filter : filters) {
				if (filter != null) {
					FeatureTreeNode filterNode = filtersNode.addChild("filter");
					filterNode.setAttribute("name", filter);
				}
			}
		}

		// TODO: Next code lines should be in parent abstract class implementation
		// when IFeaturesObject type will be changed from interface to abstract class
		List<String> errors = adapter.getErrors();
		if (!errors.isEmpty()) {
			for (String error : errors) {
				ErrorsHelper.addErrorIntoCollection(collection, root, error);
			}
		}
		collection.addNewFeatureTree(FeatureObjectType.LOW_LEVEL_INFO, root);
		return root;
	}

	/**
	 * @return null
	 */
	@Override
	public FeaturesData getData() {
		return null;
	}

	// TODO: for next method should be created abstract implementation in parent abstract class
	// in this method we should register all features with their names,
	// XPath related to feature root node and feature type
	static void registerAllFeatures() {
		registerFeature("Indirect Objects Number", "/indirectObjectsNumber", Feature.FeatureType.NUMBER);
		registerFeature("Creation ID", "/documentId/@creationId", Feature.FeatureType.STRING);
		registerFeature("Modification ID", "/documentId/@modificationId", Feature.FeatureType.STRING);
		registerFeature("Filter Name", "/filters/filter/@name", Feature.FeatureType.STRING);
	}

	// TODO: next two methods should be in parent abstract class implementation
	public static List<Feature> getFeaturesMap() {
		if (featuresList == null) {
			featuresList = new ArrayList<>();
			registerAllFeatures();
			registerFeature("Error IDs", "/@errorId", Feature.FeatureType.STRING);
		}
		return Collections.unmodifiableList(featuresList);
	}

	static void registerFeature(String featureName, String featureXPath, Feature.FeatureType featureType) {
		featuresList.add(new Feature(featureName, featureXPath, featureType));
	}

}
