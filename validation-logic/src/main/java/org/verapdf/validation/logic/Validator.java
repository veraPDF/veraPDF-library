package org.verapdf.validation.logic;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptableObject;
import org.verapdf.exceptions.validationlogic.MultiplyGlobalVariableNameException;
import org.verapdf.exceptions.validationlogic.NullLinkException;
import org.verapdf.exceptions.validationlogic.NullLinkNameException;
import org.verapdf.exceptions.validationlogic.NullLinkedObjectException;
import org.verapdf.exceptions.validationprofileparser.MissedHashTagException;
import org.verapdf.exceptions.validationprofileparser.WrongSignatureException;
import org.verapdf.model.baselayer.Object;
import org.verapdf.validation.profile.model.ValidationProfile;
import org.verapdf.validation.profile.model.Variable;
import org.verapdf.validation.profile.parser.ValidationProfileParser;
import org.verapdf.validation.report.model.*;
import org.verapdf.validation.report.model.Check.Status;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Validation logic
 *
 * @author Maksim Bezrukov
 */
public class Validator {

    private Context cx;
    private ScriptableObject scope;

    private Deque<Object> objectsStack;
    private Deque<String> objectsContext;
    private Deque<Set<String>> contextSet;
    private Map<String, List<Check>> checkMap;

    private Map<String, Script> ruleScripts = new HashMap<>();
    private Map<String, Script> variableScripts = new HashMap<>();

    private Set<String> idSet;

    private String rootType;

    private ValidationProfile profile;

    /**
     * Creates new Validator with given validation profile
     *
     * @param profile - validation profile model for validator
     */
    private Validator(ValidationProfile profile) {
        this.profile = profile;
    }

    private ValidationInfo validate(Object root) throws NullLinkNameException,
            NullLinkException, NullLinkedObjectException {
        this.cx = Context.enter();
        this.cx.setOptimizationLevel(9);
        this.scope = cx.initStandardObjects();
        this.objectsStack = new ArrayDeque<>();
        this.objectsContext = new ArrayDeque<>();
        this.contextSet = new ArrayDeque<>();
        List<String> warnings = new ArrayList<>();
        this.idSet = new HashSet<>();
        this.checkMap = new HashMap<>();

        for (String id : this.profile.getAllRulesId()) {
            this.checkMap.put(id, new ArrayList<Check>());
        }

        initializeAllVariables();

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

        List<Rule> rules = new ArrayList<>();

        for (Map.Entry<String, List<Check>> id : this.checkMap.entrySet()) {

            rules.add(new Rule(id.getKey(), id.getValue()));
        }

        return new ValidationInfo(new Profile(this.profile.getName(),
                this.profile.getHash()), new Result(
                new Details(rules, warnings)));
    }

    private void initializeAllVariables() {
        for (Variable var : this.profile.getAllVariables()) {
            if (var == null)
                continue;

            java.lang.Object res;
            res = cx.evaluateString(scope, var.getDefaultValue(), null, 0, null);
            if (res instanceof NativeJavaObject) {
                res = ((NativeJavaObject) res).unwrap();
            }

            scope.put(var.getAttrName(), scope, res);
        }
    }

