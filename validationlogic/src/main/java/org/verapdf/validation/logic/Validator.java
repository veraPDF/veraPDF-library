package org.verapdf.validation.logic;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.verapdf.model.baselayer.Object;
import org.verapdf.validation.profile.model.ValidationProfile;
import org.verapdf.validation.profile.parser.ValidationProfileParser;
import org.verapdf.validation.report.model.*;
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
    private Map<String, List<Check>> checkMap;
    private List<String> warnings;

    private ValidationProfile profile;

    protected Validator(ValidationProfile profile) {
        this.profile = profile;
    }

    private ValidationInfo validate(Object root){
        objectsQueue = new LinkedList<>();
        objectsContext = new LinkedList<>();
        checkMap = new HashMap<>();
        warnings = new ArrayList<>();

        objectsQueue.add(root);

        List<String> rootContext = new ArrayList<>();
        rootContext.add(root.get_id());

        objectsContext.add(rootContext);

        while(!objectsQueue.isEmpty()){
            checkNext();
        }

        List<Rule> rules = new ArrayList<>();

        for(String id : checkMap.keySet()){
            rules.add(new Rule(id, checkMap.get(id)));
        }

        return new ValidationInfo(new Profile(profile.getName(), profile.getHash()), new Result(new Details(rules,warnings)));
    }

    private void checkNext(){
        // This method should check next object in objectQueue (with deleting it from the queue and it's context from objectsContext,
        // and added in them object's links) and write the result into checkMap or warnings.
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
