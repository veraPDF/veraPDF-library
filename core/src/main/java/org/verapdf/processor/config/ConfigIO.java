package org.verapdf.processor.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.verapdf.pdfa.validation.validators.ValidatorConfig;

/**
 * @author Sergey Shemyakov
 */
public class ConfigIO {

	private static Path configPath;

	static {
		configPath = FileSystems.getDefault().getPath("");
 		String appHome = System.getProperty("app.home");
		if (appHome != null) {
			File user = new File(appHome);
			File f = new File(user, "config");
			if (f.exists() || f.mkdir()) {
				configPath =
						FileSystems.getDefault().getPath(f.getAbsolutePath(), "app.xml");
			}
		}
	}

	private ConfigIO() {
	}

	private static final Logger LOGGER = Logger.getLogger(ConfigIO.class.getName());

	public static Path getConfigFolderPath() {
		if (!configPath.toString().isEmpty()) {
			return configPath.getParent();
		}
		return configPath;
	}

	public static Config readConfig()
			throws IOException, JAXBException, IllegalArgumentException {
		if(configPath.equals(FileSystems.getDefault().getPath(""))) {
			return new Config();
		}
		Config config;
		File configFile = configPath.toFile();
		if(!configFile.exists() || !configFile.canRead()) {
			config = new Config();
			File configParent = configFile.getParentFile();
			File pluginsConfig = new File(configParent, "plugins.xml");
//			config.setPluginsConfigPath(pluginsConfig.toPath());
			File featuresConfig = new File(configParent, "features.xml");
//			config.setFeaturesConfigPath(featuresConfig.toPath());
		} else {
			try (FileInputStream inputStream = new FileInputStream(configFile)) {
				config = Config.fromXml(inputStream);
			}
		}
		return config;
	}

	public static boolean writeConfig(Config config) {
		if(!configPath.equals(FileSystems.getDefault().getPath("")))
			try {
				writeConfig(config, configPath);
				return true;
			}
			catch (IOException e1) {
				LOGGER.log(Level.WARNING, "Can not save config", e1);
			}
			catch (JAXBException e1) {
				LOGGER.log(Level.WARNING, "Can not convert config to XML", e1);
			}
		return false;
	}

	public static Config readConfig(Path pathToRead)
		throws IOException, JAXBException, IllegalArgumentException {
		if(pathToRead == null) {
			throw new IllegalArgumentException("Path should specify a file");
		}
		File configFile = pathToRead.toFile();
		if(!configFile.exists() || !configFile.canRead()) {
			throw new IllegalArgumentException("Path should specify existing read accessible file");
		}
		try (FileInputStream inputStream = new FileInputStream(configFile)) {
			return Config.fromXml(inputStream);
		}
	}

	public static void writeConfig(Config config, Path pathToWrite)
			throws IOException, JAXBException, IllegalArgumentException{
		if(pathToWrite == null) {
			throw new IllegalArgumentException("Path should specify a file");
		}
		File configFile = pathToWrite.toFile();
		try (FileOutputStream outputStream =
				new FileOutputStream(configFile)) {
			Config.toXml(config, outputStream, Boolean.TRUE);
		}
	}
}
