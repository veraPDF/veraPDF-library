package org.verapdf.validation.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;
import org.verapdf.core.ProfileException;
import org.verapdf.core.ValidationException;
import org.verapdf.model.baselayer.Object;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.Location;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.ValidationResults;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.validation.ProfileDirectory;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.Rule;
import org.verapdf.pdfa.validation.RuleId;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.pdfa.validation.Variable;
import org.xml.sax.SAXException;

/**
 * Validation logic
 *
 * @author Maksim Bezrukov
 */
public class Validator {

    private static final int OPTIMIZATION_LEVEL = 9;
    private static ProfileDirectory PROFILE_DIRECTORY = Profiles
            .getVeraProfileDirectory();

    private Context context;
    private ScriptableObject scope;

    private final Deque<Object> objectsStack = new ArrayDeque<>();
    private final Deque<String> objectsContext = new ArrayDeque<>();
    private final Deque<Set<String>> contextSet = new ArrayDeque<>();
    private final Set<TestAssertion> results = new HashSet<>();

    private Map<RuleId, Script> ruleScripts = new HashMap<>();
    private Map<String, Script> variableScripts = new HashMap<>();

    private Set<String> idSet = new HashSet<>();

    private String rootType;
    private final PDFAFlavour flavour;

    /**
     * Creates new Validator with given validation profile
     *
     * @param profile
     *            validation profile model for validator
     */
    private Validator(final PDFAFlavour flavour) {
        this.flavour = flavour;
    }

    private ValidationResult validate(Object root) throws ValidationException {
        initialize();
        this.rootType = root.getObjectType();
        this.objectsStack.push(root);
        this.objectsContext.push("root");

        Set<String> rootIDContext = new HashSet<>();

        if (root.getID() != null) {
            rootIDContext.add(root.getID());
            this.idSet.add(root.getID());
        }

        this.contextSet.push(rootIDContext);

        while (!this.objectsStack.isEmpty()) {
            checkNext();
        }

        Context.exit();

        return ValidationResults.resultFromValues(this.flavour, this.results);
    }

    private void initialize() {
        this.context = Context.enter();
        this.context.setOptimizationLevel(OPTIMIZATION_LEVEL);
        this.scope = this.context.initStandardObjects();
        this.objectsStack.clear();
        this.objectsContext.clear();
        this.contextSet.clear();
        this.results.clear();
        this.idSet.clear();
        initializeAllVariables();
    }

    private void initializeAllVariables() {
        for (Variable var : PROFILE_DIRECTORY.getValidationProfileByFlavour(
                this.flavour).getVariables()) {
            if (var == null)
                continue;

            java.lang.Object res;
            res = this.context.evaluateString(this.scope,
                    var.getDefaultValue(), null, 0, null);
            if (res instanceof NativeJavaObject) {
                res = ((NativeJavaObject) res).unwrap();
            }

            this.scope.put(var.getName(), this.scope, res);
        }
    }

    private boolean checkNext() throws ValidationException,
            ValidationException, ValidationException {

        Object checkObject = this.objectsStack.pop();
        String checkContext = this.objectsContext.pop();
        Set<String> checkIDContext = this.contextSet.pop();

        boolean res = checkAllRules(checkObject, checkContext);

        updateVariables(checkObject);

        addAllLinkedObjects(checkObject, checkContext, checkIDContext);

        return res;
    }

    private void updateVariables(Object object) {
        if (object != null) {
            for (Variable var : PROFILE_DIRECTORY
                    .getValidationProfileByFlavour(this.flavour)
                    .getVariablesByObject(object.getObjectType())) {

                if (var == null)
                    continue;

                java.lang.Object variable = evalVariableResult(var, object);
                this.scope.put(var.getName(), this.scope, variable);
            }
        }
    }

    private java.lang.Object evalVariableResult(Variable variable, Object object) {
        Script script;
        if (!this.variableScripts.containsKey(variable.getName())) {
            String source = getStringScript(object, variable.getValue());
            script = this.context.compileString(source, null, 0, null);
        } else {
            script = this.variableScripts.get(variable.getName());
        }

        this.scope.put("obj", this.scope, object);

        java.lang.Object res = script.exec(this.context, this.scope);

        if (res instanceof NativeJavaObject) {
            res = ((NativeJavaObject) res).unwrap();
        }
        return res;
    }

