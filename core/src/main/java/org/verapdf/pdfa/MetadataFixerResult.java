/**
 * 
 */
package org.verapdf.pdfa;

import java.util.List;

/**
 * Holds the result of an attempt to repair PDF/A metadata by a
 * {@link MetadataFixer}.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public interface MetadataFixerResult {
    /**
     * @return the {@link RepairStatus} for the fix task
     */
    public RepairStatus getRepairStatus();

    /**
     * @return a List of Strings recording all fixes applies
     */
    public List<String> getAppliedFixes();

    /**
     * Enumeration that indicates the status of a metadata repair task.
     * 
     * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
     */
    public enum RepairStatus {
        /**
         * Metadata repair was carried out successfully.
         */
        SUCCESSFUL,
        /**
         * Metadata repair was attempted but failed.
         */
        FAILED,
        /**
         * The fixer could not determine any action that could repair the PDF/A.
         */
        NOT_REPAIRABLE,
        /**
         * No action was taken because the file is already valid
         */
        NO_ACTION;
    }
}
