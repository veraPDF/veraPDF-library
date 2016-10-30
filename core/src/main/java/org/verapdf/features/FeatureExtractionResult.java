package org.verapdf.features;

import java.util.*;

import org.verapdf.features.tools.FeatureTreeNode;

/**
 * Features Collection
 *
 * @author Maksim Bezrukov
 */
public final class FeatureExtractionResult {

	private final Map<FeatureObjectType, FeaturesStructure> collection;

	/**
	 * Constructs new object
	 */
	public FeatureExtractionResult() {
		this.collection = new EnumMap<>(FeatureObjectType.class);
	}

	/**
	 * Add new feature tree for a type
	 *
	 * @param type type of feature object
	 * @param root root element of a feature tree
	 */
	public void addNewFeatureTree(FeatureObjectType type, FeatureTreeNode root) {
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
	public List<FeatureTreeNode> getFeatureTreesForType(FeatureObjectType type) {
		FeaturesStructure list = this.collection.get(type);
		return (list == null || list.list == null) ? Collections.<FeatureTreeNode>emptyList() : list.list;
	}

	/**
	 * Add new error to feature type
	 *
	 * @param type    type of feature object
	 * @param errorID errorID
	 */
	public void addNewError(FeatureObjectType type, String errorID) {
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
	public List<String> getErrorsForType(FeatureObjectType type) {
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

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof FeaturesStructure)) return false;

			FeaturesStructure that = (FeaturesStructure) o;

			if (list != null ? !list.equals(that.list) : that.list != null) return false;
			return errors != null ? errors.equals(that.errors) : that.errors == null;

		}

		@Override
		public int hashCode() {
			int result = list != null ? list.hashCode() : 0;
			result = 31 * result + (errors != null ? errors.hashCode() : 0);
			return result;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FeatureExtractionResult)) return false;

		FeatureExtractionResult that = (FeatureExtractionResult) o;

		return collection != null ? collection.equals(that.collection) : that.collection == null;

	}

	@Override
	public int hashCode() {
		return collection != null ? collection.hashCode() : 0;
	}
}