    private void addAllLinkedObjects(Object checkObject, String checkContext,
            Set<String> checkIDContext) throws ValidationException,
            ValidationException, ValidationException {
        List<String> links = checkObject.getLinks();
        for (int j = links.size() - 1; j >= 0; --j) {
            String link = links.get(j);

            if (link == null) {
                throw new ValidationException(
                        "There is a null link name in an object. Context: "
                                + checkContext);
            }
            List<? extends Object> objects = checkObject.getLinkedObjects(link);
            if (objects == null) {
                throw new ValidationException(
                        "There is a null link in an object. Context: "
                                + checkContext);
            }

            for (int i = 0; i < objects.size(); ++i) {
                Object obj = objects.get(i);

                StringBuilder path = new StringBuilder(checkContext);
                path.append("/");
                path.append(link);
                path.append("[");
                path.append(i);
                path.append("]");

                if (obj == null) {
                    throw new ValidationException(
                            "There is a null link in an object. Context of the link: "
                                    + path);
                }

                if (checkRequired(obj, checkIDContext)) {
                    this.objectsStack.push(obj);

                    Set<String> newCheckIDContext = new HashSet<>(
                            checkIDContext);

                    if (obj.getID() != null) {
                        path.append("(");
                        path.append(obj.getID());
                        path.append(")");

                        newCheckIDContext.add(obj.getID());
                        this.idSet.add(obj.getID());
                    }

                    this.objectsContext.push(path.toString());
                    this.contextSet.push(newCheckIDContext);
                }
            }
        }
    }

    private boolean checkRequired(Object obj, Set<String> checkIDContext) {

        if (obj.getID() == null) {
            return true;
        } else if (obj.isContextDependent() == null
                || obj.isContextDependent().booleanValue()) {
            return !checkIDContext.contains(obj.getID());
        } else {
            return !this.idSet.contains(obj.getID());
        }
    }

    private boolean checkAllRules(Object checkObject, String checkContext) {
        boolean res = true;
        Set<Rule> roolsForObject = PROFILE_DIRECTORY
                .getValidationProfileByFlavour(this.flavour).getRulesByObject(
                        checkObject.getObjectType());
        for (Rule rule : roolsForObject) {
            res &= checkObjWithRule(checkObject, checkContext, rule);
        }

        for (String checkType : checkObject.getSuperTypes()) {
            roolsForObject = PROFILE_DIRECTORY.getValidationProfileByFlavour(
                    this.flavour).getRulesByObject(checkType);
            if (roolsForObject != null) {
                for (Rule rule : roolsForObject) {
                    if (rule != null) {
                        res &= checkObjWithRule(checkObject, checkContext, rule);
                    }
                }
            }
        }

        return res;
    }

    private static String getScript(Object obj, Rule rule) {
        return getStringScript(obj, "(" + rule.getTest() + ")==true");
    }

    private static String getStringScript(Object obj, String arg) {
        return getScriptPrefix(obj, arg) + arg + getScriptSuffix();
    }

    private static String getScriptPrefix(Object obj, String test) {
        StringBuilder builder = new StringBuilder();
        String[] vars = test.split("\\W");

        for (String prop : obj.getProperties()) {
            if (contains(vars, prop)) {
                builder.append("var ");
                builder.append(prop);
                builder.append(" = obj.get");
                builder.append(prop);
                builder.append("();\n");
            }
        }

        for (String linkName : obj.getLinks()) {
            if (contains(vars, linkName + "_size")) {
                builder.append("var ");
                builder.append(linkName);
                builder.append("_size = obj.getLinkedObjects(\"");
                builder.append(linkName);
                builder.append("\").size();\n");
            }
        }

        builder.append("function test(){return ");

        return builder.toString();
    }

    private static boolean contains(String[] values, String prop) {
        for (String value : values) {
            if (value.equals(prop)) {
                return true;
            }
        }
        return false;
    }

    private static String getScriptSuffix() {
        return ";}\ntest();";
    }

    private boolean checkObjWithRule(Object obj, String cntxtForRule, Rule rule) {
        this.scope.put("obj", this.scope, obj);
        Script scr;
        if (!this.ruleScripts.containsKey(rule.getRuleId())) {
            scr = this.context.compileString(getScript(obj, rule), null, 0,
                    null);
            this.ruleScripts.put(rule.getRuleId(), scr);
        } else {
            scr = this.ruleScripts.get(rule.getRuleId());
        }

        boolean testEvalResult = false;
        testEvalResult = ((Boolean) scr.exec(this.context, this.scope))
                .booleanValue();
        Status assertionStatus = (testEvalResult) ? Status.PASSED
                : Status.FAILED;

        Location location = ValidationResults.locationFromValues(this.rootType,
                cntxtForRule);
        TestAssertion assertion = ValidationResults.assertionFromValues(
                rule.getRuleId(), assertionStatus,
                rule.getError().getMessage(), location);
        this.results.add(assertion);

        return testEvalResult;
    }


