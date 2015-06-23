package org.verapdf.validation.profile.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Structure of the valid validation profile.
 * Created by bezrukov on 4/24/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class ValidationProfile {
    private String attrModel;

    private String name;
    private String description;
    private String creator;
    private String created;
    private String hash;

    private Map<String, List<Rule>> rules;
    private Map<String, List<Variable>> variables;

    /**
     * Creates new Validation profile model with given description.
     * @param attrModel - model of the validation profile
     * @param name - name of the validation profile
     * @param description - description of the validation profile
     * @param creator - creator (author) of the validation profile
     * @param created - date of creation of the validation profile
     * @param hash - hash code of the validation profile
     * @param rules - map of rules of the validation profile (key is the name of the object, value is the list of rules)
     * @param variables - map of variables of the validation profile (key is the name of the object, value is the list of variables)
     */
    public ValidationProfile(String attrModel, String name, String description, String creator, String created, String hash, Map<String, List<Rule>> rules, Map<String, List<Variable>> variables) {
        this.attrModel = attrModel;
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.created = created;
        this.hash = hash;
        this.rules = rules;
        this.variables = variables;
    }

    /**
     * @return Text provided by attribute "model".
     */
    public String getAttrModel() {
        return attrModel;
    }

    /**
     * @return Text in tag "name".
     */
    public String getName() {
        return name;
    }

    /**
     * @return Text in tag "description".
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return Text in tag "creator".
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @return Text in tag "created".
     */
    public String getCreated() {
        return created;
    }

    /**
     * @return Text in tag "hash".
     */
    public String getHash() {
        return hash;
    }

    /**
     * Get all rools for the given object.
     * @param objName --- name of the object
     * @return List of rules for the given object.
     */
    public List<Rule> getRoolsForObject(String objName){
        return rules != null ? rules.get(objName) : new ArrayList<Rule>();
    }

    /**
     * Get rule by it's id.
     * @param id --- rule id
     * @return rule by it's id or null, if there is no rule with such id.
     */
    public Rule getRuleById(String id){
        if (id == null) {
            return null;
        } else if (rules != null) {
            for (List<Rule> ruleList : rules.values()) {
                if (ruleList != null) {
                    for (Rule rule : ruleList) {
                        if (rule != null && id.equals(rule.getAttrID())) {
                            return rule;
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * @return list of all id of rules
     */
    public List<String> getAllRulesId(){
        List<String> result = new ArrayList<>();

        if (rules != null) {
            for (List<Rule> ruleList : rules.values()) {
                if (ruleList != null) {
                    for (Rule rule : ruleList) {
                        if (rule != null) {
                            result.add(rule.getAttrID());
                        }
                    }
                }
            }
        }

        return result;
    }

    /**
     * Get all variables for the given object.
     * @param objName --- name of the object
     * @return List of variables for the given object.
     */
    public List<Variable> getVariablesForObject(String objName){
        return variables != null ? variables.get(objName) : new ArrayList<Variable>();
    }

    /**
     * @return list of all variables
     */
    public List<Variable> getAllVariables(){
        List<Variable> result = new ArrayList<>();

        if (variables != null) {
            for (List<Variable> variablesList : variables.values()) {
                if (variablesList != null) {
                    result.addAll(variablesList);
                }
            }
        }

        return result;
    }
}
