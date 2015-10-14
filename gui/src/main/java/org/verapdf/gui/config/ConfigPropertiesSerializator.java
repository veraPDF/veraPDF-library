package org.verapdf.gui.config;

import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Properties;

/**
 * @author Maksim Bezrukov
 */
public final class ConfigPropertiesSerializator {

	private static final Logger LOGGER = Logger.getLogger(ConfigPropertiesSerializator.class);

	private static final String PROPERTY_PROCESSING_TYPE = "processingType";
	private static final String PROPERTY_SHOW_PASSED_RULES = "showPassedRules";
	private static final String PROPERTY_MAX_NUMBER_FAILED_CHECKS = "maxNumbFailedChecks";
	private static final String PROPERTY_MAX_NUMBER_DISPLAYED_FAILED_CHECKS = "maxNumbDisplFailedChecks";
	private static final String PROPERTY_FEATURES_CONFIG_FILE = "featuresPluginConfigFile";
	private static final String PROPERTY_FIX_METADATA = "fixMetadata";
	private static final String PROPERTY_METADATA_FIXER_PREFIX = "metadataFixerPrefix";
	private static final String PROPERTY_FIX_METADATA_PATH_FOLDER = "fixMetadataPathFolder";

	/**
	 * Saves config by serrializing it as properties
	 *
	 * @param config config object for serialization
	 * @param path   path for the result file
	 * @throws IOException              if I/O errors occurs
	 * @throws IllegalArgumentException if config is null, path is null or path is invalid path for write a file
	 */
	public static void saveConfig(Config config, Path path) throws IOException {
		if (config == null) {
			throw new IllegalArgumentException("Config object can not be null");
		}
		if (path == null) {
			throw new IllegalArgumentException("Path can not be null");
		} else if (!path.getParent().toFile().isDirectory() || path.toFile().isDirectory() || (path.toFile().exists() && !path.toFile().canWrite())) {
			throw new IllegalArgumentException("Path should specify a write accessible file path");
		}

		FileWriter writer = new FileWriter(path.toFile());

		Properties settings = new Properties();
		settings.setProperty(PROPERTY_PROCESSING_TYPE, String.valueOf(config.getProcessingType()));
		settings.setProperty(PROPERTY_MAX_NUMBER_FAILED_CHECKS, String.valueOf(config.getMaxNumberOfFailedChecks()));
		settings.setProperty(PROPERTY_MAX_NUMBER_DISPLAYED_FAILED_CHECKS, String.valueOf(config.getMaxNumberOfDisplayedFailedChecks()));
		settings.setProperty(PROPERTY_SHOW_PASSED_RULES, String.valueOf(config.isShowPassedRules()));
		settings.setProperty(PROPERTY_FEATURES_CONFIG_FILE, config.getFeaturesPluginsConfigFilePath().toString());
		settings.setProperty(PROPERTY_FIX_METADATA, String.valueOf(config.isFixMetadata()));
		settings.setProperty(PROPERTY_METADATA_FIXER_PREFIX, config.getMetadataFixerPrefix());
		settings.setProperty(PROPERTY_FIX_METADATA_PATH_FOLDER, config.getFixMetadataPathFolder().toString());
		settings.store(writer, "settings");
		writer.close();

	}

	private static String getStringValue(Properties settings, String key) {
		String prefix = settings.getProperty(key);
		if (prefix == null) {
			throw new IllegalArgumentException("Settings should contain property " + key);
		}
		return prefix;
	}

	private static Path getPathValue(Properties settings, String key) {
		String pathString = settings.getProperty(key);
		if (pathString == null) {
			throw new IllegalArgumentException("Settings should contain property " + key);
		}
		return FileSystems.getDefault().getPath(pathString);
	}

