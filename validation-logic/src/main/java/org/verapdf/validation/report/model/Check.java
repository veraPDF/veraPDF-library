package org.verapdf.validation.report.model;

/**
 * Structure of the check of the rule.
 *
 * @author Maksim Bezrukov
 */
public class Check {
    /**
     * Enum to cement the passed failed option for a check
     * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
     */
    public enum Status {
        /** For passed Checks */
        PASSED("passed"),
        /** For failed Checks */
        FAILED("failed");
        
        private final String value;
        Status(final String val) {
            this.value = val;
        }
        @Override
        public String toString() {
            return this.value;
        }
    }
    private final Status status;
    private final CheckLocation location;
    private final CheckError error;
    private final boolean isHasError;

    /**
     * Creates Check model for validation report
     *
     * @param status - status of the check
     * @param location   - location of the check
     * @param error      - error of the check
     * @param isHasError - is the error represents an error or a warning
     */
    public Check(final Status status, final CheckLocation location, final CheckError error, final boolean isHasError) {
        this.status = status;
        this.location = location;
        this.error = error;
        this.isHasError = isHasError;
    }

    /**
     * @return status (passed/failed) of the check
     */
    public Status getStatus() {
        return this.status;
    }

    /**
     * @return location structure of the check
     */
    public CheckLocation getLocation() {
        return this.location;
    }

    /**
     * @return an error/warning message
     */
    public CheckError getError() {
        return this.error;
    }

    /**
     * Get what {@code error} (if {@code this} check has it) represents: an error or a warning.
     *
     * @return true if {@code error} represents an error, and false if {@code error} represents a warning (or null).
     */
    public boolean isHasError() {
        return this.isHasError;
    }
}
