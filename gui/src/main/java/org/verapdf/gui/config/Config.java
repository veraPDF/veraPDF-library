package org.verapdf.gui.config;

import java.nio.file.Path;

/**
 * @author Maksim Bezrukov
 */
public final class Config {

	private final int processingType;
	private final boolean showPassedRules;
	private final int maxNumberOfFailedChecks;
	private final int maxNumberOfDisplayedFailedChecks;
	private final Path featuresPluginsConfigFilePath;
	private final boolean fixMetadata;
	private final boolean useSelectedPathForFixer;
	private final String metadataFixerPrefix;
	private final Path fixMetadataPathFolder;

	Config(int processingType, boolean showPassedRules, int maxNumberOfFailedChecks, int maxNumberOfDisplayedFailedChecks, Path featuresPluginsConfigFilePath, boolean fixMetadata, boolean useSelectedPathForFixer, String metadataFixerPrefix, Path fixMetadataPathFolder) {
		this.processingType = processingType;
		this.showPassedRules = showPassedRules;
		this.maxNumberOfFailedChecks = maxNumberOfFailedChecks;
		this.maxNumberOfDisplayedFailedChecks = maxNumberOfDisplayedFailedChecks;
		this.featuresPluginsConfigFilePath = featuresPluginsConfigFilePath;
		this.fixMetadata = fixMetadata;
		this.useSelectedPathForFixer = useSelectedPathForFixer;
		this.metadataFixerPrefix = metadataFixerPrefix;
		this.fixMetadataPathFolder = fixMetadataPathFolder;
	}

	/**
	 * @return integer that indicates selected processing type
	 */
	public int getProcessingType() {
		return processingType;
	}

	/**
	 * @return selected number for maximum displayed fail checks for a rule. If not selected returns -1
	 */
	public int getMaxNumberOfDisplayedFailedChecks() {
		return maxNumberOfDisplayedFailedChecks;
	}

	/**
	 * @return selected number for maximum fail checks for a rule. If not selected returns -1
	 */
	public int getMaxNumberOfFailedChecks() {
		return maxNumberOfFailedChecks;
	}

	/**
	 * @return true if desplay passed pules option selected
	 */
	public boolean isShowPassedRules() {
		return showPassedRules;
	}

	/**
	 * @return selected features report plugins config file's path
	 */
	public Path getFeaturesPluginsConfigFilePath() {
		return featuresPluginsConfigFilePath;
	}

	/**
	 * @return true if metadata fixing is on
	 */
	public boolean isFixMetadata() {
		return fixMetadata;
	}

	/**
	 * @return String representation of prefix for fixed files
	 */
	public String getMetadataFixerPrefix() {
		return metadataFixerPrefix;
	}

	/**
	 * @return path to the folder in which fixed file will be placed
	 */
	public Path getFixMetadataPathFolder() {
		return fixMetadataPathFolder;
	}

	/**
	 * @return true if use selected path for fixer is on
	 */
	public boolean isUseSelectedPathForFixer() {
		return useSelectedPathForFixer;
	}
}
