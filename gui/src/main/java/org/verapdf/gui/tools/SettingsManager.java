package org.verapdf.gui.tools;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Contains all information about settings
 *
 * @author Maksim Bezrukov
 */
public final class SettingsManager {

	private static final Logger LOGGER = Logger.getLogger(SettingsManager.class);

	public static final String PROPERTY_PROCESSING_TYPE = "processingType";
	public static final String PROPERTY_SHOW_PASSED_RULES = "showPassedRules";
	public static final String PROPERTY_MAX_NUMBER_FAILED_CHECKS = "maxNumbFailedChecks";
	public static final String PROPERTY_MAX_NUMBER_DISPLAYED_FAILED_CHECKS = "maxNumbDisplFailedChecks";
	public static final String PROPERTY_FEATURES_CONFIG_FILE = "featuresPluginConfigFile";

	private static int defaultProcessingType = 3;
	private static boolean defaultHidePassedRules = false;
	private static int defaultMaxNumberOfFailedChecks = 100;
	private static int defaultMaxNumberDisplayedFailedChecks = 100;
	private static Path defaultFeaturesPluginsConfigFilePath = null;

	private int processingType;
	private boolean showPassedRules;
	private int maxNumberOfFailedChecks;
	private int maxNumberOfDisplayedFailedChecks;
	private Path featuresPluginsConfigFilePath;

	/**
	 * Creates new Settings manager
	 */
	public SettingsManager() {
		initialize();
	}

	private void initialize() {
		File user = new File(System.getProperty("user.home"));
		File f = new File(user, ".veraPDF");
		File configFile = new File(f, "config.properties");
		Properties settings = new Properties();
		if (configFile.exists()) {
			try {
				FileReader reader = new FileReader(configFile);
				settings.load(reader);
				reader.close();
			} catch (IOException e) {
				LOGGER.error("Couldn't load config file", e);
			}
		}

		this.processingType = getLoadedProcessingType(settings);
		this.maxNumberOfFailedChecks = getLoadedMaxNumberOfFailedChecks(settings);
		this.maxNumberOfDisplayedFailedChecks = getLoadedMaxNumberDisplayedFailedChecks(settings);
		this.showPassedRules = getLoadedShowPassedRules(settings);
		this.featuresPluginsConfigFilePath = getLoadedFeaturesPluginsConfigFile(settings);

		save();
	}

	private void save() {
		try {
			File user = new File(System.getProperty("user.home"));
			File f = new File(user, ".veraPDF");
			if (!f.exists() && !f.mkdir()) {
				throw new IOException("Can not create settings directory.");
			}
			File configFile = new File(f, "config.properties");
			FileWriter writer = new FileWriter(configFile);

			Properties settings = new Properties();
			settings.setProperty(PROPERTY_PROCESSING_TYPE, String.valueOf(this.processingType));
			settings.setProperty(PROPERTY_MAX_NUMBER_FAILED_CHECKS, String.valueOf(this.maxNumberOfFailedChecks));
			settings.setProperty(PROPERTY_MAX_NUMBER_DISPLAYED_FAILED_CHECKS, String.valueOf(this.maxNumberOfDisplayedFailedChecks));
			settings.setProperty(PROPERTY_SHOW_PASSED_RULES, String.valueOf(this.showPassedRules));
			if (this.featuresPluginsConfigFilePath != null) {
				settings.setProperty(PROPERTY_FEATURES_CONFIG_FILE, this.featuresPluginsConfigFilePath.toString());
			}
			settings.store(writer, "settings");
			writer.close();
		} catch (IOException e) {
			LOGGER.error("Couldn't save config into file", e);
		}
	}

