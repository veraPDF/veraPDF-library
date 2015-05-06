package org.verapdf.validation.logic;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.mozilla.javascript.Context;
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
    private Queue<List<String>> objectsContext;
    private Set<String> contextSet;
    private Map<String, List<Check>> checkMap;
    private List<String> warnings;

    private String rootType;

    private ValidationProfile profile;

    protected Validator(ValidationProfile profile) {
        this.profile = profile;
    }

    private ValidationInfo validate(Object root){
        objectsQueue = new LinkedList<>();
        objectsContext = new LinkedList<>();
        contextSet = new HashSet<>();
        checkMap = new HashMap<>();
        warnings = new ArrayList<>();

        rootType = root.get_type();

        objectsQueue.add(root);

        List<String> rootContext = new ArrayList<>();
        rootContext.add(root.get_id());

        objectsContext.add(rootContext);

        contextSet.add(root.get_id());

        while(!objectsQueue.isEmpty()){
            checkNext();
        }

        List<Rule> rules = new ArrayList<>();

        for(String id : checkMap.keySet()){
            rules.add(new Rule(id, checkMap.get(id)));
        }

        return new ValidationInfo(new Profile(profile.getName(), profile.getHash()), new Result(new Details(rules,warnings)));
    }

    private boolean checkNext(){
        boolean res = true;
        Object checkObject = objectsQueue.poll();
        List<String> checkContext = objectsContext.poll();

        for(org.verapdf.validation.profile.model.Rule rule : profile.getRoolsForObject(checkObject.get_type())){
            res &= checkObjWithRule(checkObject, checkContext, rule, getScript(checkObject, rule));
        }

        for(String checkType : checkObject.getSuperTypes()){
            for(org.verapdf.validation.profile.model.Rule rule : profile.getRoolsForObject(checkType)){
                res &= checkObjWithRule(checkObject, checkContext, rule, getScript(checkObject, rule));
            }
        }

        for(String link : checkObject.getLinks()){
            for(Object obj : checkObject.getLinkedObjects(link)){
                if(!contextSet.contains(obj.get_id())){
                    objectsQueue.add(obj);
                    List<String> objContext = new ArrayList<>(checkContext);
                    objContext.add(obj.get_id());
                    objectsContext.add(objContext);
                    contextSet.add(obj.get_id());
                }
            }
        }

        return res;
    }

    private String getScript(Object obj, org.verapdf.validation.profile.model.Rule rule){
        StringBuffer buffer = new StringBuffer();
        for (String prop : obj.getProperties()){
            buffer.append("var " + prop + " = obj." + prop + ";\n");
        }

        buffer.append("function test(){return ");
        buffer.append(rule.getTest());
        buffer.append(";}\ntest();");
        return buffer.toString();
    }


    private boolean checkObjWithRule(Object obj, List<String> context, org.verapdf.validation.profile.model.Rule rule, String script){
        Context cx = Context.enter();
        ScriptableObject scope = cx.initStandardObjects();

        scope.put("obj", scope, obj);

        Boolean res = (Boolean) cx.evaluateString(scope, script, null, 0, null);

        Context.exit();

        CheckLocation loc = new CheckLocation(rootType, context);

        Check check;

        if(res) {
            check = new Check("passed", loc, null, false);
        }
        else{
            List<String> args = new ArrayList<>();

            for(String arg : rule.getRuleError().getArgument()){
                args.add(cx.evaluateString(scope, "obj."+arg, null, 0, null).toString());
            }

            CheckError error = new CheckError(rule.getRuleError().getMessage(), args);

            check = new Check("passed", loc, error, rule.isHasError());
        }

        if(checkMap.get(rule.getAttr_id()) == null){
            checkMap.put(rule.getAttr_id(), new ArrayList<Check>());
        }

        checkMap.get(rule.getAttr_id()).add(check);

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
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
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
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
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
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static ValidationInfo validate(Object root, ValidationProfile validationProfile){
        Validator validator = new Validator(validationProfile);
        return validator.validate(root);
    }

}
