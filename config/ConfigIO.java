package org.verapdf.processor.config;

import org.apache.log4j.Logger;

import javax.xml.bind.JAXBException;
import java.io.*;
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
						FileSystems.getDefault().getPath(f.getAbsolutePath(), "config.xml");
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
		File configFile = configPath.toFile();
		if(!configFile.exists()) {
			return new Config();
		} else if(!configFile.canRead()) {
			throw new IllegalArgumentException("Path should specify read accessible file");
		} else {
			FileInputStream inputStream = new FileInputStream(configFile);
			return Config.fromXml(inputStream);
		}
	}

	public static boolean writeConfig(Config config) {
		if(!configPath.equals(FileSystems.getDefault().getPath("")))
			try {
				FileOutputStream outputStream =
						new FileOutputStream(configPath.toFile());
				Config.toXml(config, outputStream, true);
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
		if(!configFile.exists() || !configFile.canRead()) {
			throw new IllegalArgumentException("Path should specify existing read accessible file");
		}
		FileOutputStream outputStream =
				new FileOutputStream(configFile);
		Config.toXml(config, outputStream, true);
	}
}
