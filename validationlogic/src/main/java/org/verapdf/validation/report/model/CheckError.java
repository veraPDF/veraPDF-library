package org.verapdf.validation.report.model;

import java.util.List;

/**
 * Structure of the error/warning in a check.
 * Created by bezrukov on 5/6/15.
 *
 * @author Maksim Bezrukov
 * @version 1.0
 * @see Check
 */
public class CheckError {
    private String message;
    private List<String> argument;

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
