/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 * <p>
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 * <p>
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 * <p>
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.processor;

import org.verapdf.features.AbstractFeaturesExtractor;
import org.verapdf.processor.plugins.Attribute;
import org.verapdf.processor.plugins.PluginConfig;
import org.verapdf.processor.plugins.PluginsCollectionConfig;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	public static List<AbstractFeaturesExtractor> loadExtractors(final Path pluginsConfigPath) {
		File pluginsConfigFile = pluginsConfigPath.toFile();
		if (pluginsConfigFile.exists() && pluginsConfigFile.canRead()) {
			try (FileInputStream fis = new FileInputStream(pluginsConfigFile)) {
				return loadExtractors(fis);
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "Problem loading Feature Extraction plugins from: " + pluginsConfigFile, e);
			}
		}
		return Collections.emptyList();
	}

	public static List<AbstractFeaturesExtractor> loadExtractors(final InputStream pluginsConfigStream) {
		try {
			PluginsCollectionConfig pluginsCollectionConfig =
					PluginsCollectionConfig.create(pluginsConfigStream);
			return loadExtractors(pluginsCollectionConfig);
		} catch (JAXBException e) {
			LOGGER.log(Level.WARNING, "Problem loading Feature Extraction plugins.", e);
		}
		return Collections.emptyList();
	}

	public static List<AbstractFeaturesExtractor> loadExtractors(final PluginsCollectionConfig pluginsCollectionConfig) {
		return getAllExtractors(pluginsCollectionConfig);
	}

	private static List<AbstractFeaturesExtractor> getAllExtractors(PluginsCollectionConfig pluginsCollectionConfig) {
		List<PluginConfig> plugins = pluginsCollectionConfig.getPlugins();
		if (plugins != null && !plugins.isEmpty()) {
			List<AbstractFeaturesExtractor> extractors = new ArrayList<>();
			for (PluginConfig config : plugins) {
				if (config.isEnabled()) {
					AbstractFeaturesExtractor extractor = getExtractorFromConfig(config);
					if (extractor != null) {
						extractors.add(extractor);
					}
				}
			}
			return extractors;
		}
		return Collections.emptyList();
	}

	private static AbstractFeaturesExtractor getExtractorFromConfig(PluginConfig config) {
		Path pluginJar = config.getPluginJar();
		if (pluginJar == null) {
			LOGGER.log(Level.WARNING, "Plugins config file contains an enabled plugin with empty path");
			return null;
		}
		File pluginJarFile = pluginJar.toFile();
		if (pluginJarFile == null || !pluginJarFile.isFile()) {
			LOGGER.log(Level.WARNING, "Plugins config file contains wrong path");
			return null;
		}

		AbstractFeaturesExtractor extractor = loadExtractor(pluginJarFile);
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

	private static AbstractFeaturesExtractor loadExtractor(File jar) {
		try {
			List<String> classNames = getAllClassNamesFromJAR(jar);
			return loadExtractorByClassNames(jar, classNames);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Can not load extractors from file with path "
					+ jar.getPath(), e);
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

	private static AbstractFeaturesExtractor loadExtractorByClassNames(File jar, List<String> classNames) throws MalformedURLException {
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
			}
		}
		return null;
	}
}
