package org.verapdf.gui;

import org.apache.log4j.Logger;
import org.verapdf.gui.config.Config;
import org.verapdf.gui.config.ConfigPropertiesSerializator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Contains all information about settings
 *
 * @author Maksim Bezrukov
 */
final class Settings {

	private static final Logger LOGGER = Logger.getLogger(Settings.class);

	private Config config;
	private Config.Builder builder = new Config.Builder();
	private boolean isSerrialized;
	private Path path = null;

	Settings() {
		File user = new File(System.getProperty("user.home"));
		File f = new File(user, ".veraPDF");
		if (!f.exists() && !f.mkdir()) {
			this.isSerrialized = false;
			this.config = Config.Builder.buildDefaultConfig();
		} else {
			File configFile = new File(f, "config.properties");
			this.isSerrialized = true;
			this.path = configFile.toPath();
			if (configFile.exists()) {
				try {
					this.config = ConfigPropertiesSerializator.loadConfig(configFile.toPath());
				} catch (IOException e) {
					LOGGER.error("Can not read config file", e);
					this.config = Config.Builder.buildDefaultConfig();
				}

			} else {
				this.config = Config.Builder.buildDefaultConfig();
			}
		}
	}

	int getProcessingType() {
		return config.getProcessingType();
	}

	void setProcessingType(int processingType) {
		builder.processingType(processingType);
		this.config = builder.build();
		if (isSerrialized) {
			try {
				ConfigPropertiesSerializator.saveConfig(this.config, path);
			} catch (IOException e) {
				LOGGER.error("Can not save config", e);
			}
		}
	}

	boolean isShowPassedRules() {
		return config.isShowPassedRules();
	}

	void setShowPassedRules(boolean showPassedRules) {
		builder.showPassedRules(showPassedRules);
		this.config = builder.build();
		if (isSerrialized) {
			try {
				ConfigPropertiesSerializator.saveConfig(this.config, path);
			} catch (IOException e) {
				LOGGER.error("Can not save config", e);
			}
		}
	}

	int getMaxNumberOfFailedChecks() {
		return config.getMaxNumberOfFailedChecks();
	}

	void setMaxNumberOfFailedChecks(int maxNumberOfFailedChecks) {
		builder.maxNumberOfFailedChecks(maxNumberOfFailedChecks);
		this.config = builder.build();
		if (isSerrialized) {
			try {
				ConfigPropertiesSerializator.saveConfig(this.config, path);
			} catch (IOException e) {
				LOGGER.error("Can not save config", e);
			}
		}
	}

	int getMaxNumberOfDisplayedFailedChecks() {
		return config.getMaxNumberOfDisplayedFailedChecks();
	}

	void setMaxNumberOfDisplayedFailedChecks(int maxNumberOfDisplayedFailedChecks) {
		builder.maxNumberOfDisplayedFailedChecks(maxNumberOfDisplayedFailedChecks);
		this.config = builder.build();
		if (isSerrialized) {
			try {
				ConfigPropertiesSerializator.saveConfig(this.config, path);
			} catch (IOException e) {
				LOGGER.error("Can not save config", e);
			}
		}
	}

	Path getFeaturesPluginsConfigFilePath() {
		return config.getFeaturesPluginsConfigFilePath();
	}

	void setFeaturesPluginsConfigFilePath(Path featuresPluginsConfigFilePath) {
		builder.featuresPluginsConfigFilePath(featuresPluginsConfigFilePath);
		this.config = builder.build();
		if (isSerrialized) {
			try {
				ConfigPropertiesSerializator.saveConfig(this.config, path);
			} catch (IOException e) {
				LOGGER.error("Can not save config", e);
			}
		}
	}

	boolean isFixMetadata() {
		return config.isFixMetadata();
	}

	void setFixMetadata(boolean fixMetadata) {
		builder.fixMetadata(fixMetadata);
		this.config = builder.build();
		if (isSerrialized) {
			try {
				ConfigPropertiesSerializator.saveConfig(this.config, path);
			} catch (IOException e) {
				LOGGER.error("Can not save config", e);
			}
		}
	}

	String getMetadataFixerPrefix() {
		return config.getMetadataFixerPrefix();
	}

	void setMetadataFixerPrefix(String metadataFixerPrefix) {
		builder.metadataFixerPrefix(metadataFixerPrefix);
		this.config = builder.build();
		if (isSerrialized) {
			try {
				ConfigPropertiesSerializator.saveConfig(this.config, path);
			} catch (IOException e) {
				LOGGER.error("Can not save config", e);
			}
		}
	}

	Path getFixMetadataPathFolder() {
		return config.getFixMetadataPathFolder();
	}

	void setFixMetadataPathFolder(Path fixMetadataPathFolder) {
		builder.fixMetadataPathFolder(fixMetadataPathFolder);
		this.config = builder.build();
		if (isSerrialized) {
			try {
				ConfigPropertiesSerializator.saveConfig(this.config, path);
			} catch (IOException e) {
				LOGGER.error("Can not save config", e);
			}
		}
	}
}
