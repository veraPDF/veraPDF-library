package org.verapdf.validation.report.model;

/**
 * Structure of the location of a check.
 *
 * @author Maksim Bezrukov
 */
public class CheckLocation {
    private final String attrLevel;
    private final String context;

    /**
     * Creates check location model
     *
     * @param attrLevel - level of the check
     * @param context   - context of the check
     */
    public CheckLocation(final String attrLevel, final String context) {
        this.attrLevel = attrLevel;
        this.context = context;
    }

    /**
     * @return the level of the check location
     */
    public String getAttrLevel() {
        return attrLevel;
    }

    /**
     * @return list of edges' names which used for come to the checked object
     */
    public String getContext() {
        return context;
    }
}
