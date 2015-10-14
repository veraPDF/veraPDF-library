package org.verapdf.gui.config;

import org.verapdf.metadata.fixer.utils.FileGenerator;

import java.io.File;
import java.nio.file.FileSystems;
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
	private final String metadataFixerPrefix;
	private final Path fixMetadataPathFolder;

	private Config(int processingType, boolean showPassedRules, int maxNumberOfFailedChecks, int maxNumberOfDisplayedFailedChecks, Path featuresPluginsConfigFilePath, boolean fixMetadata, String metadataFixerPrefix, Path fixMetadataPathFolder) {
		this.processingType = processingType;
		this.showPassedRules = showPassedRules;
		this.maxNumberOfFailedChecks = maxNumberOfFailedChecks;
		this.maxNumberOfDisplayedFailedChecks = maxNumberOfDisplayedFailedChecks;
		this.featuresPluginsConfigFilePath = featuresPluginsConfigFilePath;
		this.fixMetadata = fixMetadata;
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

	public static final class Builder {

		private static final char[] FORBIDDEN_SYMBOLS_IN_FILE_NAME = new char[]{'\\', '/', ':', '*', '?', '\"', '<', '>', '|', '+', '\0', '%'};

		private static final int DEFAULT_PROCESSING_TYPE = 3;
		private static final boolean DEFAULT_SHOW_PASSED_RULES = false;
		private static final int DEFAULT_MAX_NUMBER_OF_FAILED_CHECKS = 100;
		private static final int DEFAULT_MAX_NUMBER_OF_DISPLAYED_FAILED_CHECKS = 100;
		private static final Path DEFAULT_FEATURES_PLUGINS_CONFIG_FILE_PATH = FileSystems.getDefault().getPath("");
		private static final boolean DEFAULT_FIX_METADATA = false;
		private static final String DEFAULT_METADATA_FIXER_PREFIX = FileGenerator.DEFAULT_PREFIX;
		private static final Path DEFAULT_FIX_METADATA_PATH_FOLDER = FileSystems.getDefault().getPath("");

		private static final Config DEFAULT_CONFIG = new Config(DEFAULT_PROCESSING_TYPE, DEFAULT_SHOW_PASSED_RULES, DEFAULT_MAX_NUMBER_OF_FAILED_CHECKS, DEFAULT_MAX_NUMBER_OF_DISPLAYED_FAILED_CHECKS, DEFAULT_FEATURES_PLUGINS_CONFIG_FILE_PATH, DEFAULT_FIX_METADATA, DEFAULT_METADATA_FIXER_PREFIX, DEFAULT_FIX_METADATA_PATH_FOLDER);

		private int processingType = DEFAULT_PROCESSING_TYPE;
		private boolean showPassedRules = DEFAULT_SHOW_PASSED_RULES;
		private int maxNumberOfFailedChecks = DEFAULT_MAX_NUMBER_OF_FAILED_CHECKS;
		private int maxNumberOfDisplayedFailedChecks = DEFAULT_MAX_NUMBER_OF_DISPLAYED_FAILED_CHECKS;
		private Path featuresPluginsConfigFilePath = DEFAULT_FEATURES_PLUGINS_CONFIG_FILE_PATH;
		private boolean fixMetadata = DEFAULT_FIX_METADATA;
		private String metadataFixerPrefix = DEFAULT_METADATA_FIXER_PREFIX;
		private Path fixMetadataPathFolder = DEFAULT_FIX_METADATA_PATH_FOLDER;

		public Builder() {
		}

		public Config build() {
			return new Config(this.processingType, this.showPassedRules, this.maxNumberOfFailedChecks, this.maxNumberOfDisplayedFailedChecks, this.featuresPluginsConfigFilePath, this.fixMetadata, this.metadataFixerPrefix, this.fixMetadataPathFolder);
		}

		public static Config buildDefaultConfig() {
			return DEFAULT_CONFIG;
		}

		public Builder fixMetadata(boolean fixMetadata) {
			this.fixMetadata = fixMetadata;
			return this;
		}

		/**
		 * Changes settings parameters
		 *
		 * @param metadataFixerPrefix a prefix which will be added to the fixed file
		 * @throws IllegalArgumentException parameter can not be null
		 */
		public Builder metadataFixerPrefix(String metadataFixerPrefix) {
			if (metadataFixerPrefix == null) {
				throw new IllegalArgumentException("Prefix for metadata fixer can not be null");
			}
			for (char c : metadataFixerPrefix.toCharArray()) {
				if (!isValidFileNameCharacter(c)) {
					throw new IllegalArgumentException("Prefix for metadata fixer contains forbidden symbols");
				}
			}
			this.metadataFixerPrefix = metadataFixerPrefix;
			return this;
		}

		/**
		 * Changes settings parameters
		 *
		 * @param processingType must be in range from 1 to 3
		 * @throws IllegalArgumentException if parameter is not an integer of range from 1 to 3
		 */
		public Builder processingType(int processingType) {
			if (processingType >= 1 && processingType <= 3) {
				this.processingType = processingType;
			} else {
				throw new IllegalArgumentException("Processing type must be an integer in range from 1 to 3");
			}
			return this;
		}

		/**
		 * Changes settings parameter
		 *
		 * @param showPassedRules true for show passed rules at the report
		 */
		public Builder showPassedRules(boolean showPassedRules) {
			this.showPassedRules = showPassedRules;
			return this;
		}

		/**
		 * Changes settings parameter
		 *
		 * @param maxNumberOfFailedChecks a natural number that indicates maximum number of failed checks for rule or -1 for unlimited
		 * @throws IllegalArgumentException if parameter is not a natural number or -1
		 */
		public Builder maxNumberOfFailedChecks(int maxNumberOfFailedChecks) {
			if (maxNumberOfFailedChecks > 0 || maxNumberOfFailedChecks == -1) {
				this.maxNumberOfFailedChecks = maxNumberOfFailedChecks;
			} else {
				throw new IllegalArgumentException("Max number of failed checks for rule for setter method is not a natural or -1");
			}
			return this;
		}

		/**
		 * Changes settings parameter
		 *
		 * @param maxNumberOfDisplayedFailedChecks a non negative integer number that indicates maximum number of displayed
		 *                                         failed checks for rule or -1 for infinite
		 * @throws IllegalArgumentException if parameter is less than -1
		 */
		public Builder maxNumberOfDisplayedFailedChecks(int maxNumberOfDisplayedFailedChecks) {
			if (maxNumberOfDisplayedFailedChecks >= -1) {
				this.maxNumberOfDisplayedFailedChecks = maxNumberOfDisplayedFailedChecks;
			} else {
				throw new IllegalArgumentException("Max number of displayed failed checks for rule for setter method is less than -1");
			}
			return this;
		}

		/**
		 * Changes settings parameter
		 *
		 * @param featuresPluginsConfigFilePath a path to the config file for features plugins
		 * @throws IllegalArgumentException parameter should be an empty path or a path to an existing file
		 */
		public Builder featuresPluginsConfigFilePath(Path featuresPluginsConfigFilePath) {
			if (isValidFilePath(featuresPluginsConfigFilePath)) {
				this.featuresPluginsConfigFilePath = featuresPluginsConfigFilePath;
				return this;
			} else {
				throw new IllegalArgumentException("Path should be an empty path or a path to an existing and read acceptable file");
			}
		}

		/**
		 * Changes settings parameters
		 *
		 * @param fixMetadataPathFolder a path to the folder in which fixed files will be saved
		 * @throws IllegalArgumentException parameter should be an empty path or a path to an existing and write acceptable directory
		 */
		public Builder fixMetadataPathFolder(Path fixMetadataPathFolder) {
			if (isValidFolderPath(fixMetadataPathFolder)) {
				this.fixMetadataPathFolder = fixMetadataPathFolder;
				return this;
			} else {
				throw new IllegalArgumentException("Path should be an empty path or a path to an existing and write acceptable directory");
			}
		}

		/**
		 * Checks is the parameter path a valid for saving fixed file
		 *
		 * @param path path for check
		 * @return true if it is valid
		 */
		public static boolean isValidFolderPath(Path path) {
			if (path == null) {
				return false;
			}
			File f = path.toFile();
			return path.toString().isEmpty() || (f.isDirectory() && f.canWrite());
		}

		/**
		 * Checks is the parameter path a valid for features plugins config file
		 *
		 * @param path path for check
		 * @return true if it is valid
		 */
		public static boolean isValidFilePath(Path path) {
			if (path == null) {
				return false;
			}
			File f = path.toFile();
			return path.toString().isEmpty() || (f.isFile() && f.canRead());
		}

		/**
		 * Checks is the character valid for file name
		 *
		 * @param c character to be checked
		 * @return true if it is valid
		 */
		public static boolean isValidFileNameCharacter(char c) {
			for (char ch : FORBIDDEN_SYMBOLS_IN_FILE_NAME) {
				if (ch == c) {
					return false;
				}
			}
			return true;
		}
	}
}
