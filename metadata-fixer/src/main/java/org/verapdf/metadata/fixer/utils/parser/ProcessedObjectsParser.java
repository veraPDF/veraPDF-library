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

	ProcessedObjects getProcessedObjects() throws IOException, URISyntaxException, ParserConfigurationException, SAXException;

	ProcessedObjects getProcessedObjects(String path) throws IOException, SAXException, ParserConfigurationException;

	ProcessedObjects getProcessedObjects(InputStream file) throws ParserConfigurationException, IOException, SAXException;

	String getProcessedObjectsPathProperty();

}
