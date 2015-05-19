package org.verapdf.validation.logic;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.ScriptableObject;
import org.verapdf.model.baselayer.Object;
import org.verapdf.validation.profile.model.*;
import org.verapdf.validation.profile.parser.ValidationProfileParser;
import org.verapdf.validation.report.model.*;
import org.verapdf.validation.report.model.Rule;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Validation logic
 * Created by bezrukov on 5/4/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class Validator {

    private Queue<Object> objectsQueue;
    private Queue<String> objectsContext;
    private Queue<Set<String>> contextSet;
    private Map<String, List<Check>> checkMap;
    private List<String> warnings;
    private Set<String> idSet;

    private String rootType;

    private ValidationProfile profile;

    /**
     * Creates new Validator with given validation profile
     * @param profile - validation profile model for validator
     */
    protected Validator(ValidationProfile profile) {
        this.profile = profile;
    }

    private ValidationInfo validate(Object root){
        objectsQueue = new LinkedList<>();
        objectsContext = new LinkedList<>();
        contextSet = new LinkedList<>();
        checkMap = new HashMap<>();
        warnings = new ArrayList<>();
        idSet = new HashSet<>();

        rootType = root.getType();

        objectsQueue.add(root);

        objectsContext.add("root");

        Set<String> rootIDContext = new HashSet<>();
        if (root.getID() != null) {
            rootIDContext.add(root.getID());
        }

        contextSet.add(rootIDContext);

        if (root.getID() != null) {
            idSet.add(root.getID());
        }

        while(!objectsQueue.isEmpty()){
            checkNext();
        }

        List<Rule> rules = new ArrayList<>();

        for(Map.Entry<String, List<Check>> id : checkMap.entrySet()){

            rules.add(new Rule(id.getKey(), id.getValue()));
        }

        return new ValidationInfo(new Profile(profile.getName(), profile.getHash()), new Result(new Details(rules,warnings)));
    }

    private boolean checkNext(){
        boolean res;
        Object checkObject = objectsQueue.poll();
        String checkContext = objectsContext.poll();
        Set<String> checkIDContext = contextSet.poll();

        res = checkAllRules(checkObject, checkContext);

        addAllLinkedObjects(checkObject, checkContext, checkIDContext);

        return res;
    }

    private void addAllLinkedObjects(Object checkObject, String checkContext, Set<String> checkIDContext){
        for(String link : checkObject.getLinks()){
            List<? extends Object> objects = checkObject.getLinkedObjects(link);
            for(int i = 0; i < objects.size(); ++i){
                Object obj = objects.get(i);

                if(checkRequired(obj, checkIDContext)){

                    objectsQueue.add(obj);
                    objectsContext.add(checkContext + "/" + link + "[" + i + "]");
                    Set<String> newCheckIDContext = new HashSet<>(checkIDContext);
                    if (obj.getID() != null) {
                        newCheckIDContext.add(obj.getID());
                        idSet.add(obj.getID());
                    }
                    contextSet.add(newCheckIDContext);
                }
            }
        }
    }

    private boolean checkRequired(Object obj, Set<String> checkIDContext){

        if (obj.getID() == null){
            return true;
        }
        else if (obj.isContextDependent() == null || obj.isContextDependent()){
            if (!checkIDContext.contains(obj.getID())){
                return true;
            }
            else {
                return false;
            }
        }
        else if (!idSet.contains(obj.getID())){
            return true;
        }
        else {
            return false;
        }
    }

    private boolean checkAllRules(Object checkObject, String checkContext){
        boolean res = true;
        if (profile.getRoolsForObject(checkObject.getType()) != null) {
            for (org.verapdf.validation.profile.model.Rule rule : profile.getRoolsForObject(checkObject.getType())) {
                res &= checkObjWithRule(checkObject, checkContext, rule, getScript(checkObject, rule));
            }
        }

        for(String checkType : checkObject.getSuperTypes()){
            if (profile.getRoolsForObject(checkType) != null) {
                for (org.verapdf.validation.profile.model.Rule rule : profile.getRoolsForObject(checkType)) {
                    res &= checkObjWithRule(checkObject, checkContext, rule, getScript(checkObject, rule));
                }
            }
        }

        return res;
    }

    private String getScript(Object obj, org.verapdf.validation.profile.model.Rule rule){
        StringBuffer buffer = new StringBuffer();
        for (String prop : obj.getProperties()){
            buffer.append("var " + prop + " = obj.get" + prop + "();\n");
        }

        buffer.append("function test(){return ");
        buffer.append(rule.getTest());
        buffer.append(";}\ntest();");
        return buffer.toString();
    }


    private boolean checkObjWithRule(Object obj, String context, org.verapdf.validation.profile.model.Rule rule, String script){
        Context cx = Context.enter();
        ScriptableObject scope = cx.initStandardObjects();

        scope.put("obj", scope, obj);

        Boolean res = (Boolean) cx.evaluateString(scope, script, null, 0, null);

        CheckLocation loc = new CheckLocation(rootType, context);

        Check check;

        if(res) {
            check = new Check("passed", loc, null, false);
        }
        else{
            List<String> args = new ArrayList<>();

            for(String arg : rule.getRuleError().getArgument()){
                NativeJavaObject resArg = (NativeJavaObject) cx.evaluateString(scope, "obj.get"+arg+"()", null, 0, null);
                args.add(resArg.unwrap().toString());
            }

            CheckError error = new CheckError(rule.getRuleError().getMessage(), args);

            check = new Check("failed", loc, error, rule.isHasError());
        }

        if(checkMap.get(rule.getAttrID()) == null){
            checkMap.put(rule.getAttrID(), new ArrayList<Check>());
        }

        checkMap.get(rule.getAttrID()).add(check);

        Context.exit();

        return res;
    }

    /**
     * Generates validation info for objects with root {@code root} and validation profile path {@code validationProfilePath}
     *
     * This method needs to parse validation profile (it works slower than those ones, which don't parse profile).
     *
     * @param root --- the root object for validation
     * @param validationProfilePath --- validation profile's file path
     * @return validation info structure
     * @throws ParserConfigurationException - if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws IOException - If any IO errors occur.
     * @throws SAXException - If any parse errors occur.
     */
    public static ValidationInfo validate(Object root, String validationProfilePath) throws IOException, SAXException, ParserConfigurationException {
        return validate(root, ValidationProfileParser.parseValidationProfile(validationProfilePath));
    }

    /**
     * Generates validation info for objects with root {@code root} and validation profile file {@code validationProfilePath}
     *
     * This method needs to parse validation profile (it works slower than those ones, which don't parse profile).
     *
     * @param root --- the root object for validation
     * @param validationProfile --- validation profile's file
     * @return validation info structure
     * @throws ParserConfigurationException - if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws IOException - If any IO errors occur.
     * @throws SAXException - If any parse errors occur.
     */
    public static ValidationInfo validate(Object root, File validationProfile) throws ParserConfigurationException, SAXException, IOException {
        return validate(root, ValidationProfileParser.parseValidationProfile(validationProfile));
    }

    /**
     * Generates validation info for objects with root {@code root} and validation profile structure  {@code validationProfile}
     *
     * This method doesn't need to parse validation profile (it works faster than those ones, which parses profile).
     *
     * @param root --- the root object for validation
     * @param validationProfile --- validation profile's structure
     * @return validation info structure
     * @throws ParserConfigurationException - if a DocumentBuilder cannot be created which satisfies the configuration requested.
     * @throws IOException - If any IO errors occur.
     * @throws SAXException - If any parse errors occur.
     */
    public static ValidationInfo validate(Object root, ValidationProfile validationProfile){
        Validator validator = new Validator(validationProfile);
        return validator.validate(root);
    }

}
