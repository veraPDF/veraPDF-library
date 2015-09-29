package org.verapdf.features;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * Configurates features reporter with given config file
 *
 * @author Maksim Bezrukov
 */
public final class FeaturesReporterConfigurator {

	private static final Logger LOGGER = Logger
			.getLogger(FeaturesReporterConfigurator.class);


	private FeaturesReporterConfigurator() {
	}

	/**
	 * Configurates features reporter
	 *
	 * @param reporter features reporter for configurating
	 * @param config   config file for reporter configurating
	 */
	public static void configurate(FeaturesReporter reporter, File config) {
		if (config != null && config.exists()) {
			try {
				List<ExtractorStructure> exts = parse(config);
				for (ExtractorStructure ext : exts) {
					reporter.registerFeaturesExtractor(ext.extractor, ext.id);
				}
			} catch (ParserConfigurationException | ClassNotFoundException | InstantiationException | IllegalAccessException | SAXException | IOException e) {
				LOGGER.error("Error while loading external features extractor class", e);
			}
		}
	}

	private static List<ExtractorStructure> parse(File config) throws ParserConfigurationException, IOException, SAXException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		factory.setIgnoringElementContentWhitespace(true);
		Document doc = builder.parse(config);
		Node root = doc.getDocumentElement();
		root.normalize();

		List<ExtractorStructure> res = new ArrayList<>();

		if (!"pluginsConfig".equals(root.getNodeName())) {
			return res;
		}

		NodeList list = root.getChildNodes();
		for (int i = 0; i < list.getLength(); ++i) {
			Node node = list.item(i);
			if ("plugin".equals(node.getNodeName())) {
				ExtractorStructure ext = getExtractor(node);
				if (ext != null) {
					res.add(ext);
				}
			}
		}

		return res;
	}

	private static ExtractorStructure getExtractor(Node node) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
		NodeList list = node.getChildNodes();
		String classPath = null;
		String className = null;
		UUID id = null;
		Map<String, String> parametrs = new HashMap<>();
		for (int i = 0; i < list.getLength(); ++i) {
			Node child = list.item(i);

			switch (child.getNodeName()) {
				case "classPath":
					classPath = child.getTextContent().trim();
					break;
				case "className":
					className = child.getTextContent().trim();
					break;
				case "parameters":
					getParametrs(parametrs, child);
					break;
				case "id":
					try {
						id = UUID.fromString(child.getTextContent().trim());
					} catch (IllegalArgumentException e) {
						LOGGER.error("Attribute id of the plugin is not in UUID format", e);
						return null;
					}
					break;
			}
		}

		if (classPath == null || className == null || id == null) {
			LOGGER.error("Some of required nodes have not founded.");
			return null;
		}

		File file = new File(classPath);
		URL url = file.toURI().toURL();
		URL[] urls = new URL[]{url};
		ClassLoader cl = new URLClassLoader(urls);
		Class cls = cl.loadClass(className);
		Object obj = cls.newInstance();
		if (obj instanceof IFeaturesExtractor) {
			IFeaturesExtractor extractor = (IFeaturesExtractor) obj;
			extractor.initialize(parametrs);
			return new ExtractorStructure(extractor, id);
		}

		return null;
	}

	private static void getParametrs(Map<String, String> parametrs, Node node) {
		NodeList list = node.getChildNodes();
		for (int i = 0; i < list.getLength(); ++i) {
			Node child = list.item(i);
			if ("parameter".equals(child.getNodeName())) {
				NamedNodeMap map = child.getAttributes();
				parametrs.put(map.getNamedItem("name").getTextContent().trim(), map.getNamedItem("value").getTextContent().trim());
			}
		}
	}

	private static class ExtractorStructure {
		UUID id;
		IFeaturesExtractor extractor;

		ExtractorStructure(IFeaturesExtractor extractor, UUID id) {
			this.extractor = extractor;
			this.id = id;
		}
	}
}
