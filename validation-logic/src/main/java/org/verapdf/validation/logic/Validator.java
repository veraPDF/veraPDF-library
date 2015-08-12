package org.verapdf.validation.logic;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.ScriptableObject;
import org.verapdf.exceptions.validationlogic.MultiplyGlobalVariableNameException;
import org.verapdf.exceptions.validationlogic.NullLinkException;
import org.verapdf.exceptions.validationlogic.NullLinkNameException;
import org.verapdf.exceptions.validationlogic.NullLinkedObjectException;
import org.verapdf.exceptions.validationprofileparser.IncorrectImportPathException;
import org.verapdf.exceptions.validationprofileparser.MissedHashTagException;
import org.verapdf.exceptions.validationprofileparser.WrongSignatureException;
import org.verapdf.model.baselayer.Object;
import org.verapdf.validation.profile.model.ValidationProfile;
import org.verapdf.validation.profile.model.Variable;
import org.verapdf.validation.profile.parser.ValidationProfileParser;
import org.verapdf.validation.report.model.Check;
import org.verapdf.validation.report.model.Check.Status;
import org.verapdf.validation.report.model.CheckError;
import org.verapdf.validation.report.model.CheckLocation;
import org.verapdf.validation.report.model.Details;
import org.verapdf.validation.report.model.Profile;
import org.verapdf.validation.report.model.Result;
import org.verapdf.validation.report.model.Rule;
import org.verapdf.validation.report.model.ValidationInfo;
import org.xml.sax.SAXException;

/**
 * Validation logic
 *
 * @author Maksim Bezrukov
 */
public class Validator {

    private Deque<Object> objectsStack;
    private Deque<String> objectsContext;
    private Deque<Set<String>> contextSet;
    private Map<String, List<Check>> checkMap;
    private Map<String, java.lang.Object> variablesMap;
    private Set<String> idSet;

    private String rootType;

    private ValidationProfile profile;

    /**
     * Creates new Validator with given validation profile
     *
     * @param profile
     *            - validation profile model for validator
     */
    private Validator(ValidationProfile profile) {
        this.profile = profile;
    }

    private ValidationInfo validate(Object root) throws NullLinkNameException,
            NullLinkException, NullLinkedObjectException,
            MultiplyGlobalVariableNameException {
        this.objectsStack = new ArrayDeque<>();
        this.objectsContext = new ArrayDeque<>();
        this.contextSet = new ArrayDeque<>();
        List<String> warnings = new ArrayList<>();
        this.idSet = new HashSet<>();
        this.checkMap = new HashMap<>();
        this.variablesMap = new HashMap<>();

        for (String id : this.profile.getAllRulesId()) {
            this.checkMap.put(id, new ArrayList<Check>());
        }

        initializeAllVariables();

        this.rootType = root.getType();
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

        List<Rule> rules = new ArrayList<>();

        for (Map.Entry<String, List<Check>> id : this.checkMap.entrySet()) {

            rules.add(new Rule(id.getKey(), id.getValue()));
        }

        return new ValidationInfo(new Profile(this.profile.getName(),
                this.profile.getHash()), new Result(
                new Details(rules, warnings)));
    }

    private void initializeAllVariables()
            throws MultiplyGlobalVariableNameException {
        for (Variable var : this.profile.getAllVariables()) {
            if (var == null)
                continue;
            if (this.variablesMap.containsKey(var.getAttrName())) {
                throw new MultiplyGlobalVariableNameException(
                        "Founded multiply variable with name: "
                                + var.getAttrName() + "\".");
            }
            Context cx = Context.enter();
            ScriptableObject scope = cx.initStandardObjects();

            java.lang.Object res;
            res = cx.evaluateString(scope, var.getDefaultValue(), null, 0, null);
            if (res instanceof NativeJavaObject) {
                res = ((NativeJavaObject) res).unwrap();
            }

            this.variablesMap.put(var.getAttrName(), res);

            Context.exit();
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
        for (Variable var : this.profile
                .getVariablesForObject(object.getType())) {

            if (var == null)
                continue;

            this.variablesMap.put(var.getAttrName(),
                    evalVariableResult(var, object));

        }
    }

    private java.lang.Object evalVariableResult(Variable variable, Object object) {
        Context cx = Context.enter();
        ScriptableObject scope = cx.initStandardObjects();
        String source = variable.getDefaultValue();

        // If object's NOT null it's an update so sort the scope and source
        if (object != null) {
            scope.put("obj", scope, object);
            for (Map.Entry<String, java.lang.Object> entry : this.variablesMap
                    .entrySet()) {
                scope.put(entry.getKey(), scope, entry.getValue());
            }
            source = getScriptPrefix(object) + variable.getValue()
                    + getScriptSuffix();
        }

        java.lang.Object res;
        res = cx.evaluateString(scope, source, null, 0, null);
        Context.exit();

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
        if (this.profile.getRoolsForObject(checkObject.getType()) != null) {
            for (org.verapdf.validation.profile.model.Rule rule : this.profile
                    .getRoolsForObject(checkObject.getType())) {
                if (rule != null) {
                    res &= checkObjWithRule(checkObject, checkContext, rule,
                            getScript(checkObject, rule));
                }
            }
        }

        for (String checkType : checkObject.getSuperTypes()) {
            if (this.profile.getRoolsForObject(checkType) != null) {
                for (org.verapdf.validation.profile.model.Rule rule : this.profile
                        .getRoolsForObject(checkType)) {
                    if (rule != null) {
                        res &= checkObjWithRule(checkObject, checkContext,
                                rule, getScript(checkObject, rule));
                    }
                }
            }
        }

        return res;
    }

    private static String getScript(Object obj,
            org.verapdf.validation.profile.model.Rule rule) {
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
            List<? extends Object> linkedObject = obj
                    .getLinkedObjects(linkName);
            builder.append("var " + linkName + "_size = " + linkedObject.size()
                    + ";\n");
        }

        builder.append("function test(){return ");

        return builder.toString();
    }

