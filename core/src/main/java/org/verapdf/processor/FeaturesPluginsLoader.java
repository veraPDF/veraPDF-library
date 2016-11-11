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

import org.verapdf.features.AbstractFeaturesExtractor;
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
	public static List<AbstractFeaturesExtractor> loadExtractors(final Path pluginsConfigPath, final List<String> errors) {
		File pluginsConfigFile = pluginsConfigPath.toFile();
		if (pluginsConfigFile.exists() && pluginsConfigFile.canRead()) {
			try (FileInputStream fis = new FileInputStream(pluginsConfigFile)) {
				PluginsCollectionConfig pluginsCollectionConfig =
						PluginsCollectionConfig.create(fis);
				return getAllExtractors(pluginsCollectionConfig, errors);
			} catch (IOException | JAXBException e) {
				LOGGER.log(Level.WARNING, "Problem loading Feature Extraction plugins from: " + pluginsConfigFile, e);
				errors.add(e.getMessage());
			}
		}
		return Collections.emptyList();
	}

	private static List<AbstractFeaturesExtractor> getAllExtractors(PluginsCollectionConfig pluginsCollectionConfig, List<String> errors) {
		List<AbstractFeaturesExtractor> extractors = new ArrayList<>();
		List<PluginConfig> plugins = pluginsCollectionConfig.getPlugins();
		if (plugins != null) {
			for (PluginConfig config : plugins) {
				if (config.isEnabled()) {
					AbstractFeaturesExtractor extractor = getExtractorFromConfig(config, errors);
					if (extractor != null) {
						extractors.add(extractor);
					}
				}
			}
		}
		return extractors;
	}

	private static AbstractFeaturesExtractor getExtractorFromConfig(PluginConfig config, List<String> errors) {
		Path pluginJar = config.getPluginJar();
		if (pluginJar == null) {
			errors.add("Plugins config file contains an enabled plugin with empty path");
			return null;
		}
		File pluginJarFile = pluginJar.toFile();
		if (pluginJarFile == null || !pluginJarFile.isFile()) {
			errors.add("Plugins config file contains wrong path");
			return null;
		}

		AbstractFeaturesExtractor extractor = loadExtractor(pluginJarFile, errors);
		initializeExtractor(extractor, config);
		return extractor;
	}

	private static void initializeExtractor(AbstractFeaturesExtractor extractor, PluginConfig config) {
		String name = getNonNullString(config.getName());
		String version = getNonNullString(config.getVersion());
		String description = getNonNullString(config.getDescription());
		AbstractFeaturesExtractor.ExtractorDetails details = new AbstractFeaturesExtractor.ExtractorDetails(name, version, description);

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

	private static AbstractFeaturesExtractor loadExtractor(File jar, List<String> errors) {
		try {
			List<String> classNames = getAllClassNamesFromJAR(jar);
			return loadExtractorByClassNames(jar, classNames, errors);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Can not load extractors from file with path "
					+ jar.getPath(), e);
			errors.add("Can not load extractors from file with path "
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

	private static AbstractFeaturesExtractor loadExtractorByClassNames(File jar, List<String> classNames, List<String> errors) throws MalformedURLException {
		URL url = jar.toURI().toURL();
		URL[] urls = new URL[]{url};
		@SuppressWarnings("resource")
		ClassLoader cl = new URLClassLoader(urls);
		Class<?> extractorClass = null;
		for (String className : classNames) {
			try {
				Class<?> cls = cl.loadClass(className);
				if (AbstractFeaturesExtractor.class.isAssignableFrom(cls)) {
					if (extractorClass == null) {
						extractorClass = cls;
					} else {
						LOGGER.log(Level.WARNING, "JAR file " + jar.getAbsolutePath()
								+ " contains more than one extractor.");
						errors.add("JAR file " + jar.getAbsolutePath()
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
				AbstractFeaturesExtractor extractor = (AbstractFeaturesExtractor) obj;
				return extractor;
			} catch (InstantiationException | IllegalAccessException e) {
				LOGGER.log(Level.WARNING, 
						"Can not create an instance of the class "
								+ extractorClass.getName() + " from jar "
								+ jar.getPath(), e);
				errors.add("Can not create an instance of the class "
						+ extractorClass.getName() + " from jar "
						+ jar.getPath());
			}
		}
		return null;
	}
}
