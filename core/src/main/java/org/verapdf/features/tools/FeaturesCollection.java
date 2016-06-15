package org.verapdf.features.tools;

import org.verapdf.features.FeaturesObjectTypesEnum;

import java.util.*;

/**
 * Features Collection
 *
 * @author Maksim Bezrukov
 */
public class FeaturesCollection {

	private Map<FeaturesObjectTypesEnum, FeaturesStructure> collection;

	/**
	 * Constructs new object
	 */
	public FeaturesCollection() {
		this.collection = new EnumMap<>(FeaturesObjectTypesEnum.class);
	}

	/**
	 * Add new feature tree for a type
	 *
	 * @param type type of feature object
	 * @param root root element of a feature tree
	 */
	public void addNewFeatureTree(FeaturesObjectTypesEnum type, FeatureTreeNode root) {
		FeaturesStructure list = this.collection.get(type);

		if (list == null) {
			list = new FeaturesStructure();
			this.collection.put(type, list);
		}
		if (list.list == null) {
			list.list = new ArrayList<>();
		}
		list.list.add(root);
	}

	/**
	 * Gets list of feature trees for the type
	 *
	 * @param type type of the feature object
	 * @return list of feature trees for the given type
	 */
	public List<FeatureTreeNode> getFeatureTreesForType(FeaturesObjectTypesEnum type) {
		FeaturesStructure list = this.collection.get(type);
		return (list == null || list.list == null) ? Collections.<FeatureTreeNode>emptyList() : list.list;
	}

	/**
	 * Add new error to feature type
	 *
	 * @param type    type of feature object
	 * @param errorID errorID
	 */
	public void addNewError(FeaturesObjectTypesEnum type, String errorID) {
		FeaturesStructure list = this.collection.get(type);

		if (list == null) {
			list = new FeaturesStructure();
			this.collection.put(type, list);
		}
		if (list.errors == null) {
			list.errors = new ArrayList<>();
		}
		list.errors.add(errorID);
	}

	/**
	 * Gets list of feature trees for the type
	 *
	 * @param type type of the feature object
	 * @return list of errorss for the given type
	 */
	public List<String> getErrorsForType(FeaturesObjectTypesEnum type) {
		FeaturesStructure list = this.collection.get(type);
		return (list == null || list.errors == null) ? Collections.<String>emptyList() : list.errors;
	}

	private class FeaturesStructure {
		List<FeatureTreeNode> list;
		List<String> errors;

		public FeaturesStructure() {
			this.list = null;
			this.errors = null;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		FeaturesCollection that = (FeaturesCollection) o;

		return collection != null ? collection.equals(that.collection) : that.collection == null;

	}

	@Override
	public int hashCode() {
		return collection != null ? collection.hashCode() : 0;
	}
}