    /**
     * Generates validation info for objects with root {@code root} and
     * validation profile path {@code validationProfilePath}
     * <p/>
     * This method needs to parse validation profile (it works slower than those
     * ones, which don't parse profile).
     *
     * @param root
     *            the root object for validation
     * @param validationProfilePath
     *            validation profile's file path
     * @return validation info structure
     * @throws ParserConfigurationException
     *             if a DocumentBuilder cannot be created which satisfies the
     *             configuration requested.
     * @throws IOException
     *             if any IO errors occur.
     * @throws FileNotFoundException
     *             if the profileFile is not an existing file
     * @throws SAXException
     *             if any parse errors occur.
     * @throws ValidationException
     *             if there is a null link name in some object
     * @throws ValidationException
     *             if there is a null link
     * @throws ValidationException
     *             if there is a null object in links list
     * @throws ProfileException
     *             if validation profile must be signed, but it has no hash tag
     * @throws XMLStreamException
     *             if exception occurs in parsing a validation profile with xml
     *             stream (in checking signature of the validation profile)
     * @throws ProfileException
     *             if validation profile must be signed, but it has wrong
     *             signature
     * @throws UnsupportedEncodingException
     *             if validation profile has not utf8 encoding
     * @throws ValidationException
     *             if there is more than one identical global variable names in
     *             the profile model
     * @throws JAXBException
     */
    public static ValidationResult validate(String validationProfilePath,
            Object root) throws IOException, SAXException,
            ParserConfigurationException, ValidationException,
            ValidationException, ValidationException, ProfileException,
            XMLStreamException, ProfileException, ValidationException,
            JAXBException {
        if (validationProfilePath == null) {
            throw new IllegalArgumentException(
                    "Parameter (String validationProfilePath) cannot be null.");
        }
        File profileFile = new File(validationProfilePath);
        return validate(profileFile, root);
    }

    /**
     * Generates validation info for objects with root {@code root} and
     * validation profile file {@code validationProfilePath}
     * <p/>
     * This method needs to parse validation profile (it works slower than those
     * ones, which don't parse profile).
     *
     * @param root
     *            the root object for validation
     * @param validationProfile
     *            validation profile's file
     * @return validation info structure
     * @throws ParserConfigurationException
     *             if a DocumentBuilder cannot be created which satisfies the
     *             configuration requested.
     * @throws IOException
     *             If any IO errors occur.
     * @throws FileNotFoundException
     *             if the profileFile is not an existing file
     * @throws SAXException
     *             If any parse errors occur.
     * @throws ValidationException
     *             if there is a null link name in some object
     * @throws ValidationException
     *             if there is a null link
     * @throws ValidationException
     *             if there is a null object in links list
     * @throws ProfileException
     *             if validation profile must be signed, but it has no hash tag
     * @throws XMLStreamException
     *             if exception occurs in parsing a validation profile with xml
     *             stream (in checking signature of the validation profile)
     * @throws ProfileException
     *             if validation profile must be signed, but it has wrong
     *             signature
     * @throws UnsupportedEncodingException
     *             if validation profile has not utf8 encoding
     * @throws ValidationException
     *             if there is more than one identical global variable names in
     *             the profile model
     * @throws JAXBException
     */
    public static ValidationResult validate(File validationProfile, Object root)
            throws ParserConfigurationException, SAXException, IOException,
            ValidationException, ValidationException, ValidationException,
            ProfileException, XMLStreamException, ProfileException,
            ValidationException, JAXBException {
        if (validationProfile == null) {
            throw new IllegalArgumentException(
                    "Parameter (ValidationProfile validationProfile) cannot be null.");
        }
        try (InputStream fis = new FileInputStream(validationProfile)) {
            ValidationProfile profile = Profiles.profileFromXml(fis);
            return validate(profile.getPDFAFlavour(), root);
        }
    }

    /**
     * Generates validation info for objects with root {@code root} and
     * validation profile structure {@code validationProfile}
     * <p/>
     * This method doesn't need to parse validation profile (it works faster
     * than those ones, which parses profile).
     * @param flavour 
     *
     * @param root
     *            the root object for validation
     * @return validation info structure
     * @throws ValidationException
     *             when a problem occurs validating the PDF
     */
    public static ValidationResult validate(PDFAFlavour flavour, Object root)
            throws ValidationException {
        if (root == null)
            throw new IllegalArgumentException(
                    "Parameter (Object root) cannot be null.");
        if (flavour == null)
            throw new IllegalArgumentException(
                    "Parameter (PDFAFlavour flavour) cannot be null.");
        Validator validator = new Validator(flavour);
        ValidationResult res = validator.validate(root);
        return res;
    }

}
