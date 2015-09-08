package org.verapdf.features.tools;

import org.verapdf.features.FeaturesObjectTypesEnum;

import java.util.*;

/**
 * Features Collection
 *
 * @author Maksim Bezrukov
 */
public class FeaturesCollection {

	private Map<FeaturesObjectTypesEnum, List<FeatureTreeNode>> collection;

	/**
	 * Constructs new object
	 */
	public FeaturesCollection() {
		collection = new HashMap<>();
	}

	/**
	 * Add new feature tree for a type
	 *
	 * @param type type of feature object
	 * @param root root element of a feature tree
	 */
	public void addNewFeatureTree(FeaturesObjectTypesEnum type, FeatureTreeNode root) {
		List<FeatureTreeNode> list = collection.get(type);

		if (list == null) {
			list = new ArrayList<>();
			collection.put(type, list);
		}

		list.add(root);
	}

	/**
	 * Gets list of feature trees for the type
	 *
	 * @param type type of the feature object
	 * @return list of feature trees for the given type
	 */
	public List<FeatureTreeNode> getFeatureTreesForType(FeaturesObjectTypesEnum type) {
		List<FeatureTreeNode> list = collection.get(type);

        return list == null ? Collections.<FeatureTreeNode>emptyList() : list;
    }
}
