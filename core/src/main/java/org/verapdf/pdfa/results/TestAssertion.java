/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
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

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.verapdf.pdfa.validation.profiles.ErrorArgument;
import org.verapdf.pdfa.validation.profiles.RuleId;

import java.util.List;

/**
 * A TestAssertion records the result of performing a validation test on a
 * particular document property, or set of properties.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@XmlJavaTypeAdapter(TestAssertionImpl.Adapter.class)
public interface TestAssertion {
    /**
     * @return the ordinal for the instance
     */
    public int getOrdinal();
    /**
     * @return the String id for the {@link org.verapdf.pdfa.validation.profiles.Rule} that this assertion refers to
     */
    public RuleId getRuleId();

    /**
     * @return the {@link Status} that indicates the result of this test
     *         assertion
     */
    public Status getStatus();

    /**
     * @return any message that accompanies this assertion.
     */
    public String getMessage();

    /**
     * @return location context.
     */
    public String getLocationContext();

    /**
     * @return error message.
     */
    public String getErrorMessage();

    /**
     * @return the {@link Location} within the PDF document where this test was
     *         asserted.
     */
    public Location getLocation();

    /**
     * @return the list of error arguments
     */
    public List<ErrorArgument> getErrorArguments();

    /**
     * Enum that indicates the result of a particular test assertion, i.e.
     * whether the test passed or failed
     * 
     * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
     */
    public enum Status {
        /**
         * Indicates that a test assertion passed
         */
        PASSED("passed"),
        /**
         * Indicates a test failure
         */
        FAILED("failed"),
        /**
         * 
         */
        UNKNOWN("unknown");
        
        private final String description;
        
        private Status(final String description) {
            this.description = description;
        }
        
        @Override
        public String toString() {
            return this.description;
        }
    }

}
