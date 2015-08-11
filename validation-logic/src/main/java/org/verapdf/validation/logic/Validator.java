package org.verapdf.validation.logic;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.ScriptableObject;
import org.verapdf.exceptions.validationlogic.*;
import org.verapdf.exceptions.validationprofileparser.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.validation.profile.model.ValidationProfile;
import org.verapdf.validation.profile.model.Variable;
import org.verapdf.validation.profile.parser.ValidationProfileParser;
import org.verapdf.validation.report.model.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Validation logic
 *
 * @author Maksim Bezrukov
 */
public class Validator {

    private Stack<Object> objectsStack;
    private Stack<String> objectsContext;
    private Stack<Set<String>> contextSet;
    private Map<String, List<Check>> checkMap;
    private Map<String, java.lang.Object> variablesMap;
    private Set<String> idSet;

    private String rootType;

    private ValidationProfile profile;

    /**
     * Creates new Validator with given validation profile
     *
     * @param profile - validation profile model for validator
     */
    protected Validator(ValidationProfile profile) {
        this.profile = profile;
    }

    private ValidationInfo validate(Object root) throws NullLinkNameException, JavaScriptEvaluatingException, NullLinkException, NullLinkedObjectException, RullWithNullIDException, MultiplyGlobalVariableNameException {
        objectsStack = new Stack<>();
        objectsContext = new Stack<>();
        contextSet = new Stack<>();
        List<String> warnings = new ArrayList<>();
        idSet = new HashSet<>();
        checkMap = new HashMap<>();
        variablesMap = new HashMap<>();

        if (profile == null) {
            return new ValidationInfo(new Profile("", ""), new Result(new Details(new ArrayList<Rule>(), new ArrayList<String>())));
        }
        for (String id : profile.getAllRulesId()) {
            checkMap.put(id, new ArrayList<Check>());
        }

        if (root == null) {
            List<Rule> rules = new ArrayList<>();

            for (Map.Entry<String, List<Check>> id : checkMap.entrySet()) {

                rules.add(new Rule(id.getKey(), id.getValue()));
            }

            return new ValidationInfo(new Profile(profile.getName(), profile.getHash()), new Result(new Details(rules, warnings)));

        }
        initializeAllVariables();

        rootType = root.getType();

        objectsStack.push(root);

        objectsContext.push("root");

        Set<String> rootIDContext = new HashSet<>();

        if (root.getID() != null) {
            rootIDContext.add(root.getID());
            idSet.add(root.getID());
        }

        contextSet.push(rootIDContext);

        while (!objectsStack.isEmpty()) {
            checkNext();
        }

        List<Rule> rules = new ArrayList<>();

        for (Map.Entry<String, List<Check>> id : checkMap.entrySet()) {

            rules.add(new Rule(id.getKey(), id.getValue()));
        }

        return new ValidationInfo(new Profile(profile.getName(), profile.getHash()), new Result(new Details(rules, warnings)));
    }

    private void initializeAllVariables() throws JavaScriptEvaluatingException, MultiplyGlobalVariableNameException {
        if (profile.getAllVariables() != null) {
            for (Variable var : profile.getAllVariables()) {
                if (var != null) {

                    if (variablesMap.containsKey(var.getAttrName())) {
                        throw new MultiplyGlobalVariableNameException("Founded multiply variable with name: " + var.getAttrName() + "\".");
                    }
                    Context cx = Context.enter();
                    ScriptableObject scope = cx.initStandardObjects();

                    java.lang.Object res;
                    try {
                        res = cx.evaluateString(scope, var.getDefaultValue(), null, 0, null);
                    } catch (Exception e) {
                        throw new JavaScriptEvaluatingException("Problem with evaluating default value of the variable: \"" + var.getAttrName() + "\".", e);
                    }
                    if (res instanceof NativeJavaObject) {
                        res = ((NativeJavaObject) res).unwrap();
                    }

                    variablesMap.put(var.getAttrName(), res);

                    Context.exit();
                }
            }
        }
    }

    private boolean checkNext() throws JavaScriptEvaluatingException, NullLinkException, NullLinkedObjectException, NullLinkNameException, RullWithNullIDException {

        Object checkObject = objectsStack.pop();
        String checkContext = objectsContext.pop();
        Set<String> checkIDContext = contextSet.pop();

        boolean res = checkAllRules(checkObject, checkContext);

        updateVariables(checkObject, checkContext);

        addAllLinkedObjects(checkObject, checkContext, checkIDContext);

        return res;
    }

