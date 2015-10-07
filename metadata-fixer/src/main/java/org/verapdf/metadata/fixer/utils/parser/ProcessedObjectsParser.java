package org.verapdf.metadata.fixer.utils.parser;

import org.verapdf.metadata.fixer.utils.model.ProcessedObjects;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * @author Evgeniy Muravitskiy
 */
public interface ProcessedObjectsParser {

	String PROCESSED_OBJECTS_PROPERTIES_PATH = "/processed-objects.properties";

	String RULE_DESCRIPTION_TAG = "ruleDescription";
	String OBJECT_TYPE_TAG = "objectType";
	String TEST_TAG = "test";

	ProcessedObjects getProcessedObjects() throws IOException, URISyntaxException, ParserConfigurationException, SAXException;

	ProcessedObjects getProcessedObjects(String path) throws IOException, SAXException, ParserConfigurationException;

	ProcessedObjects getProcessedObjects(InputStream file) throws ParserConfigurationException, IOException, SAXException;

	String getProcessedObjectsPathProperty();

}
