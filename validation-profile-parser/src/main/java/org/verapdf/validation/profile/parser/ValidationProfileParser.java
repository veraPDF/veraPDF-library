package org.verapdf.validation.profile.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.verapdf.exceptions.validationprofileparser.MissedHashTagException;
import org.verapdf.exceptions.validationprofileparser.WrongSignatureException;
import org.verapdf.validation.profile.model.Fix;
import org.verapdf.validation.profile.model.Reference;
import org.verapdf.validation.profile.model.Rule;
import org.verapdf.validation.profile.model.RuleError;
import org.verapdf.validation.profile.model.ValidationProfile;
import org.verapdf.validation.profile.model.Variable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class is for parse the validation profile xml file into java classes.
 *
 * @author Maksim Bezrukov
 */
public final class ValidationProfileParser {
    private static final String ARGUMENT = "argument";
    private static final String CLAUSE = "clause";
    private static final String CREATED = "created";
    private static final String CREATOR = "creator";
    private static final String DEFAULT_VALUE = "defaultValue";
    private static final String DESCRIPTION = "description";
    private static final String ERROR = "error";
    private static final String FIX = "fix";
    private static final String HASH = "hash";
    private static final String ID = "id";
    private static final String IMPORT = "import";
    private static final String IMPORTS = "imports";
    private static final String INFO = "info";
    private static final String MESSAGE = "message";
    private static final String MODEL = "model";
    private static final String NAME = "name";
    private static final String OBJECT = "object";
    private static final String REFERENCE = "reference";
    private static final String RULE = "rule";
    private static final String RULES = "rules";
    private static final String SPECIFICATION = "specification";
    private static final String TEST = "test";
    private static final String VALUE = "value";
    private static final String VARIABLES = "variables";
    private static final String VARIABLE = "variable";
    private static final String WARNING = "warning";

    private Set<String> profilesPaths;
    private ValidationProfile profile;
    private DocumentBuilder builder;
    private File resource;

    private ValidationProfileParser(File resourceFile, boolean isSignCheckOn)
            throws ParserConfigurationException, IOException, SAXException,
            XMLStreamException, MissedHashTagException, WrongSignatureException {
        this.resource = resourceFile;

        if (isSignCheckOn) {
            ValidationProfileSignatureChecker checker = ValidationProfileSignatureChecker
                    .newInstance(this.resource);
            if (!checker.isValidSignature()) {
                throw new WrongSignatureException(
                        "Unsigned validation profile: "
                                + this.resource.getCanonicalPath());
            }
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        this.builder = factory.newDocumentBuilder();

        factory.setIgnoringElementContentWhitespace(true);

        Document doc = this.builder.parse(this.resource);

        this.profilesPaths = new HashSet<>();
        this.profilesPaths.add(resourceFile.getCanonicalPath());
        Node root = doc.getDocumentElement();
        root.normalize();
        parseRoot(root, isSignCheckOn);
    }

    private void parseRoot(Node root, boolean isSignCheckOn)
            throws IOException, SAXException {
        String model = null;
        String name = null;
        String description = null;
        String creator = null;
        String created = null;
        String hash = null;
        Map<String, List<Rule>> rules = new HashMap<>();
        Map<String, List<Variable>> variables = new HashMap<>();

        Node modelNode = root.getAttributes().getNamedItem(MODEL);

        if (modelNode != null) {
            model = modelNode.getNodeValue();
        }

        NodeList children = root.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            String childName = child.getNodeName();

            switch (childName) {
            case NAME:
                name = child.getTextContent().trim();
                break;
            case DESCRIPTION:
                description = child.getTextContent().trim();
                break;
            case CREATOR:
                creator = child.getTextContent().trim();
                break;
            case CREATED:
                created = child.getTextContent().trim();
                break;
            case HASH:
                if (isSignCheckOn) {
                    hash = child.getTextContent().trim();
                }
                break;
            case IMPORTS:
                parseImports(this.resource, child, rules);
                break;
            case RULES:
                parseRules(child, rules);
                break;
            case VARIABLES:
                parseVariables(child, variables);
                break;
            default:
                // White space node or some another node, which doesn't a part
                // of a validation profile
                // by specification. So do nothing.
            }
        }

        this.profile = new ValidationProfile(model, name, description, creator,
                created, hash, rules, variables);
    }

    private void parseImports(File sourceFile, Node imports,
            Map<String, List<Rule>> rules) throws SAXException, IOException {
        NodeList children = imports.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);

            if (!child.getNodeName().equals(IMPORT)) {
                continue;
            }

            String path = child.getTextContent().trim();

            File newFile = new File(sourceFile.getParent(), path);

