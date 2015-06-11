package org.verapdf.validation.profile.parser;

import org.verapdf.exceptions.validationprofileparser.IncorrectImportPathException;
import org.verapdf.validation.profile.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class is for parse the validation profile xml file into java classes.
 * Created by bezrukov on 4/24/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public final class ValidationProfileParser {

    private Set<String> profilesPaths;
    private ValidationProfile profile;
    private DocumentBuilder builder;
    private File resource;

    private ValidationProfileParser(File resourceFile) throws ParserConfigurationException, IOException, SAXException, IncorrectImportPathException {
        resource = resourceFile;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        builder = factory.newDocumentBuilder();

        factory.setIgnoringElementContentWhitespace(true);

        Document doc = builder.parse(resource);

        profilesPaths = new HashSet<>();
        profilesPaths.add(resourceFile.getCanonicalPath());
        Node root = doc.getDocumentElement();
        root.normalize();
        parseRoot(root);
    }

    private void parseRoot(Node root) throws IOException, SAXException, IncorrectImportPathException {
        String model = null;
        String name = null;
        String description = null;
        String creator = null;
        String created = null;
        String hash = null;
        Map<String, List<Rule>> rules = new HashMap<>();

        Node modelNode = root.getAttributes().getNamedItem("model");

        if (modelNode != null){
            model = modelNode.getNodeValue();
        }

        NodeList children = root.getChildNodes();

        for(int i = 0; i < children.getLength(); ++i){
            Node child = children.item(i);
            String childName = child.getNodeName();

            if (childName.equals("name")) {
                name = child.getTextContent().trim();

            } else if (childName.equals("description")) {
                description = child.getTextContent().trim();

            } else if (childName.equals("creator")) {
                creator = child.getTextContent().trim();

            } else if (childName.equals("created")) {
                created = child.getTextContent().trim();

            } else if (childName.equals("hash")) {
                hash = child.getTextContent().trim();

            } else if (childName.equals("imports")) {
                parseImports(resource, child, rules);

            } else if (childName.equals("rules")) {
                parseRules(child, rules);

            }
        }


        profile = new ValidationProfile(model, name, description, creator, created, hash, rules);
    }

    private void parseImports(File sourceFile, Node imports, Map<String, List<Rule>> rules) throws SAXException, IncorrectImportPathException, IOException {
        NodeList children = imports.getChildNodes();

        for(int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);

            if (!child.getNodeName().equals("import")) {
                continue;
            }

            String path = child.getTextContent().trim();

            File newFile = new File(sourceFile.getParent(), path);

            if (newFile == null || !newFile.exists()){
                throw new IncorrectImportPathException("Can not find import with path \"" + path + "\" directly to the given profile.");
            }

            if(profilesPaths.contains(newFile.getCanonicalPath())){
                continue;
            }

            profilesPaths.add(newFile.getCanonicalPath());

            Document doc = builder.parse(newFile);

            NodeList children2 = doc.getDocumentElement().getChildNodes();

            for(int j = 0; j < children2.getLength(); ++j){
                Node child2 = children2.item(j);
                String name = child2.getNodeName();

                if (name.equals("rules")) {
                    parseRules(child2, rules);

                } else if (name.equals("imports")) {
                    parseImports(newFile, child2, rules);

                }
            }
        }
    }

    private void parseRules(Node rules, Map<String, List<Rule>> rulesMap){
        NodeList children = rules.getChildNodes();

        for(int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child.getNodeName().equals("rule")) {
                Rule rule = parseRule(child);

                if (rulesMap.get(rule.getAttrObject()) == null) {
                    List<Rule> newRules = new ArrayList<>();
                    rulesMap.put(rule.getAttrObject(), newRules);
                }

                rulesMap.get(rule.getAttrObject()).add(rule);
            }
        }
    }

    private Rule parseRule(Node rule){
        String id = null;
        String object = null;
        String description = null;
        String test = null;
        RuleError ruleError = null;
        boolean isHasError = false;
        Reference reference = null;
        List<Fix> fix = new ArrayList<>();

        Node idNode = rule.getAttributes().getNamedItem("id");

        if (idNode != null){
            id = idNode.getNodeValue();
        }

        Node objectNode = rule.getAttributes().getNamedItem("object");

        if (objectNode != null){
            object = objectNode.getNodeValue();
        }

        NodeList children = rule.getChildNodes();

        for(int i = 0; i < children.getLength(); ++i){
            Node child = children.item(i);
            String childName = child.getNodeName();

            if (childName.equals("description")) {
                description = child.getTextContent().trim();

            } else if (childName.equals("test")) {
                test = child.getTextContent().trim();

            } else if (childName.equals("error")) {
                ruleError = parseRuleError(child);
                isHasError = true;

            } else if (childName.equals("warning")) {
                ruleError = parseRuleError(child);

            } else if (childName.equals("reference")) {
                reference = parseReference(child);

            } else if (childName.equals("fix")) {
                fix.add(parseFix(child));
            }
        }


        return new Rule(id, object, description, ruleError, isHasError, test, reference, fix);

    }

    private RuleError parseRuleError(Node err){
        String message = null;
        List<String> argument = new ArrayList<>();

        NodeList children = err.getChildNodes();

        for(int i = 0; i < children.getLength(); ++i){
            Node child = children.item(i);
            String childName = child.getNodeName();

            if (childName.equals("message")) {
                message = child.getTextContent().trim();

            } else if (childName.equals("argument")) {
                argument.add(child.getTextContent().trim());
            }
        }


        return new RuleError(message, argument);

    }

    private Reference parseReference(Node ref){
        String specification = null;
        String clause = null;

        NodeList children = ref.getChildNodes();

        for(int i = 0; i < children.getLength(); ++i){
            Node child = children.item(i);
            String childName = child.getNodeName();

            if (childName.equals("specification")) {
                specification = child.getTextContent().trim();

            } else if (childName.equals("clause")) {
                clause = child.getTextContent().trim();

            }
        }


        return new Reference(specification, clause);

    }

    private Fix parseFix(Node fix){
        String id = null;
        String description = null;
        FixInfo info = null;
        FixError error = null;

        Node idNode = fix.getAttributes().getNamedItem("id");

        if (idNode != null){
            id = idNode.getNodeValue();
        }

        NodeList children = fix.getChildNodes();

        for(int i = 0; i < children.getLength(); ++i){
            Node child = children.item(i);
            String childName = child.getNodeName();

            if (childName.equals("description")) {
                description = child.getTextContent().trim();

            } else if (childName.equals("info")) {
                NodeList nodelist = child.getChildNodes();
                for (int j = 0; j < nodelist.getLength(); ++j){
                    if (nodelist.item(j).getNodeName().equals("message")){
                        info = new FixInfo(nodelist.item(j).getTextContent().trim());
                        break;
                    }
                }

            } else if (childName.equals("error")) {
                NodeList nodelist = child.getChildNodes();
                for (int j = 0; j < nodelist.getLength(); ++j){
                    if (nodelist.item(j).getNodeName().equals("message")){
                        error = new FixError(nodelist.item(j).getTextContent().trim());
                        break;
                    }
                }

            }
        }


        return new Fix(id, description, info, error);

    }

    /**
     * Parses validation profile xml.
     * @param resourcePath - Path to the file for parse.
     * @return Validation profile represent in Java classes.
     * @throws ParserConfigurationException - if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws IOException - If any IO errors occur.
     * @throws SAXException - If any parse errors occur.
     */
    public static ValidationProfile parseValidationProfile(String resourcePath) throws ParserConfigurationException, SAXException, IOException, IncorrectImportPathException {
        return parseValidationProfile(new File(resourcePath));
    }

    /**
     *
     * @param resourceFile - File for parse.
     * @return Validation profile represent in Java classes.
     * @throws ParserConfigurationException - if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws IOException - If any IO errors occur.
     * @throws SAXException - If any parse errors occur.
     */
    public static ValidationProfile parseValidationProfile(File resourceFile) throws ParserConfigurationException, SAXException, IOException, IncorrectImportPathException {
        return new ValidationProfileParser(resourceFile).profile;
    }
}
