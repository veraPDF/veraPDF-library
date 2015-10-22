package org.verapdf.metadata.fixer.utils.parser;

import org.verapdf.metadata.fixer.utils.model.ProcessedObjects;
import org.verapdf.metadata.fixer.utils.model.RuleDescription;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

import static org.verapdf.metadata.fixer.utils.MetadataFixerConstants.*;

/**
 * @author Evgeniy Muravitskiy
 */
public class XMLProcessedObjectsParser implements ProcessedObjectsParser {

	private static final String XML_PROCESSED_OBJECTS_PATH_PROPERTY = "processed.objects.path";

	private static ProcessedObjectsParser instance;

	private XMLProcessedObjectsParser() {
		// hide default constructor
	}

	@Override
	public ProcessedObjects getProcessedObjects() throws IOException, URISyntaxException,
			ParserConfigurationException, SAXException {
		Properties prop = new Properties();
		InputStream inputStream = ClassLoader.class.getResourceAsStream(PROCESSED_OBJECTS_PROPERTIES_PATH);
		prop.load(inputStream);
		String appliedObjectsPath = prop.getProperty(this.getProcessedObjectsPathProperty());
		InputStream xml = ClassLoader.class.getResourceAsStream(appliedObjectsPath);
		return this.getProcessedObjects(xml);
	}

	@Override
	public ProcessedObjects getProcessedObjects(String path) throws IOException, SAXException, ParserConfigurationException {
		return this.getProcessedObjects(new BufferedInputStream(new FileInputStream(path)));
	}

	@Override
	public ProcessedObjects getProcessedObjects(InputStream xml) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = factory.newDocumentBuilder();

		factory.setIgnoringElementContentWhitespace(true);

		Document doc = builder.parse(xml);

		Node root = doc.getDocumentElement();
		root.normalize();

		return this.parse(root);
	}

	@Override
	public String getProcessedObjectsPathProperty() {
		return XML_PROCESSED_OBJECTS_PATH_PROPERTY;
	}

	private ProcessedObjects parse(Node root) {
		ProcessedObjects objects = new ProcessedObjects();
		NodeList child = root.getChildNodes();
		for (int i = 0; i < child.getLength(); i++) {
			Node children = child.item(i);
			if (RULE_DESCRIPTION_TAG.equals(children.getNodeName())) {
				RuleDescription object = parseCheckObject(children);
				if (object != null) {
					objects.addCheckObject(object);
				}
			}
		}
		return objects;
	}

	private RuleDescription parseCheckObject(Node root) {
		NodeList child = root.getChildNodes();
		String type = null;
		String test = null;

		for (int i = 0; i < child.getLength(); i++) {
			Node children = child.item(i);
			switch (children.getNodeName()) {
				case OBJECT_TYPE_TAG:
					type = children.getTextContent();
					break;
				case TEST_TAG:
					test = children.getTextContent();
					break;
			}
		}

		boolean isValidNode = type != null && !type.trim().isEmpty() &&
				test != null && !test.trim().isEmpty();
		return isValidNode ? new RuleDescription(test, type) : null;
	}

	public static ProcessedObjectsParser getInstance() {
		if (instance == null) {
			instance = new XMLProcessedObjectsParser();
		}
		return instance;
	}

}
