/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.metadata.fixer.utils.parser;

import org.verapdf.metadata.fixer.utils.model.ProcessedObjects;
import org.verapdf.metadata.fixer.utils.model.RuleDescription;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.verapdf.metadata.fixer.utils.MetadataFixerConstants.*;

/**
 * @author Evgeniy Muravitskiy
 */
public class XMLProcessedObjectsParser implements ProcessedObjectsParser {

    private static final Logger LOGGER = Logger.getLogger(XMLProcessedObjectsParser.class.getCanonicalName());
    private static final String XML_PROCESSED_OBJECTS_PATH_PROPERTY_PDFA_1 = "processed.objects.path.pdfa_1";
    private static final String XML_PROCESSED_OBJECTS_PATH_PROPERTY_PDFA_2_3 = "processed.objects.path.pdfa_2_3";
    private static final String XML_PROCESSED_OBJECTS_PATH_PROPERTY_PDFA_4 = "processed.objects.path.pdfa_4";
    private static final String XML_PROCESSED_OBJECTS_PATH_PROPERTY_PDFUA_1 = "processed.objects.path.pdfua_1";
    private static final String XML_PROCESSED_OBJECTS_PATH_PROPERTY_PDFUA_2 = "processed.objects.path.pdfua_2";

    private static final ClassLoader cl = XMLProcessedObjectsParser.class.getClassLoader();
    private static ProcessedObjectsParser instance;

    private XMLProcessedObjectsParser() {
        // hide default constructor
    }

    @Override
    public ProcessedObjects getProcessedObjects(PDFAFlavour flavour)
            throws IOException, ParserConfigurationException, SAXException {
        Properties prop = new Properties();
        try (InputStream inputStream = cl.getResourceAsStream(PROCESSED_OBJECTS_PROPERTIES_PATH)) {
            prop.load(inputStream);
        }
        String appliedObjectsPath = prop.getProperty(this
                .getProcessedObjectsPathProperty(flavour));
        try (InputStream xml = cl.getResourceAsStream(appliedObjectsPath)) {
            return this.getProcessedObjects(xml);
        }
    }

    @Override
    public ProcessedObjects getProcessedObjects(String path)
            throws IOException, SAXException, ParserConfigurationException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(path))) {
            return this.getProcessedObjects(is);
        }
    }

    @Override
    public ProcessedObjects getProcessedObjects(InputStream xml)
            throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Unable to secure xml processing");
        }
        DocumentBuilder builder = factory.newDocumentBuilder();

        factory.setIgnoringElementContentWhitespace(true);

        Document doc = builder.parse(xml);

        Node root = doc.getDocumentElement();
        root.normalize();

        return XMLProcessedObjectsParser.parse(root);
    }

    @Override
    public String getProcessedObjectsPathProperty(PDFAFlavour flavour) {
        if (flavour.getPart() == PDFAFlavour.Specification.ISO_19005_1) {
            return XML_PROCESSED_OBJECTS_PATH_PROPERTY_PDFA_1;
        }
        if (flavour.getPart() == PDFAFlavour.Specification.ISO_19005_4) {
            return XML_PROCESSED_OBJECTS_PATH_PROPERTY_PDFA_4;
        }
        if (flavour.getPart() == PDFAFlavour.Specification.ISO_14289_1) {
            return XML_PROCESSED_OBJECTS_PATH_PROPERTY_PDFUA_1;
        }
        if (flavour.getPart() == PDFAFlavour.Specification.ISO_14289_2) {
            return XML_PROCESSED_OBJECTS_PATH_PROPERTY_PDFUA_2;
        }
        return XML_PROCESSED_OBJECTS_PATH_PROPERTY_PDFA_2_3;
    }

    private static ProcessedObjects parse(Node root) {
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

    private static RuleDescription parseCheckObject(Node root) {
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
            default:
                break;
            }
        }

        boolean isValidNode = type != null && !type.trim().isEmpty()
                && (test == null || !test.trim().isEmpty());
        return isValidNode ? new RuleDescription(test, type) : null;
    }

    public static ProcessedObjectsParser getInstance() {
        if (instance == null) {
            instance = new XMLProcessedObjectsParser();
        }
        return instance;
    }

}
