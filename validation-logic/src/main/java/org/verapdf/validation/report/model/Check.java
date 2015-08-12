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

    /**
     * Creates Check model for validation report
     *
     * @param status - status of the check
     * @param location   - location of the check
     * @param error      - error of the check
     */
    public Check(final Status status, final CheckLocation location, final CheckError error) {
        this.status = status;
        this.location = location;
        this.error = error;
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
}