            if (!newFile.exists()) {
                throw new FileNotFoundException(
                        "Can not find import with path \"" + path
                                + "\" directly to the given profile.");
            }

            if (this.profilesPaths.contains(newFile.getCanonicalPath())) {
                continue;
            }

            this.profilesPaths.add(newFile.getCanonicalPath());

            Document doc = this.builder.parse(newFile);

            NodeList children2 = doc.getDocumentElement().getChildNodes();

            for (int j = 0; j < children2.getLength(); ++j) {
                Node child2 = children2.item(j);
                String name = child2.getNodeName();

                switch (name) {
                case RULES:
                    parseRules(child2, rules);
                    break;

                case IMPORTS:
                    parseImports(newFile, child2, rules);
                    break;

                default:
                    // White space node or some another node, which doesn't a
                    // part of a validation profile
                    // by specification. So do nothing.
                }
            }
        }
    }

    private static void parseRules(Node rules, Map<String, List<Rule>> rulesMap) {
        NodeList children = rules.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (RULE.equals(child.getNodeName())) {
                Rule rule = parseRule(child);

                if (!rulesMap.containsKey(rule.getAttrObject())) {
                    rulesMap.put(rule.getAttrObject(), new ArrayList<Rule>());
                }

                rulesMap.get(rule.getAttrObject()).add(rule);
            }
        }
    }

    private static Rule parseRule(Node rule) {
        String id = null;
        String object = null;
        String description = null;
        String test = null;
        RuleError ruleError = null;
        boolean isHasError = false;
        Reference reference = null;
        List<Fix> fix = new ArrayList<>();

        Node idNode = rule.getAttributes().getNamedItem(ID);

        if (idNode != null) {
            id = idNode.getNodeValue();
        }

        Node objectNode = rule.getAttributes().getNamedItem(OBJECT);

        if (objectNode != null) {
            object = objectNode.getNodeValue();
        }

        NodeList children = rule.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);

            switch (child.getNodeName()) {
            case DESCRIPTION:
                description = child.getTextContent().trim();
                break;
            case TEST:
                test = child.getTextContent().trim();
                break;
            case ERROR:
                ruleError = parseRuleError(child);
                isHasError = true;
                break;
            case WARNING:
                ruleError = parseRuleError(child);
                break;
            case REFERENCE:
                reference = parseReference(child);
                break;
            case FIX:
                fix.add(parseFix(child));
                break;
            default:
                // White space node or some another node, which doesn't a part
                // of a validation profile
                // by specification. So do nothing.
            }
        }

        return new Rule(id, object, description, ruleError, isHasError, test,
                reference, fix);

    }

    private static void parseVariables(Node rules,
            Map<String, List<Variable>> variablesMap) {
        NodeList children = rules.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child.getNodeName().equals(VARIABLE)) {
                Variable variable = parseVariable(child);

                if (variablesMap.get(variable.getAttrObject()) == null) {
                    List<Variable> newVariables = new ArrayList<>();
                    variablesMap.put(variable.getAttrObject(), newVariables);
                }

                variablesMap.get(variable.getAttrObject()).add(variable);
            }
        }
    }

    private static Variable parseVariable(Node rule) {
        String name = null;
        String object = null;
        String defaultValue = null;
        String value = null;

        Node nameNode = rule.getAttributes().getNamedItem(NAME);

        if (nameNode != null) {
            name = nameNode.getNodeValue();
        }

        Node objectNode = rule.getAttributes().getNamedItem(OBJECT);

        if (objectNode != null) {
            object = objectNode.getNodeValue();
        }

        NodeList children = rule.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            String childName = child.getNodeName();

            switch (childName) {
            case DEFAULT_VALUE:
                defaultValue = child.getTextContent().trim();
                break;
            case VALUE:
                value = child.getTextContent().trim();
                break;
            default:
                // White space node or some another node, which doesn't a part
                // of a validation profile
                // by specification. So do nothing.

            }
        }

        return new Variable(name, object, defaultValue, value);

    }

    private static RuleError parseRuleError(Node err) {
        String message = null;
        List<String> argument = new ArrayList<>();

        NodeList children = err.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            String childName = child.getNodeName();

            switch (childName) {
            case MESSAGE:
                message = child.getTextContent().trim();
                break;

            case ARGUMENT:
                argument.add(child.getTextContent().trim());
                break;

            default:
                // White space node or some another node, which doesn't a part
                // of a validation profile
                // by specification. So do nothing.
            }
        }

        return new RuleError(message, argument);

    }

    private static Reference parseReference(Node ref) {
        String specification = null;
        String clause = null;
        List<Reference> references = new ArrayList<>();

        NodeList children = ref.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            String childName = child.getNodeName();

            switch (childName) {
            case SPECIFICATION:
                specification = child.getTextContent().trim();
                break;

            case CLAUSE:
                clause = child.getTextContent().trim();
                break;

            case REFERENCE:
                references.add(parseReference(child));
                break;

            default:
                // White space node or some another node, which doesn't a part
                // of a validation profile
                // by specification. So do nothing.

            }
        }

        return new Reference(specification, clause, references);

    }

    private static Fix parseFix(Node fix) {
        String id = null;
        String description = null;
        String info = null;
        String error = null;

        Node idNode = fix.getAttributes().getNamedItem("id");

        if (idNode != null) {
            id = idNode.getNodeValue();
        }

        NodeList children = fix.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            String childName = child.getNodeName();
            NodeList descendants;

            switch (childName) {
            case DESCRIPTION:
                description = child.getTextContent().trim();
                break;

            case INFO:
                descendants = child.getChildNodes();
                String infoMessageContent = getFirstMessageNodeTextContent(descendants);
                info = infoMessageContent;
                break;

            case ERROR:
                descendants = child.getChildNodes();
                String errorMessageContent = getFirstMessageNodeTextContent(descendants);
                error = errorMessageContent;
                break;
            default:
                break;
            }
        }

        return new Fix(id, description, info, error);
    }

    private static String getFirstMessageNodeTextContent(final NodeList nodeList) {
        for (int j = 0; j < nodeList.getLength(); ++j) {
            if (MESSAGE.equals(nodeList.item(j).getNodeName())) {
                return nodeList.item(j).getTextContent().trim();
            }
        }
        return null;
    }

    /**
     * Parses validation profile xml.
     *
     * @param profileFilePath
     *            - Path to the file for parse.
     * @param isSignCheckOn
     * @return Validation profile represent in Java classes.
     * @throws ParserConfigurationException
     *             if a DocumentBuilder cannot be created which satisfies the
     *             configuration requested.
     * @throws IOException
     *             if any IO errors occur.
     * @throws FileNotFoundException
     *             if the profileFile is not an existing file
     * @throws SAXException
     *             if any parse errors occur.
     * @throws MissedHashTagException
     *             if validation profile must be signed, but it has no hash tag
     * @throws XMLStreamException
     *             if exception occurs in parsing a validation profile with xml
     *             stream (in checking signature of the validation profile)
     * @throws WrongSignatureException
     *             if validation profile must be signed, but it has wrong
     *             signature
     * @throws UnsupportedEncodingException
     *             if validation profile has not utf8 encoding
     */
    public static ValidationProfile parseFromFilePath(String profileFilePath,
            boolean isSignCheckOn) throws ParserConfigurationException,
            SAXException, IOException, MissedHashTagException,
            XMLStreamException, WrongSignatureException {
        if (profileFilePath == null)
            throw new IllegalArgumentException(
                    "Parameter (String profileFilePath) can not be null");
        if (profileFilePath.isEmpty())
            throw new IllegalArgumentException(
                    "Parameter (String profileFilePath) can not be an empty String");
        return parseFromFile(new File(profileFilePath), isSignCheckOn);
    }

    /**
     * @param profileFile
     *            File for parse.
     * @param isSignCheckOn
     * @return Validation profile represent in Java classes.
     * @throws ParserConfigurationException
     *             if a DocumentBuilder cannot be created which satisfies the
     *             configuration requested.
     * @throws IOException
     *             if any IO errors occur.
     * @throws FileNotFoundException
     *             if the profileFile is not an existing file
     * @throws SAXException
     *             if any parse errors occur.
     * @throws MissedHashTagException
     *             if validation profile must be signed, but it has no hash tag
     * @throws XMLStreamException
     *             if exception occurs in parsing a validation profile with xml
     *             stream (in checking signature of the validation profile)
     * @throws WrongSignatureException
     *             if validation profile must be signed, but it has wrong
     *             signature
     * @throws UnsupportedEncodingException
     *             if validation profile has not utf8 encoding
     */
    public static ValidationProfile parseFromFile(File profileFile,
            boolean isSignCheckOn) throws ParserConfigurationException,
            SAXException, IOException, MissedHashTagException,
            XMLStreamException, WrongSignatureException {
        if (profileFile == null)
            throw new IllegalArgumentException(
                    "Parameter (File resourceFile) can not be null");
        if (!profileFile.isFile())
            throw new IllegalArgumentException(
                    "Parameter (File resourceFile) must be an existing file.");
        return new ValidationProfileParser(profileFile, isSignCheckOn).profile;
    }
}