	private static boolean getBooleanValue(Properties settings, String key) {
		String type = settings.getProperty(key);
		if (type == null) {
			throw new IllegalArgumentException("Settings should contain property " + key);
		}

		if ("true".equalsIgnoreCase(type)) {
			return true;
		} else if ("false".equalsIgnoreCase(type)) {
			return false;
		} else {
			throw new IllegalArgumentException("Settings should contain property " + key + " with boolean value");
		}
	}

	private static int getIntegerValue(Properties settings, String key) {
		String type = settings.getProperty(key);
		if (type == null) {
			throw new IllegalArgumentException("Settings should contain property " + key);
		}

		try {
			return Integer.parseInt(type);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Settings should contain property " + key + " with integer value", e);
		}
	}

	/**
	 * Loads config from serrialized properties
	 *
	 * @param path path to the file
	 * @return loaded config object
	 * @throws IOException              if any I/O errors occurs
	 * @throws IllegalArgumentException if any error with values occurs or some value is missing
	 */
	public static Config loadConfig(Path path) throws IOException {
		Properties properties = new Properties();
		if (!path.toFile().isFile() || !path.toFile().canRead()) {
			throw new IllegalArgumentException("Path should specify an existed and read accessible file");
		}
		FileReader reader = new FileReader(path.toFile());
		properties.load(reader);
		reader.close();
		Config.Builder builder = new Config.Builder();

		try {
			builder.processingType(getIntegerValue(properties, PROPERTY_PROCESSING_TYPE));
		} catch (IllegalArgumentException e) {
			LOGGER.error("Property " + PROPERTY_PROCESSING_TYPE + " is missing or containing a wrong value. Setting it to default");
		}
		try {
			builder.maxNumberOfFailedChecks(getIntegerValue(properties, PROPERTY_MAX_NUMBER_FAILED_CHECKS));
		} catch (IllegalArgumentException e) {
			LOGGER.error("Property " + PROPERTY_MAX_NUMBER_FAILED_CHECKS + " is missing or containing a wrong value. Setting it to default");
		}
		try {
			builder.maxNumberOfDisplayedFailedChecks(getIntegerValue(properties, PROPERTY_MAX_NUMBER_DISPLAYED_FAILED_CHECKS));
		} catch (IllegalArgumentException e) {
			LOGGER.error("Property " + PROPERTY_MAX_NUMBER_DISPLAYED_FAILED_CHECKS + " is missing or containing a wrong value. Setting it to default");
		}
		try {
			builder.showPassedRules(getBooleanValue(properties, PROPERTY_SHOW_PASSED_RULES));
		} catch (IllegalArgumentException e) {
			LOGGER.error("Property " + PROPERTY_SHOW_PASSED_RULES + " is missing or containing a wrong value. Setting it to default");
		}
		try {
			builder.fixMetadata(getBooleanValue(properties, PROPERTY_FIX_METADATA));
		} catch (IllegalArgumentException e) {
			LOGGER.error("Property " + PROPERTY_FIX_METADATA + " is missing or containing a wrong value. Setting it to default");
		}
		try {
			builder.metadataFixerPrefix(getStringValue(properties, PROPERTY_METADATA_FIXER_PREFIX));
		} catch (IllegalArgumentException e) {
			LOGGER.error("Property " + PROPERTY_METADATA_FIXER_PREFIX + " is missing or containing a wrong value. Setting it to default");
		}
		try {
			builder.fixMetadataPathFolder(getPathValue(properties, PROPERTY_FIX_METADATA_PATH_FOLDER));
		} catch (IllegalArgumentException e) {
			LOGGER.error("Property " + PROPERTY_FIX_METADATA_PATH_FOLDER + " is missing or containing a wrong value. Setting it to default");
		}
		try {
			builder.featuresPluginsConfigFilePath(getPathValue(properties, PROPERTY_FEATURES_CONFIG_FILE));
		} catch (IllegalArgumentException e) {
			LOGGER.error("Property " + PROPERTY_FEATURES_CONFIG_FILE + " is missing or containing a wrong value. Setting it to default");
		}
		return builder.build();
	}
}
