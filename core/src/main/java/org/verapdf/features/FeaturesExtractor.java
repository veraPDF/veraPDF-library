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

	private Path pluginFolder;

	final void initialize(Path pluginFolderPath) {
		this.pluginFolder = pluginFolderPath;
	}

	/**
	 * @return path to the plugin folder
	 */
	final protected Path getFolderPath() {
		return this.pluginFolder;
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
	abstract FeaturesObjectTypesEnum getType();

	/**
	 * @return ID of the plugin
	 */
	public abstract String getID();

	/**
	 * @return Description of the plugin
	 */
	public abstract String getDescription();

}
