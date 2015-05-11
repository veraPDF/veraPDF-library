package org.verapdf.validation.report.model;

import java.util.List;

/**
 * Structure of the location of a check.
 * Created by bezrukov on 5/4/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 */
public class CheckLocation {
    private String attr_level;
    private List<String> context;

    public CheckLocation(String attr_level, List<String> context) {
        this.attr_level = attr_level;
        this.context = context;
    }

    /**
     * @return the level of the check location
     */
    public String getAttr_level() {
        return attr_level;
    }

    /**
     * @return list of edges' names which used for come to the checked object
     */
    public List<String> getContext() {
        return context;
    }
}
