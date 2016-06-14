package org.verapdf.features;

import org.verapdf.features.tools.FeatureTreeNode;

import java.nio.file.Path;
import java.util.List;

/**
 * Class for features extractors
 *
 * @author Maksim Bezrukov
 */
abstract class FeaturesExtractor {
	public final String DEFAULT_ID = FeaturesExtractor.class.getName();
	public final String DEFAULT_DESCRIPTION = "description";

	private final FeaturesObjectTypesEnum type;
	private final String id;
	private final String description;

	private Path pluginFolderPath;

	FeaturesExtractor(final FeaturesObjectTypesEnum type, final String id, final String description) {
		this.type = type;
		this.id = id;
		this.description = description;
	}

	final void initialize(Path pluginFolderPath) {
		this.pluginFolderPath = pluginFolderPath;
	}

	final protected Path getFolderPath() {
		return this.pluginFolderPath;
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
	 * @return ID of the plugin
	 */
	public final String getID() {
		return this.id;
	}

	/**
	 * @return Description of the plugin
	 */
	public final String getDescription() {
		return this.description;
	}

}
