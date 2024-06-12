/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
/**
 * 
 */
package org.verapdf.pdfa.results;

import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Holds the result of an attempt to repair PDF/A metadata by a
 * {@link org.verapdf.pdfa.MetadataFixer}.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@XmlJavaTypeAdapter(MetadataFixerResultImpl.Adapter.class)
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