    private static String getScriptSuffix() {
        return ";}\ntest();";
    }

    private boolean checkObjWithRule(Object obj, String context,
            org.verapdf.validation.profile.model.Rule rule, String script) {
        Context cx = Context.enter();
        ScriptableObject scope = cx.initStandardObjects();

        scope.put("obj", scope, obj);
        for (Map.Entry<String, java.lang.Object> entry : this.variablesMap
                .entrySet()) {
            scope.put(entry.getKey(), scope, entry.getValue());
        }

        Boolean res = (Boolean) cx.evaluateString(scope, script, null, 0, null);

        CheckLocation loc = new CheckLocation(this.rootType, context);
        Check check = res.booleanValue() ? new Check(Status.PASSED, loc, null)
                : createFailCkeck(obj, loc, rule, cx, scope);

        Context.exit();

        this.checkMap.get(rule.getAttrID()).add(check);

        return res.booleanValue();
    }

    private static Check createFailCkeck(Object obj, CheckLocation loc,
            org.verapdf.validation.profile.model.Rule rule, Context cx,
            ScriptableObject scope) {
        List<String> args = new ArrayList<>();

        if (rule.getRuleError() == null) {
            return new Check(Status.FAILED, loc, new CheckError(null, args));
        }
        String errorMessage = rule.getRuleError().getMessage();

        for (String arg : rule.getRuleError().getArgument()) {
            String argScript = getScriptPrefix(obj) + arg + getScriptSuffix();

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
     * @throws SAXException
     *             if any parse errors occur.
     * @throws IncorrectImportPathException
     *             if validation profile contains incorrect import path
     * @throws NullLinkNameException
     *             if there is a null link name in some object
     * @throws NullLinkException
     *             if there is a null link
     * @throws NullLinkedObjectException
     *             if there is a null object in links list
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
     * @throws MultiplyGlobalVariableNameException
     *             if there is more than one identical global variable names in
     *             the profile model
     */
    public static ValidationInfo validate(Object root,
            String validationProfilePath, boolean isSignCheckOn)
            throws IOException, SAXException, ParserConfigurationException,
            IncorrectImportPathException, NullLinkNameException,
            NullLinkException, NullLinkedObjectException,
            MissedHashTagException, XMLStreamException,
            WrongSignatureException, MultiplyGlobalVariableNameException {
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
     * @throws SAXException
     *             If any parse errors occur.
     * @throws IncorrectImportPathException
     *             if validation profile contains incorrect import path
     * @throws NullLinkNameException
     *             if there is a null link name in some object
     * @throws NullLinkException
     *             if there is a null link
     * @throws NullLinkedObjectException
     *             if there is a null object in links list
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
     * @throws MultiplyGlobalVariableNameException
     *             if there is more than one identical global variable names in
     *             the profile model
     */
    public static ValidationInfo validate(Object root, File validationProfile,
            boolean isSignCheckOn) throws ParserConfigurationException,
            SAXException, IOException, IncorrectImportPathException,
            NullLinkNameException, NullLinkException,
            NullLinkedObjectException, MissedHashTagException,
            XMLStreamException, WrongSignatureException,
            MultiplyGlobalVariableNameException {
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
     * @param root
     *            the root object for validation
     * @param validationProfile
     *            validation profile's structure
     * @return validation info structure
     * @throws NullLinkNameException
     *             if there is a null link name in some object
     * @throws NullLinkException
     *             if there is a null link
     * @throws NullLinkedObjectException
     *             if there is a null object in links list
     * @throws MultiplyGlobalVariableNameException
     *             if there is more than one identical global variable names in
     *             the profile model
     */
    public static ValidationInfo validate(Object root,
            ValidationProfile validationProfile) throws NullLinkNameException,
            NullLinkException, NullLinkedObjectException,
            MultiplyGlobalVariableNameException {
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
