package org.verapdf.processor.config;

import org.apache.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

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

	private static final Logger LOGGER = Logger.getLogger(ConfigIO.class);

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
			config.setPluginsConfigPath(pluginsConfig.toPath());
		} else {
			FileInputStream inputStream = new FileInputStream(configFile);
			config = Config.fromXml(inputStream);
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
				LOGGER.error("Can not save config", e1);
			}
			catch (JAXBException e1) {
				LOGGER.error("Can not convert config to XML", e1);
			}
		return false;
	}

	public static Config readConfig(Path configPath)
		throws IOException, JAXBException, IllegalArgumentException {
		if(configPath == null) {
			throw new IllegalArgumentException("Path should specify a file");
		}
		File configFile = configPath.toFile();
		if(!configFile.exists() || !configFile.canRead()) {
			throw new IllegalArgumentException("Path should specify existing read accessible file");
		} else {
			FileInputStream inputStream = new FileInputStream(configFile);
			return Config.fromXml(inputStream);
		}
	}

	public static void writeConfig(Config config, Path configPath)
			throws IOException, JAXBException, IllegalArgumentException{
		if(configPath == null) {
			throw new IllegalArgumentException("Path should specify a file");
		}
		File configFile = configPath.toFile();
		FileOutputStream outputStream =
				new FileOutputStream(configFile);
		Config.toXml(config, outputStream, true);
	}
}
