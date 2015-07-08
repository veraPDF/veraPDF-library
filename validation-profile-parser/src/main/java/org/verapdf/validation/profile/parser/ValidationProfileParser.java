package org.verapdf.validation.profile.parser;

import org.verapdf.exceptions.validationprofileparser.*;
import org.verapdf.validation.profile.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class is for parse the validation profile xml file into java classes.
 *
 * @author Maksim Bezrukov
 */
public final class ValidationProfileParser {

    private Set<String> profilesPaths;
    private ValidationProfile profile;
    private DocumentBuilder builder;
    private File resource;

    private ValidationProfileParser(File resourceFile, boolean isSignCheckOn) throws ParserConfigurationException, IOException, SAXException, IncorrectImportPathException, XMLStreamException, MissedHashTagException, WrongSignatureException, WrongProfileEncodingException, NullProfileException {
        resource = resourceFile;

        if (isSignCheckOn) {
            ValidationProfileSignatureChecker checker = ValidationProfileSignatureChecker.newInstance(resource);
            if (!checker.isValidSignature()) {
                throw new WrongSignatureException("Unsigned validation profile: " + resource.getCanonicalPath());
            }
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        builder = factory.newDocumentBuilder();

        factory.setIgnoringElementContentWhitespace(true);

        Document doc = builder.parse(resource);

        profilesPaths = new HashSet<>();
        profilesPaths.add(resourceFile.getCanonicalPath());
        Node root = doc.getDocumentElement();
        root.normalize();
        parseRoot(root, isSignCheckOn);
    }

    private void parseRoot(Node root, boolean isSignCheckOn) throws IOException, SAXException, IncorrectImportPathException {
        String model = null;
        String name = null;
        String description = null;
        String creator = null;
        String created = null;
        String hash = null;
        Map<String, List<Rule>> rules = new HashMap<>();
        Map<String, List<Variable>> variables = new HashMap<>();

        Node modelNode = root.getAttributes().getNamedItem("model");

        if (modelNode != null) {
            model = modelNode.getNodeValue();
        }

        NodeList children = root.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            String childName = child.getNodeName();

            switch (childName) {
                case "name":
                    name = child.getTextContent().trim();
                    break;
                case "description":
                    description = child.getTextContent().trim();
                    break;

                case "creator":
                    creator = child.getTextContent().trim();
                    break;
                case "created":
                    created = child.getTextContent().trim();
                    break;
                case "hash":
                    if (isSignCheckOn) {
                        hash = child.getTextContent().trim();
                    }
                    break;
                case "imports":
                    parseImports(resource, child, rules);
                    break;
                case "rules":
                    parseRules(child, rules);
                    break;
                case "variables":
                    parseVariables(child, variables);
                    break;
            }
        }

        profile = new ValidationProfile(model, name, description, creator, created, hash, rules, variables);
    }

    private void parseImports(File sourceFile, Node imports, Map<String, List<Rule>> rules) throws SAXException, IncorrectImportPathException, IOException {
        NodeList children = imports.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);

            if (!child.getNodeName().equals("import")) {
                continue;
            }

            String path = child.getTextContent().trim();

            File newFile = new File(sourceFile.getParent(), path);

            if (newFile == null || !newFile.exists()) {
                throw new IncorrectImportPathException("Can not find import with path \"" + path + "\" directly to the given profile.");
            }

            if (profilesPaths.contains(newFile.getCanonicalPath())) {
                continue;
            }

            profilesPaths.add(newFile.getCanonicalPath());

            Document doc = builder.parse(newFile);

            NodeList children2 = doc.getDocumentElement().getChildNodes();

