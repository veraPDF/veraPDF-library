package org.verapdf.processor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBException;

import org.verapdf.features.FeaturesExtractor;
import org.verapdf.processor.plugins.Attribute;
import org.verapdf.processor.plugins.PluginConfig;
import org.verapdf.processor.plugins.PluginsCollectionConfig;

/**
 * @author Maksim Bezrukov
 */
public class FeaturesPluginsLoader {
	private static final Logger LOGGER = Logger
			.getLogger(FeaturesPluginsLoader.class.getName());

	private FeaturesPluginsLoader() {
	}

	/**
	 * Configurates features reporter
	 */
	public static List<FeaturesExtractor> loadExtractors(Path pluginsConfigPath, ProcessingResult result) {
		File pluginsConfigFile = pluginsConfigPath.toFile();
		if (pluginsConfigFile.exists() && pluginsConfigFile.canRead()) {
			try (FileInputStream fis = new FileInputStream(pluginsConfigFile)) {
				PluginsCollectionConfig pluginsCollectionConfig =
						PluginsCollectionConfig.fromXml(fis);
				return getAllExtractors(pluginsCollectionConfig, result);
			} catch (IOException | JAXBException e) {
				LOGGER.log(Level.WARNING, "Problem loading Feature Extraction plugins from: " + pluginsConfigFile, e);
				result.addErrorMessage(e.getMessage());
			}
		}
		return Collections.emptyList();
	}

	private static List<FeaturesExtractor> getAllExtractors(PluginsCollectionConfig pluginsCollectionConfig, ProcessingResult result) {
		List<FeaturesExtractor> extractors = new ArrayList<>();
		List<PluginConfig> plugins = pluginsCollectionConfig.getPlugins();
		if (plugins != null) {
			for (PluginConfig config : plugins) {
				if (config.isEnabled()) {
					FeaturesExtractor extractor = getExtractorFromConfig(config, result);
					if (extractor != null) {
						extractors.add(extractor);
					}
				}
			}
		}
		return extractors;
	}

	private static FeaturesExtractor getExtractorFromConfig(PluginConfig config, ProcessingResult result) {
		Path pluginJar = config.getPluginJar();
		if (pluginJar == null) {
			result.addErrorMessage("Plugins config file contains an enabled plugin with empty path");
			return null;
		}
		File pluginJarFile = pluginJar.toFile();
		if (pluginJarFile == null || !pluginJarFile.isFile()) {
			result.addErrorMessage("Plugins config file contains wrong path");
			return null;
		}

		FeaturesExtractor extractor = loadExtractor(pluginJarFile, result);
		initializeExtractor(extractor, config);
		return extractor;
	}

	private static void initializeExtractor(FeaturesExtractor extractor, PluginConfig config) {
		String name = getNonNullString(config.getName());
		String version = getNonNullString(config.getVersion());
		String description = getNonNullString(config.getDescription());
		FeaturesExtractor.ExtractorDetails details = new FeaturesExtractor.ExtractorDetails(name, version, description);

		Map<String, String> attributes = new HashMap<>();
		List<Attribute> attributesList = config.getAttributes();
		if (attributesList != null) {
			for (Attribute attr : attributesList) {
				if (attr.getKey() != null && attr.getValue() != null) {
					attributes.put(attr.getKey(), attr.getValue());
				}
			}
		}
		extractor.initialize(details, attributes);
	}

	private static String getNonNullString(String original) {
		return original == null ? "" : original;
	}

	private static FeaturesExtractor loadExtractor(File jar, ProcessingResult result) {
		try {
			List<String> classNames = getAllClassNamesFromJAR(jar);
			return loadExtractorByClassNames(jar, classNames, result);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Can not load extractors from file with path "
					+ jar.getPath(), e);
			result.addErrorMessage("Can not load extractors from file with path "
					+ jar.getPath());
		}
		return null;
	}

	private static List<String> getAllClassNamesFromJAR(File jar)
			throws IOException {
		List<String> classNames = new ArrayList<>();
		try (JarInputStream jarStream = new JarInputStream(new FileInputStream(
				jar))) {
			JarEntry jarEntry = jarStream.getNextJarEntry();
			while (jarEntry != null) {
				if (jarEntry.getName().endsWith(".class")) {
					String className = jarEntry.getName().replaceAll("/", ".");
					String myClass = className.substring(0,
							className.lastIndexOf('.'));
					classNames.add(myClass);
				}
				jarEntry = jarStream.getNextJarEntry();
			}
		}
		return classNames;
	}

	private static FeaturesExtractor loadExtractorByClassNames(File jar, List<String> classNames, ProcessingResult result) throws MalformedURLException {
		URL url = jar.toURI().toURL();
		URL[] urls = new URL[]{url};
		@SuppressWarnings("resource")
		ClassLoader cl = new URLClassLoader(urls);
		Class<?> extractorClass = null;
		for (String className : classNames) {
			try {
				Class<?> cls = cl.loadClass(className);
				if (FeaturesExtractor.class.isAssignableFrom(cls)) {
					if (extractorClass == null) {
						extractorClass = cls;
					} else {
						LOGGER.log(Level.WARNING, "JAR file " + jar.getAbsolutePath()
								+ " contains more than one extractor.");
						result.addErrorMessage("JAR file " + jar.getAbsolutePath()
								+ " contains more than one extractor.");
						return null;
					}
				}
			} catch (ClassNotFoundException e) {
				LOGGER.log(Level.WARNING, "Can not load class " + className + " from jar "
						+ jar.getAbsolutePath(), e);
			}
		}
		if (extractorClass != null) {
			try {
				Object obj = extractorClass.newInstance();
				FeaturesExtractor extractor = (FeaturesExtractor) obj;
				return extractor;
			} catch (InstantiationException | IllegalAccessException e) {
				LOGGER.log(Level.WARNING, 
						"Can not create an instance of the class "
								+ extractorClass.getName() + " from jar "
								+ jar.getPath(), e);
				result.addErrorMessage("Can not create an instance of the class "
						+ extractorClass.getName() + " from jar "
						+ jar.getPath());
			}
		}
		return null;
	}
}
