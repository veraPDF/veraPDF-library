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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				List<IFeaturesExtractor> exts = parse(config);
				for (IFeaturesExtractor ext : exts) {
					reporter.registerFeaturesExtractor(ext);
				}
			} catch (ParserConfigurationException | ClassNotFoundException | InstantiationException | IllegalAccessException | SAXException | IOException e) {
				LOGGER.error("Error while loading external features extractor class", e);
			}
		}
	}

	private static List<IFeaturesExtractor> parse(File config) throws ParserConfigurationException, IOException, SAXException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		factory.setIgnoringElementContentWhitespace(true);
		Document doc = builder.parse(config);
		Node root = doc.getDocumentElement();
		root.normalize();

		List<IFeaturesExtractor> res = new ArrayList<>();

		if (!"thirdPartySoftwares".equals(root.getNodeName())) {
			return res;
		}

		NodeList list = root.getChildNodes();
		for (int i = 0; i < list.getLength(); ++i) {
			Node node = list.item(i);
			if ("cli".equals(node.getNodeName())) {
				IFeaturesExtractor ext = getExtractor(node);
				if (ext != null) {
					res.add(ext);
				}
			}
		}

		return res;
	}

	private static IFeaturesExtractor getExtractor(Node node) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
		NodeList list = node.getChildNodes();
		String classPath = null;
		String className = null;
		Map<String, String> parametrs = new HashMap<>();
		for (int i = 0; i < list.getLength(); ++i) {
			Node child = list.item(i);

			if ("classPath".equals(child.getNodeName())) {
				classPath = child.getTextContent().trim();
			} else if ("className".equals(child.getNodeName())) {
				className = child.getTextContent().trim();
			} else if ("parameters".equals(child.getNodeName())) {
				getParametrs(parametrs, child);
			}
		}

		if (classPath == null || className == null) {
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
			return extractor;
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

}
