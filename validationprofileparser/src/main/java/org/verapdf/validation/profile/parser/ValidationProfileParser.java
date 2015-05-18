package org.verapdf.validation.profile.parser;

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
public class ValidationProfileParser {

    private Set<String> profilesPaths;
    private ValidationProfile profile;
    private DocumentBuilder builder;

    private ValidationProfileParser(File resourceFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        builder = factory.newDocumentBuilder();
        Document doc = builder.parse(resourceFile);

        profilesPaths = new HashSet<String>();
        profilesPaths.add(resourceFile.getPath());
        Node root = doc.getDocumentElement();
        root.normalize();
        parseRoot(root);
    }

    private void parseRoot(Node root) throws IOException, SAXException {
        String model = null;
        String name = null;
        String description = null;
        String creator = null;
        String created = null;
        String hash = null;
        Map<String, List<Rule>> rules = new HashMap<String, List<Rule>>();

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
                parseImports(child, rules);

            } else if (childName.equals("rules")) {
                parseRules(child, rules);

            }
        }


        profile = new ValidationProfile(model, name, description, creator, created, hash, rules);
    }

    private void parseImports(Node imports, Map<String, List<Rule>> rules) throws IOException, SAXException {
        NodeList children = imports.getChildNodes();

        for(int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);

            if (child.getNodeName().equals("#text"))
                continue;

            String path = child.getTextContent().trim();

            if(profilesPaths.contains(path)){
                continue;
            }
            profilesPaths.add(path);

            File profile = new File(path);

            Document doc = builder.parse(profile);


            NodeList children2 = doc.getDocumentElement().getChildNodes();

            for(int j = 0; j < children2.getLength(); ++j){
                Node child2 = children2.item(j);
                String name = child2.getNodeName();

                if (name.equals("rules")) {
                    parseRules(child2, rules);

                } else if (name.equals("imports")) {
                    parseImports(child2, rules);

                }
            }
        }
    }

    private void parseRules(Node rules, Map<String, List<Rule>> rulesMap){
        NodeList children = rules.getChildNodes();

        for(int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child.getNodeName().equals("#text"))
                continue;
            Rule rule = parseRule(child);

            if (rulesMap.get(rule.getAttr_object()) == null){
                List<Rule> newRules = new ArrayList<Rule>();
                rulesMap.put(rule.getAttr_object(), newRules);
            }

            rulesMap.get(rule.getAttr_object()).add(rule);
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
        List<Fix> fix = new ArrayList<Fix>();

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
        List<String> argument = new ArrayList<String>();

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
                Node textNode = null;
                for (int j = 0; j < nodelist.getLength(); ++j){
                    if (!nodelist.item(j).getNodeName().equals("#text")){
                        textNode = nodelist.item(j);
                        break;
                    }
                }
                info = new FixInfo(textNode.getTextContent().trim());

            } else if (childName.equals("error")) {
                NodeList nodelist = child.getChildNodes();
                Node textNode = null;

                for (int j = 0; j < nodelist.getLength(); ++j){
                    if (!nodelist.item(j).getNodeName().equals("#text")){
                        textNode = nodelist.item(j);
                        break;
                    }
                }

                error = new FixError(textNode.getTextContent().trim());

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
    public static ValidationProfile parseValidationProfile(String resourcePath) throws ParserConfigurationException, SAXException, IOException {
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
    public static ValidationProfile parseValidationProfile(File resourceFile) throws ParserConfigurationException, SAXException, IOException {
        ValidationProfileParser parser = new ValidationProfileParser(resourceFile);
        return parser.profile;
    }
}