            for (int j = 0; j < children2.getLength(); ++j) {
                Node child2 = children2.item(j);
                String name = child2.getNodeName();

                switch (name) {
                    case "rules":
                        parseRules(child2, rules);
                        break;

                    case "imports":
                        parseImports(newFile, child2, rules);
                        break;
                }
            }
        }
    }

    private void parseRules(Node rules, Map<String, List<Rule>> rulesMap) {
        NodeList children = rules.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
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

    private Rule parseRule(Node rule) {
        String id = null;
        String object = null;
        String description = null;
        String test = null;
        RuleError ruleError = null;
        boolean isHasError = false;
        Reference reference = null;
        List<Fix> fix = new ArrayList<>();

        Node idNode = rule.getAttributes().getNamedItem("id");

        if (idNode != null) {
            id = idNode.getNodeValue();
        }

        Node objectNode = rule.getAttributes().getNamedItem("object");

        if (objectNode != null) {
            object = objectNode.getNodeValue();
        }

        NodeList children = rule.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            String childName = child.getNodeName();

            switch (childName) {
                case "description":
                    description = child.getTextContent().trim();
                    break;
                case "test":
                    test = child.getTextContent().trim();
                    break;
                case "error":
                    ruleError = parseRuleError(child);
                    isHasError = true;
                    break;
                case "warning":
                    ruleError = parseRuleError(child);
                    break;
                case "reference":
                    reference = parseReference(child);
                    break;
                case "fix":
                    fix.add(parseFix(child));
                    break;
            }
        }


        return new Rule(id, object, description, ruleError, isHasError, test, reference, fix);

    }

    private void parseVariables(Node rules, Map<String, List<Variable>> variablesMap) {
        NodeList children = rules.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child.getNodeName().equals("variable")) {
                Variable variable = parseVariable(child);

                if (variablesMap.get(variable.getAttrObject()) == null) {
                    List<Variable> newVariables = new ArrayList<>();
                    variablesMap.put(variable.getAttrObject(), newVariables);
                }

                variablesMap.get(variable.getAttrObject()).add(variable);
            }
        }
    }

    private Variable parseVariable(Node rule) {
        String name = null;
        String object = null;
        String defaultValue = null;
        String value = null;

        Node nameNode = rule.getAttributes().getNamedItem("name");

        if (nameNode != null) {
            name = nameNode.getNodeValue();
        }

        Node objectNode = rule.getAttributes().getNamedItem("object");

        if (objectNode != null) {
            object = objectNode.getNodeValue();
        }

        NodeList children = rule.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            String childName = child.getNodeName();

            switch (childName) {
                case "defaultValue":
                    defaultValue = child.getTextContent().trim();
                    break;
                case "value":
                    value = child.getTextContent().trim();
                    break;

            }
        }


        return new Variable(name, object, defaultValue, value);

    }

    private RuleError parseRuleError(Node err) {
        String message = null;
        List<String> argument = new ArrayList<>();

        NodeList children = err.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            String childName = child.getNodeName();

            switch (childName) {
                case "message":
                    message = child.getTextContent().trim();
                    break;

                case "argument":
                    argument.add(child.getTextContent().trim());
                    break;
            }
        }


        return new RuleError(message, argument);

    }

    private Reference parseReference(Node ref) {
        String specification = null;
        String clause = null;

        NodeList children = ref.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            String childName = child.getNodeName();

            switch (childName) {
                case "specification":
                    specification = child.getTextContent().trim();
                    break;

                case "clause":
                    clause = child.getTextContent().trim();
                    break;

            }
        }


        return new Reference(specification, clause);

    }

    private Fix parseFix(Node fix) {
        String id = null;
        String description = null;
        FixInfo info = null;
        FixError error = null;

        Node idNode = fix.getAttributes().getNamedItem("id");

        if (idNode != null) {
            id = idNode.getNodeValue();
        }

        NodeList children = fix.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            String childName = child.getNodeName();
            NodeList nodelist;

            switch (childName) {
                case "description":
                    description = child.getTextContent().trim();
                    break;

                case "info":
                    nodelist = child.getChildNodes();
                    for (int j = 0; j < nodelist.getLength(); ++j) {
                        if (nodelist.item(j).getNodeName().equals("message")) {
                            info = new FixInfo(nodelist.item(j).getTextContent().trim());
                            break;
                        }
                    }
                    break;

                case "error":
                    nodelist = child.getChildNodes();
                    for (int j = 0; j < nodelist.getLength(); ++j) {
                        if (nodelist.item(j).getNodeName().equals("message")) {
                            error = new FixError(nodelist.item(j).getTextContent().trim());
                            break;
                        }
                    }
                    break;
            }
        }


        return new Fix(id, description, info, error);

    }

    /**
     * Parses validation profile xml.
     *
     * @param resourcePath - Path to the file for parse.
     * @return Validation profile represent in Java classes.
     * @throws ParserConfigurationException  - if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws IOException                   - if any IO errors occur.
     * @throws SAXException                  - if any parse errors occur.
     * @throws IncorrectImportPathException  - if validation profile contains incorrect input path
     * @throws MissedHashTagException        - if validation profile must be signed, but it has no hash tag
     * @throws XMLStreamException            - if exception occurs in parsing a validation profile with xml stream (in checking signature of the validation profile)
     * @throws WrongSignatureException       - if validation profile must be signed, but it has wrong signature
     * @throws WrongProfileEncodingException - if validation profile has not utf8 encoding
     * @throws NullProfileException          - if resource profile pointer is null
     */
    public static ValidationProfile parseValidationProfile(String resourcePath, boolean isSignCheckOn) throws ParserConfigurationException, SAXException, IOException, IncorrectImportPathException, MissedHashTagException, XMLStreamException, WrongSignatureException, WrongProfileEncodingException, NullProfileException {
        return parseValidationProfile(new File(resourcePath), isSignCheckOn);
    }

    /**
     * @param resourceFile - File for parse.
     * @return Validation profile represent in Java classes.
     * @throws ParserConfigurationException  - if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws IOException                   - if any IO errors occur.
     * @throws SAXException                  - if any parse errors occur.
     * @throws IncorrectImportPathException  - if validation profile contains incorrect input path
     * @throws MissedHashTagException        - if validation profile must be signed, but it has no hash tag
     * @throws XMLStreamException            - if exception occurs in parsing a validation profile with xml stream (in checking signature of the validation profile)
     * @throws WrongSignatureException       - if validation profile must be signed, but it has wrong signature
     * @throws WrongProfileEncodingException - if validation profile has not utf8 encoding
     * @throws NullProfileException          - if resource profile pointer is null
     */
    public static ValidationProfile parseValidationProfile(File resourceFile, boolean isSignCheckOn) throws ParserConfigurationException, SAXException, IOException, IncorrectImportPathException, MissedHashTagException, XMLStreamException, WrongSignatureException, WrongProfileEncodingException, NullProfileException {
        return new ValidationProfileParser(resourceFile, isSignCheckOn).profile;
    }
}