    private void updateVariables(Object object, String context) throws JavaScriptEvaluatingException {
        if (profile.getVariablesForObject(object.getType()) != null) {
            for (Variable var : profile.getVariablesForObject(object.getType())) {
                if (var != null) {

                    Context cx = Context.enter();
                    ScriptableObject scope = cx.initStandardObjects();
                    scope.put("obj", scope, object);
                    for (Map.Entry<String, java.lang.Object> entry : variablesMap.entrySet()) {
                        scope.put(entry.getKey(), scope, entry.getValue());
                    }

                    java.lang.Object res;
                    String valScript = getScriptPrefix(object) + var.getValue() + getScriptSuffix();
                    try {
                        res = cx.evaluateString(scope, valScript, null, 0, null);
                    } catch (Exception e) {
                        throw new JavaScriptEvaluatingException("Problem with evaluating value of the variable: \"" + var.getAttrName() + "\" for object: " + object.getType() + " with context: " + context, e);
                    }

                    if (res instanceof NativeJavaObject) {
                        res = ((NativeJavaObject) res).unwrap();
                    }
                    variablesMap.put(var.getAttrName(), res);

                    Context.exit();
                }
            }
        }
    }

    private void addAllLinkedObjects(Object checkObject, String checkContext, Set<String> checkIDContext) throws NullLinkNameException, NullLinkException, NullLinkedObjectException {
        for (int j = checkObject.getLinks().size() - 1; j >= 0; --j) {
            String link = checkObject.getLinks().get(j);

            if (link != null) {
                List<? extends Object> objects = checkObject.getLinkedObjects(link);

                if (objects != null) {
                    for (int i = 0; i < objects.size(); ++i) {
                        Object obj = objects.get(i);

                        StringBuilder path = new StringBuilder(checkContext);
                        path.append("/");
                        path.append(link);
                        path.append("[");
                        path.append(i);
                        path.append("]");

                        if (obj != null) {

                            if (checkRequired(obj, checkIDContext)) {
                                objectsStack.push(obj);

                                Set<String> newCheckIDContext = new HashSet<>(checkIDContext);

                                if (obj.getID() != null) {
                                    path.append("(");
                                    path.append(obj.getID());
                                    path.append(")");

                                    newCheckIDContext.add(obj.getID());
                                    idSet.add(obj.getID());
                                }

                                objectsContext.push(path.toString());
                                contextSet.push(newCheckIDContext);
                            }
                        } else {
                            throw new NullLinkedObjectException("There is a null link in an object. Context of the link: " + path);
                        }
                    }
                } else {
                    throw new NullLinkException("There is a null link in an object. Context: " + checkContext);
                }
            } else {
                throw new NullLinkNameException("There is a null link name in an object. Context: " + checkContext);
            }
        }
    }

    private boolean checkRequired(Object obj, Set<String> checkIDContext) {

        if (obj.getID() == null) {
            return true;
        } else if (obj.isContextDependent() == null || obj.isContextDependent().booleanValue()) {
            return !checkIDContext.contains(obj.getID());
        } else {
            return !idSet.contains(obj.getID());
        }
    }

    private boolean checkAllRules(Object checkObject, String checkContext) throws JavaScriptEvaluatingException, RullWithNullIDException {
        boolean res = true;
        if (profile.getRoolsForObject(checkObject.getType()) != null) {
            for (org.verapdf.validation.profile.model.Rule rule : profile.getRoolsForObject(checkObject.getType())) {
                if (rule != null) {
                    res &= checkObjWithRule(checkObject, checkContext, rule, getScript(checkObject, rule));
                }
            }
        }

        for (String checkType : checkObject.getSuperTypes()) {
            if (profile.getRoolsForObject(checkType) != null) {
                for (org.verapdf.validation.profile.model.Rule rule : profile.getRoolsForObject(checkType)) {
                    if (rule != null) {
                        res &= checkObjWithRule(checkObject, checkContext, rule, getScript(checkObject, rule));
                    }
                }
            }
        }

        return res;
    }

