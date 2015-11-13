package org.verapdf.features;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
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
	 *
	 * @param reporter features reporter for configurating
	 */
	public static void loadExtractors(FeaturesReporter reporter) {
		String appHome = System.getProperty("app.home");
		if (appHome != null) {
			File dir = new File(appHome, "plugins");
			if (dir.isDirectory() && dir.canRead()) {
				List<FeaturesExtractor> extractors = getAllExtractors(dir);
				for (FeaturesExtractor ext : extractors) {
					reporter.registerFeaturesExtractor(ext);
				}
			} else {
				LOGGER.error("Plugins folder is not exists or it can not be read.");
			}
		} else {
			LOGGER.error("Con not get system property \"app.home\"");
		}
	}

	private static List<FeaturesExtractor> getAllExtractors(File dir) {
		List<FeaturesExtractor> extractors = new ArrayList<>();

		List<File> jars = new ArrayList<>();
		addAllJars(dir, jars);
		for (File jar : jars) {
			try {
				addAllExtractors(jar, extractors);
			} catch (IOException e) {
				LOGGER.error("Can not load extractors from file with path " + jar.getPath(), e);
			}
		}

		return extractors;
	}

	private static void addAllJars(File dir, List<File> jars) {
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (File f : files) {
			if (f.isDirectory()) {
				addJar(f, jars);
			}
		}
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

	private static void addAllExtractors(File jar, List<FeaturesExtractor> extractors) throws IOException {
		JarInputStream jarStream = new JarInputStream(new FileInputStream(jar));
		JarEntry jarEntry = jarStream.getNextJarEntry();
		List<String> classNames = new ArrayList<>();
		while (jarEntry != null) {

			if ((jarEntry.getName().endsWith(".class"))) {
				String className = jarEntry.getName().replaceAll("/", ".");
				String myClass = className.substring(0, className.lastIndexOf('.'));
				classNames.add(myClass);
			}
			jarEntry = jarStream.getNextJarEntry();
		}

		for (String className : classNames) {
			URL url = jar.toURI().toURL();
			URL[] urls = new URL[]{url};
			ClassLoader cl = new URLClassLoader(urls);
			try {
				Class cls = cl.loadClass(className);
				if (FeaturesExtractor.class.isAssignableFrom(cls)) {
					try {
						Object obj = cls.newInstance();
						FeaturesExtractor extractor = (FeaturesExtractor) obj;
						((FeaturesExtractor) obj).initialize(jar.getParentFile().toPath());
						extractors.add(extractor);
					} catch (InstantiationException | IllegalAccessException e) {
						LOGGER.error("Some error while creating an instance of class " + cls.getName(), e);
					}
				}
			} catch (ClassNotFoundException e) {
				LOGGER.error("Can not load class " + className + " from jar " + jar.getPath(), e);
			}
		}

	}
}
