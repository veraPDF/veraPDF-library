package org.verapdf.validation.report.model;

import java.util.List;

/**
 * Structure of the error/warning in a check.
 *
 * @author Maksim Bezrukov
 */
public class CheckError {
    private String message;
    private List<String> argument;

    /**
     * Creates check error model
     *
     * @param message  - message of the error
     * @param argument - list of arguments for the message
     */
    public CheckError(String message, List<String> argument) {
        this.message = message;
        this.argument = argument;
    }

    /**
     * @return Error message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return Error arguments.
     */
    public List<String> getArgument() {
        return argument;
    }
}
