package org.verapdf.validation.profile.model;

import java.util.List;

/**
 * Structure of the error/warning in a rule.
 *
 * @author Maksim Bezrukov
 */
public class RuleError {
    private String message;
    private List<String> argument;

    /**
     * Creates new  rule error model.
     *
     * @param message  - error message
     * @param argument - list of arguments for the message
     */
    public RuleError(String message, List<String> argument) {
        this.message = message;
        this.argument = argument;
    }

    /**
     * @return Text provided by attribute "argument".
     */
    public List<String> getArgument() {
        return argument;
    }

    /**
     * @return Text provided by attribute "message".
     */
    public String getMessage() {
        return message;
    }
}
