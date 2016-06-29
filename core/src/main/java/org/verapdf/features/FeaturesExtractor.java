package org.verapdf.features;

import org.verapdf.features.tools.FeatureTreeNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for features extractors
 *
 * @author Maksim Bezrukov
 */
public abstract class FeaturesExtractor {
	private final FeaturesObjectTypesEnum type;
	private ExtractorDetails details = new ExtractorDetails();
	private Map<String, String> attributes = new HashMap<>();

	FeaturesExtractor(final FeaturesObjectTypesEnum type) {
		this.type = type;
	}

	public final void initialize(ExtractorDetails details) {
		initialize(details, Collections.<String, String>emptyMap());
	}

	public final void initialize(ExtractorDetails details, Map<String, String> attributes) {
		if (details == null) {
			throw new IllegalArgumentException("Argument details shall not be null");
		}
		if (attributes == null) {
			throw new IllegalArgumentException("Argument attributes shall not be null");
		}
		this.details = details;
		this.attributes = new HashMap<>(attributes);
	}

	/**
	 * Extract features from features data
	 *
	 * @param data features data for extractor
	 * @return list of roots for extracted data tree
	 */
	abstract List<FeatureTreeNode> getFeatures(FeaturesData data);

	/**
	 * @return type of object for which this extractor applies
	 */
	public final FeaturesObjectTypesEnum getType() {
		return this.type;
	}

	/**
	 * @return extractor details
	 */
	public ExtractorDetails getDetails() {
		return details;
	}

	/**
	 * @return a map of attributes
	 */
	public Map<String, String> getAttributes() {
		return Collections.unmodifiableMap(this.attributes);
	}

	/**
	 * Extractor details
	 */
	public static final class ExtractorDetails {
		private final String name;
		private final String version;
		private final String description;

		/**
		 * @param name        the name of the extractor
		 * @param version     the version of the extractor
		 * @param description the description of the extractor
		 */
		public ExtractorDetails(String name, String version, String description) {
			this.name = name;
			this.version = version;
			this.description = description;
		}

		public ExtractorDetails() {
			this("", "", "");
		}

		/**
		 * @return the name of the extractor
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the version of the extractor
		 */
		public String getVersion() {
			return version;
		}

		/**
		 * @return the description of the extractor
		 */
		public String getDescription() {
			return description;
		}
	}

}
