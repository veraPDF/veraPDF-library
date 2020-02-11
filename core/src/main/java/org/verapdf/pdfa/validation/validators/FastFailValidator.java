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
package org.verapdf.pdfa.validation.validators;

import org.verapdf.pdfa.validation.profiles.Rule;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
class FastFailValidator extends BaseValidator {
    private final int maxFailedTests;
    protected int failureCount = 0;

    /**
     * @param profile
     * @param logPassedTests
     */
    protected FastFailValidator(final ValidationProfile profile,
                                final boolean logPassedTests) {
        this(profile, logPassedTests, -1, 0);
    }

    /**
     * @param profile
     * @param logPassedTests
     */
    protected FastFailValidator(final ValidationProfile profile,
                                final boolean logPassedTests,
                                final int maxFailedTests) {
        this(profile, logPassedTests,-1, maxFailedTests);
    }

    /**
     * @param profile
     * @param logPassedTests
     */
    protected FastFailValidator(final ValidationProfile profile,
                                final boolean logPassedTests,
                                final int maxDetailedChecksPerRule,
                                final int maxFailedTests) {
        super(profile, logPassedTests, maxDetailedChecksPerRule);
        this.maxFailedTests = maxFailedTests;
    }

    @Override
    protected void processAssertionResult(final boolean assertionResult,
                                          final String locationContext, final Rule rule) {
        super.processAssertionResult(assertionResult, locationContext, rule);
        if (!assertionResult) {
            this.failureCount++;
            if ((this.maxFailedTests > 0) && (this.failureCount >= this.maxFailedTests)) {
                this.abortProcessing = true;
            }
        }
    }

}
