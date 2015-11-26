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
public interface MetadataFixerResult extends Iterable<String> {
    /**
     * @return the {@link RepairStatus} for the fix task
     */
    RepairStatus getRepairStatus();

    /**
     * @return a List of Strings recording all fixes applies
     */
    List<String> getAppliedFixes();

    /**
     * Enumeration that indicates the status of a metadata repair task.
     * 
     * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
     */
    enum RepairStatus {
        /**
         * Metadata repair was carried out successfully.
         */
        SUCCESS("Repair successful"),
        /**
         * Metadata repair was attempted but failed.
         */
        FIX_ERROR("Error during repair"),
        /**
         * The fixer could not determine any action that could repair the PDF/A.
         */
        WONT_FIX("Can`t Fix"),
        /**
         * No action was taken because the file is already valid
         */
        NO_ACTION("No Action"),

		ID_REMOVED("ID Removed");

        private final String message;

        RepairStatus(final String name) {
            this.message = name;
        }

        @Override
        public String toString() {
            return this.message;
        }
    }
}