	/**
	 * Changes settings parameters
	 * @param processingType must be in range from 1 to 3
	 * @param showPassedRules true for show passed rules at the report
	 * @param maxNumberOfFailedChecks a natural number that indicates maximum number of failed checks for rule or -1 for unlimited
	 * @param maxNumberOfDisplayedFailedChecks a non negative integer number that indicates maximum number of displayed
	 *                                         failed checks for rule or -1 for infinite
	 * @param featuresPluginsConfigFilePath a path to the config file for features plugins
	 */
	public void setState(int processingType, boolean showPassedRules, int maxNumberOfFailedChecks, int maxNumberOfDisplayedFailedChecks, Path featuresPluginsConfigFilePath) {
		if (processingType >= 1 && processingType <= 3) {
			this.processingType = processingType;
		} else {
			LOGGER.error("Processing type for setter method is not in range from 1 to 3");
		}
		this.showPassedRules = showPassedRules;
		if (maxNumberOfFailedChecks > 0 || maxNumberOfFailedChecks == -1) {
			this.maxNumberOfFailedChecks = maxNumberOfFailedChecks;
		} else {
			LOGGER.error("Max number of failed checks for rule for setter method is not a natural or -1");
		}
		if (maxNumberOfDisplayedFailedChecks >= -1) {
			this.maxNumberOfDisplayedFailedChecks = maxNumberOfDisplayedFailedChecks;
		} else {
			LOGGER.error("Max number of displayed failed checks for rule for setter method is less than -1");
		}
		if (featuresPluginsConfigFilePath != null) {
			File f = featuresPluginsConfigFilePath.toFile();
			if (f.exists() && f.isFile()) {
				this.featuresPluginsConfigFilePath = featuresPluginsConfigFilePath;
			} else {
				LOGGER.error("Features plugins config file's path is not valid");
			}
		} else {
			this.featuresPluginsConfigFilePath = null;
		}

		save();
	}

	private static Path getLoadedFeaturesPluginsConfigFile(Properties settings) {
		String path = settings.getProperty(PROPERTY_FEATURES_CONFIG_FILE);
		if (path != null) {
			File f = new File(path);
			if (f.exists() && f.isFile()) {
				return f.toPath();
			} else {
				LOGGER.error("Settings file holds incorrect path to the features plugins config file");
				return defaultFeaturesPluginsConfigFilePath;
			}
		} else {
			return defaultFeaturesPluginsConfigFilePath;
		}
	}

	private static boolean getLoadedShowPassedRules(Properties settings) {
		String bool = settings.getProperty(PROPERTY_SHOW_PASSED_RULES);
		if (bool != null) {
			if ("true".equalsIgnoreCase(bool)) {
				return true;
			} else if ("false".equalsIgnoreCase(bool)) {
				return false;
			} else {
				LOGGER.error("Settings file holds incorrect value for hide passed rules setting");
				return defaultHidePassedRules;
			}
		} else {
			return defaultHidePassedRules;
		}
	}

	private static int getLoadedMaxNumberOfFailedChecks(Properties settings) {
		String numb = settings.getProperty(PROPERTY_MAX_NUMBER_FAILED_CHECKS);
		if (numb != null) {
			try {
				int tempNumb = Integer.parseInt(numb);
				if (tempNumb > 0 || tempNumb == -1) {
					return tempNumb;
				} else {
					LOGGER.error("Settings file holds incorrect maximum number of failed checks for rule");
					return defaultMaxNumberOfFailedChecks;
				}
			} catch (NumberFormatException e) {
				LOGGER.error("Settings file holds incorrect maximum number of failed checks for rule");
				return defaultMaxNumberOfFailedChecks;
			}
		} else {
			return defaultMaxNumberOfFailedChecks;
		}
	}

	private static int getLoadedMaxNumberDisplayedFailedChecks(Properties settings) {
		String numb = settings.getProperty(PROPERTY_MAX_NUMBER_DISPLAYED_FAILED_CHECKS);
		if (numb != null) {
			try {
				int tempNumb = Integer.parseInt(numb);
				if (tempNumb >= -1) {
					return tempNumb;
				} else {
					LOGGER.error("Settings file holds incorrect maximum number of displayed failed checks for rule");
					return defaultMaxNumberDisplayedFailedChecks;
				}
			} catch (NumberFormatException e) {
				LOGGER.error("Settings file holds incorrect maximum number of displayed failed checks for rule");
				return defaultMaxNumberDisplayedFailedChecks;
			}
		} else {
			return defaultMaxNumberDisplayedFailedChecks;
		}
	}

	private static int getLoadedProcessingType(Properties settings) {
		String type = settings.getProperty(PROPERTY_PROCESSING_TYPE);
		if (type != null) {
			try {
				int tempType = Integer.parseInt(type);
				if (tempType >= 1 && tempType <= 3) {
					return tempType;
				} else {
					LOGGER.error("Settings file holds incorrect processing type");
					return defaultProcessingType;
				}
			} catch (NumberFormatException e) {
				LOGGER.error("Settings file holds incorrect processing type");
				return defaultProcessingType;
			}
		} else {
			return defaultProcessingType;
		}
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
}