    private boolean checkNext() throws NullLinkException,
            NullLinkedObjectException, NullLinkNameException {

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
            for (Variable var : this.profile
                    .getVariablesForObject(object.getObjectType())) {

                if (var == null)
                    continue;

                java.lang.Object variable = evalVariableResult(var, object);
                scope.put(var.getAttrName(), scope, variable);
            }
        }
    }

    private java.lang.Object evalVariableResult(Variable variable, Object object) {
        Script script;
        if (!variableScripts.containsKey(variable.getAttrName())) {
            String source = getScriptPrefix(object, variable.getValue()) + variable.getValue()
                    + getScriptSuffix();
            script = cx.compileString(source, null, 0, null);
        } else {
            script = variableScripts.get(variable.getAttrName());
        }

        scope.put("obj", scope, object);

        java.lang.Object res = script.exec(cx, scope);

        if (res instanceof NativeJavaObject) {
            res = ((NativeJavaObject) res).unwrap();
        }
        return res;
    }

    private void addAllLinkedObjects(Object checkObject, String checkContext,
                                     Set<String> checkIDContext) throws NullLinkNameException,
            NullLinkException, NullLinkedObjectException {
        for (int j = checkObject.getLinks().size() - 1; j >= 0; --j) {
            String link = checkObject.getLinks().get(j);

            if (link == null) {
                throw new NullLinkNameException(
                        "There is a null link name in an object. Context: "
                                + checkContext);
            }
            List<? extends Object> objects = checkObject.getLinkedObjects(link);
            if (objects == null) {
                throw new NullLinkException(
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
                    throw new NullLinkedObjectException(
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
        if (this.profile.getRoolsForObject(checkObject.getObjectType()) != null) {
            for (org.verapdf.validation.profile.model.Rule rule : this.profile
                    .getRoolsForObject(checkObject.getObjectType())) {
                if (rule != null) {
                    res &= checkObjWithRule(checkObject, checkContext, rule);
                }
            }
        }

        for (String checkType : checkObject.getSuperTypes()) {
            if (this.profile.getRoolsForObject(checkType) != null) {
                for (org.verapdf.validation.profile.model.Rule rule : this.profile
                        .getRoolsForObject(checkType)) {
                    if (rule != null) {
                        res &= checkObjWithRule(checkObject, checkContext,
                                rule);
                    }
                }
            }
        }

        return res;
    }

    private static String getScript(Object obj,
                                    org.verapdf.validation.profile.model.Rule rule) {
        StringBuilder builder = new StringBuilder();

        builder.append(getScriptPrefix(obj, rule.getTest()));
        builder.append("(");
        builder.append(rule.getTest());
        builder.append(")==true");
        builder.append(getScriptSuffix());
        return builder.toString();
    }

    private static String getScriptPrefix(Object obj, String test) {
        StringBuilder builder = new StringBuilder();

        for (String prop : obj.getProperties()) {
            if (test.contains(prop)) {
                builder.append("var ");
                builder.append(prop);
                builder.append(" = obj.get");
                builder.append(prop);
                builder.append("();\n");
            }
        }

        for (String linkName : obj.getLinks()) {
            if (test.contains(linkName + "_size")) {
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

    private static String getScriptSuffix() {
        return ";}\ntest();";
    }

    private boolean checkObjWithRule(Object obj, String context,
                                     org.verapdf.validation.profile.model.Rule rule) {
        scope.put("obj", scope, obj);

        Script scr;
        if (!ruleScripts.containsKey(rule.getAttrID())) {
            scr = cx.compileString(getScript(obj, rule), null, 0, null);
            ruleScripts.put(rule.getAttrID(), scr);
        } else {
            scr = ruleScripts.get(rule.getAttrID());
        }

        Boolean res = (Boolean) scr.exec(cx, scope);

        CheckLocation loc = new CheckLocation(this.rootType, context);
        Check check = res.booleanValue() ? new Check(Status.PASSED, loc, null)
                : createFailCheck(obj, loc, rule);

        this.checkMap.get(rule.getAttrID()).add(check);

        return res.booleanValue();
    }

    private Check createFailCheck(Object obj, CheckLocation loc,
                                  org.verapdf.validation.profile.model.Rule rule) {
        List<String> args = new ArrayList<>();

        if (rule.getRuleError() == null) {
            return new Check(Status.FAILED, loc, new CheckError(null, args));
        }
        String errorMessage = rule.getRuleError().getMessage();

        for (String arg : rule.getRuleError().getArgument()) {
            String argScript = getScriptPrefix(obj, arg) + arg + getScriptSuffix();

            java.lang.Object resArg;

            resArg = cx.evaluateString(scope, argScript, null, 0, null);

            String resStringArg;

            if (resArg instanceof NativeJavaObject) {
                resStringArg = ((NativeJavaObject) resArg).unwrap().toString();
            } else {
                resStringArg = resArg.toString();
            }

            args.add(resStringArg);
        }

        CheckError error = new CheckError(errorMessage, args);

        return new Check(Status.FAILED, loc, error);
    }

    /**
     * Generates validation info for objects with root {@code root} and
     * validation profile path {@code validationProfilePath}
     * <p/>
     * This method needs to parse validation profile (it works slower than those
     * ones, which don't parse profile).
     *
     * @param root                  the root object for validation
     * @param validationProfilePath validation profile's file path
     * @return validation info structure
     * @throws ParserConfigurationException        if a DocumentBuilder cannot be created which satisfies the
     *                                             configuration requested.
     * @throws IOException                         if any IO errors occur.
     * @throws FileNotFoundException               if the profileFile is not an existing file
     * @throws SAXException                        if any parse errors occur.
     * @throws NullLinkNameException               if there is a null link name in some object
     * @throws NullLinkException                   if there is a null link
     * @throws NullLinkedObjectException           if there is a null object in links list
     * @throws MissedHashTagException              if validation profile must be signed, but it has no hash tag
     * @throws XMLStreamException                  if exception occurs in parsing a validation profile with xml
     *                                             stream (in checking signature of the validation profile)
     * @throws WrongSignatureException             if validation profile must be signed, but it has wrong
     *                                             signature
     * @throws UnsupportedEncodingException        if validation profile has not utf8 encoding
     * @throws MultiplyGlobalVariableNameException if there is more than one identical global variable names in
     *                                             the profile model
     */
    public static ValidationInfo validate(Object root,
                                          String validationProfilePath, boolean isSignCheckOn)
            throws IOException, SAXException, ParserConfigurationException,
            NullLinkNameException, NullLinkException,
            NullLinkedObjectException, MissedHashTagException,
            XMLStreamException, WrongSignatureException, MultiplyGlobalVariableNameException {
        if (root == null)
            throw new IllegalArgumentException(
                    "Parameter (Object root) cannot be null.");
        if (validationProfilePath == null)
            throw new IllegalArgumentException(
                    "Parameter (String validationProfilePath) cannot be null.");
        return validate(root, ValidationProfileParser.parseFromFilePath(
                validationProfilePath, isSignCheckOn));
    }

    /**
     * Generates validation info for objects with root {@code root} and
     * validation profile file {@code validationProfilePath}
     * <p/>
     * This method needs to parse validation profile (it works slower than those
     * ones, which don't parse profile).
     *
     * @param root              the root object for validation
     * @param validationProfile validation profile's file
     * @return validation info structure
     * @throws ParserConfigurationException        if a DocumentBuilder cannot be created which satisfies the
     *                                             configuration requested.
     * @throws IOException                         If any IO errors occur.
     * @throws FileNotFoundException               if the profileFile is not an existing file
     * @throws SAXException                        If any parse errors occur.
     * @throws NullLinkNameException               if there is a null link name in some object
     * @throws NullLinkException                   if there is a null link
     * @throws NullLinkedObjectException           if there is a null object in links list
     * @throws MissedHashTagException              if validation profile must be signed, but it has no hash tag
     * @throws XMLStreamException                  if exception occurs in parsing a validation profile with xml
     *                                             stream (in checking signature of the validation profile)
     * @throws WrongSignatureException             if validation profile must be signed, but it has wrong
     *                                             signature
     * @throws UnsupportedEncodingException        if validation profile has not utf8 encoding
     * @throws MultiplyGlobalVariableNameException if there is more than one identical global variable names in
     *                                             the profile model
     */
    public static ValidationInfo validate(Object root, File validationProfile,
                                          boolean isSignCheckOn) throws ParserConfigurationException,
            SAXException, IOException, NullLinkNameException,
            NullLinkException, NullLinkedObjectException,
            MissedHashTagException, XMLStreamException,
            WrongSignatureException, MultiplyGlobalVariableNameException {
        if (root == null)
            throw new IllegalArgumentException(
                    "Parameter (Object root) cannot be null.");
        if (validationProfile == null)
            throw new IllegalArgumentException(
                    "Parameter (ValidationProfile validationProfile) cannot be null.");
        return validate(root, ValidationProfileParser.parseFromFile(
                validationProfile, isSignCheckOn));
    }

    /**
     * Generates validation info for objects with root {@code root} and
     * validation profile structure {@code validationProfile}
     * <p/>
     * This method doesn't need to parse validation profile (it works faster
     * than those ones, which parses profile).
     *
     * @param root              the root object for validation
     * @param validationProfile validation profile's structure
     * @return validation info structure
     * @throws NullLinkNameException     if there is a null link name in some object
     * @throws NullLinkException         if there is a null link
     * @throws NullLinkedObjectException if there is a null object in links list
     */
    public static ValidationInfo validate(Object root,
                                          ValidationProfile validationProfile) throws NullLinkNameException,
            NullLinkException, NullLinkedObjectException {
        if (root == null)
            throw new IllegalArgumentException(
                    "Parameter (Object root) cannot be null.");
        if (validationProfile == null)
            throw new IllegalArgumentException(
                    "Parameter (ValidationProfile validationProfile) cannot be null.");
        Validator validator = new Validator(validationProfile);
        return validator.validate(root);
    }

}