    private static String getScript(Object obj, org.verapdf.validation.profile.model.Rule rule) {
        StringBuilder builder = new StringBuilder();

        builder.append(getScriptPrefix(obj));
        builder.append("(");
        builder.append(rule.getTest());
        builder.append(")==true");
        builder.append(getScriptSuffix());
        return builder.toString();
    }

    private static String getScriptPrefix(Object obj) {
        StringBuilder builder = new StringBuilder();

        for (String prop : obj.getProperties()) {
            builder.append("var " + prop + " = obj.get" + prop + "();\n");
        }

        for (String linkName : obj.getLinks()) {
            List<? extends Object> linkedObject = obj.getLinkedObjects(linkName);
            builder.append("var " + linkName + "_size = " + linkedObject.size() + ";\n");
        }

        builder.append("function test(){return ");

        return builder.toString();
    }

    private static String getScriptSuffix() {
        return ";}\ntest();";
    }


    private boolean checkObjWithRule(Object obj, String context, org.verapdf.validation.profile.model.Rule rule, String script) throws JavaScriptEvaluatingException, RullWithNullIDException {
        Context cx = Context.enter();
        ScriptableObject scope = cx.initStandardObjects();

        scope.put("obj", scope, obj);
        for (Map.Entry<String, java.lang.Object> entry : variablesMap.entrySet()) {
            scope.put(entry.getKey(), scope, entry.getValue());
        }

        Boolean res;

        try {
            res = (Boolean) cx.evaluateString(scope, script, null, 0, null);
        } catch (Exception e) {
            throw new JavaScriptEvaluatingException("Problem with evaluating test: \"" + rule.getTest() + "\" for object with context: " + context, e);
        }

        CheckLocation loc = new CheckLocation(rootType, context);

        Check check;

        if (res.booleanValue()) {
            check = new Check("passed", loc, null, false);
        } else {
            check = createFailCkeck(obj, loc, rule, cx, scope, context);
        }

        if (rule.getAttrID() == null) {
            throw new RullWithNullIDException("There is a rule with null id in the profile. Profile name: " + profile.getName());
        }
        checkMap.get(rule.getAttrID()).add(check);

        Context.exit();

        return res.booleanValue();
    }

    private static Check createFailCkeck(Object obj, CheckLocation loc, org.verapdf.validation.profile.model.Rule rule, Context cx, ScriptableObject scope, String context) throws JavaScriptEvaluatingException {
        List<String> args = new ArrayList<>();

        String errorMessage;

        if (rule.getRuleError() != null) {
            errorMessage = rule.getRuleError().getMessage();

            if (rule.getRuleError().getArgument() != null) {
                for (String arg : rule.getRuleError().getArgument()) {
                    if (arg != null) {
                        String argScript = getScriptPrefix(obj) + arg + getScriptSuffix();

                        java.lang.Object resArg;

                        try {
                            resArg = cx.evaluateString(scope, argScript, null, 0, null);
                        } catch (Exception e) {
                            throw new JavaScriptEvaluatingException("Problem with evaluating argument: " + arg + "for object with context: " + context, e);
                        }

                        String resStringArg;

                        if (resArg instanceof NativeJavaObject) {
                            resStringArg = ((NativeJavaObject) resArg).unwrap().toString();
                        } else {
                            resStringArg = resArg.toString();
                        }

                        args.add(resStringArg);
                    }
                }
            }
        } else {
            errorMessage = null;
        }

        CheckError error = new CheckError(errorMessage, args);

        return new Check("failed", loc, error, rule.isHasError());
    }

