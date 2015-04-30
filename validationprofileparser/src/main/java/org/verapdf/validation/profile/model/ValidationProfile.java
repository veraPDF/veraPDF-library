package org.verapdf.validation.profile.model;

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
    private String attr_Model;

    private String name;
    private String description;
    private String creator;
    private String created;
    private String hash;

    private Map<String, List<Rule>> rules;

    public ValidationProfile(String attr_Model, String name, String description, String creator, String created, String hash, Map<String, List<Rule>> rules) {
        this.attr_Model = attr_Model;
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.created = created;
        this.hash = hash;
        this.rules = rules;
    }

    /**
     * @return Text provided by attribute "model".
     */
    public String getAttr_Model() {
        return attr_Model;
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
        return rules.get(objName);
    }

    /**
     * Get rule by it's id.
     * @param id --- rule id
     * @return rule by it's id or null, if there is no rule with such id.
     */
    public Rule getRuleById(String id){
        for (List<Rule> ruleList : rules.values()){
            for(Rule rule : ruleList){
                if (rule.getAttr_id().equals(id)){
                    return rule;
                }
            }
        }
        return null;
    }
}
