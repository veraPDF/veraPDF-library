package org.verapdf.features;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * @author Maksim Bezrukov
 */
public class FeaturesPluginsLoader {
	private static final Logger LOGGER = Logger
			.getLogger(FeaturesPluginsLoader.class);


	private FeaturesPluginsLoader() {
	}

	/**
	 * Configurates features reporter
	 */
	static void loadExtractors() {
		String appHome = System.getProperty("app.home");
		if (appHome != null) {
			File dir = new File(appHome, "plugins");
			if (dir.isDirectory() && dir.canRead()) {
				List<FeaturesExtractor> extractors = getAllExtractors(dir);
				for (FeaturesExtractor ext : extractors) {
					FeaturesReporter.registerFeaturesExtractor(ext);
				}
			} else {
				LOGGER.warn("Plugins folder does not exist.");
			}
		} else {
			LOGGER.warn("Can not get system property \"app.home\"");
		}
	}

	private static List<FeaturesExtractor> getAllExtractors(File dir) {
		List<File> jars = getAllJars(dir);
		List<FeaturesExtractor> extractors = loadAllExtractors(jars);

		return extractors;
	}

	private static List<File> getAllJars(File dir) {
		List<File> jars = new ArrayList<>();
		File[] files = dir.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					addJar(f, jars);
				}
			}
		}
		return jars;
	}

	private static void addJar(File dir, List<File> jars) {
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (File f : files) {
			if (f.isFile() && f.getName().endsWith(".jar")) {
				jars.add(f);
				return;
			}
		}

	}

	private static List<FeaturesExtractor> loadAllExtractors(List<File> jars) {
		List<FeaturesExtractor> extractors = new ArrayList<>();
		Set<String> extractorsIds = new HashSet<>();
		for (File jar : jars) {
			try {
				List<String> classNames = getAllClassNamesFromJAR(jar);
				loadAllExtractorsWithUniqueIDsByClassNames(extractors, extractorsIds, jar, classNames);
			} catch (IOException e) {
				LOGGER.error("Can not load extractors from file with path " + jar.getPath(), e);
			}
		}
		return extractors;
	}

	private static List<String> getAllClassNamesFromJAR(File jar) throws IOException {
		List<String> classNames = new ArrayList<>();
		JarInputStream jarStream = new JarInputStream(new FileInputStream(jar));
		JarEntry jarEntry = jarStream.getNextJarEntry();
		while (jarEntry != null) {
			if (jarEntry.getName().endsWith(".class")) {
				String className = jarEntry.getName().replaceAll("/", ".");
				String myClass = className.substring(0, className.lastIndexOf('.'));
				classNames.add(myClass);
			}
			jarEntry = jarStream.getNextJarEntry();
		}
		return classNames;
	}

	private static void loadAllExtractorsWithUniqueIDsByClassNames(List<FeaturesExtractor> toAdd,
																   Set<String> uniqueIds,
																   File jar,
																   List<String> classNames) throws MalformedURLException {
		URL url = jar.toURI().toURL();
		URL[] urls = new URL[]{url};
		ClassLoader cl = new URLClassLoader(urls);
		Class extractorClass = null;
		for (String className : classNames) {
			try {
				Class cls = cl.loadClass(className);
				if (FeaturesExtractor.class.isAssignableFrom(cls)) {
					if (extractorClass == null) {
						extractorClass = cls;
					} else {
						LOGGER.error("JAR file " + jar.getAbsolutePath() + " contains more than one extractor.");
						return;
					}
				}
			} catch (ClassNotFoundException e) {
				LOGGER.error("Can not load class " + className + " from jar " + jar.getAbsolutePath(), e);
			}
		}
		if (extractorClass != null) {
			try {
				Object obj = extractorClass.newInstance();
				FeaturesExtractor extractor = (FeaturesExtractor) obj;
				String extractorID = extractor.getID();
				if (!uniqueIds.contains(extractorID)) {
					uniqueIds.add(extractorID);
					extractor.initialize(jar.getParentFile().toPath());
					toAdd.add(extractor);
				} else {
					LOGGER.error("Founded extractor with the same ID as already loaded extractor. Extractor name: "
							+ extractorClass.getName() + ", jar file:" + jar.getAbsolutePath());
				}
			} catch (InstantiationException | IllegalAccessException e) {
				LOGGER.error("Can not create an instance of the class " + extractorClass.getName() + " from jar " + jar.getPath(), e);
			}
		}
	}
}