    /**
     * Generates validation info for objects with root {@code root} and validation profile path {@code validationProfilePath}
     * <p/>
     * This method needs to parse validation profile (it works slower than those ones, which don't parse profile).
     *
     * @param root                  --- the root object for validation
     * @param validationProfilePath --- validation profile's file path
     * @return validation info structure
     * @throws ParserConfigurationException        - if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws IOException                         - if any IO errors occur.
     * @throws SAXException                        - if any parse errors occur.
     * @throws IncorrectImportPathException        - if validation profile contains incorrect import path
     * @throws NullLinkNameException               - if there is a null link name in some object
     * @throws JavaScriptEvaluatingException       - if there is some problems with evaluating javascript
     * @throws NullLinkException                   - if there is a null link
     * @throws NullLinkedObjectException           -  if there is a null object in links list
     * @throws RullWithNullIDException             - if there is a rule with null id in the profile
     * @throws MissedHashTagException              - if validation profile must be signed, but it has no hash tag
     * @throws XMLStreamException                  - if exception occurs in parsing a validation profile with xml stream (in checking signature of the validation profile)
     * @throws WrongSignatureException             - if validation profile must be signed, but it has wrong signature
     * @throws WrongProfileEncodingException       - if validation profile has not utf8 encoding
     * @throws MultiplyGlobalVariableNameException - if there is more than one identical global variable names in the profile model
     */
    public static ValidationInfo validate(Object root, String validationProfilePath, boolean isSignCheckOn) throws IOException, SAXException, ParserConfigurationException, IncorrectImportPathException, NullLinkNameException, JavaScriptEvaluatingException, NullLinkException, NullLinkedObjectException, RullWithNullIDException, MissedHashTagException, XMLStreamException, WrongSignatureException, WrongProfileEncodingException, MultiplyGlobalVariableNameException {
        return validate(root, ValidationProfileParser.parseValidationProfile(validationProfilePath, isSignCheckOn));
    }

    /**
     * Generates validation info for objects with root {@code root} and validation profile file {@code validationProfilePath}
     * <p/>
     * This method needs to parse validation profile (it works slower than those ones, which don't parse profile).
     *
     * @param root              --- the root object for validation
     * @param validationProfile --- validation profile's file
     * @return validation info structure
     * @throws ParserConfigurationException        - if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws IOException                         - If any IO errors occur.
     * @throws SAXException                        - If any parse errors occur.
     * @throws IncorrectImportPathException        - if validation profile contains incorrect import path
     * @throws NullLinkNameException               - if there is a null link name in some object
     * @throws JavaScriptEvaluatingException       - if there is some problems with evaluating javascript
     * @throws NullLinkException                   - if there is a null link
     * @throws NullLinkedObjectException           -  if there is a null object in links list
     * @throws RullWithNullIDException             - if there is a rule with null id in the profile
     * @throws MissedHashTagException              - if validation profile must be signed, but it has no hash tag
     * @throws XMLStreamException                  - if exception occurs in parsing a validation profile with xml stream (in checking signature of the validation profile)
     * @throws WrongSignatureException             - if validation profile must be signed, but it has wrong signature
     * @throws WrongProfileEncodingException       - if validation profile has not utf8 encoding
     * @throws MultiplyGlobalVariableNameException - if there is more than one identical global variable names in the profile model
     */
    public static ValidationInfo validate(Object root, File validationProfile, boolean isSignCheckOn) throws ParserConfigurationException, SAXException, IOException, IncorrectImportPathException, NullLinkNameException, JavaScriptEvaluatingException, NullLinkException, NullLinkedObjectException, RullWithNullIDException, MissedHashTagException, XMLStreamException, WrongSignatureException, WrongProfileEncodingException, MultiplyGlobalVariableNameException {
        return validate(root, ValidationProfileParser.parseValidationProfile(validationProfile, isSignCheckOn));
    }

    /**
     * Generates validation info for objects with root {@code root} and validation profile structure  {@code validationProfile}
     * <p/>
     * This method doesn't need to parse validation profile (it works faster than those ones, which parses profile).
     *
     * @param root              --- the root object for validation
     * @param validationProfile --- validation profile's structure
     * @return validation info structure
     * @throws NullLinkNameException               - if there is a null link name in some object
     * @throws JavaScriptEvaluatingException       - if there is some problems with evaluating javascript
     * @throws NullLinkException                   - if there is a null link
     * @throws NullLinkedObjectException           -  if there is a null object in links list
     * @throws RullWithNullIDException             - if there is a rule with null id in the profile
     * @throws MultiplyGlobalVariableNameException - if there is more than one identical global variable names in the profile model
     */
    public static ValidationInfo validate(Object root, ValidationProfile validationProfile) throws NullLinkNameException, JavaScriptEvaluatingException, NullLinkException, NullLinkedObjectException, RullWithNullIDException, MultiplyGlobalVariableNameException {
        Validator validator = new Validator(validationProfile);
        return validator.validate(root);
    }

}
