/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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
import org.verapdf.model.baselayer.Object;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
class FastFailValidator extends BaseValidator {
    private final int maxFailedTests;
    protected int failureCount = 0;

    /**
     * @param profile
     * @param logPassedChecks
     */
    protected FastFailValidator(final ValidationProfile profile, final boolean logPassedChecks) {
        this(profile, logPassedChecks, 0, false, false, 0);
    }

    /**
     * @param profile
     * @param logPassedChecks
     */
    protected FastFailValidator(final ValidationProfile profile, final boolean logPassedChecks,
                                final int maxFailedTests, final boolean showErrorMessages, boolean showProgress,
                                int maxNumberOfDisplayedFailedChecks) {
        this(Collections.singletonList(profile), logPassedChecks, maxFailedTests, showErrorMessages, showProgress, maxNumberOfDisplayedFailedChecks);
    }

    protected FastFailValidator(final List<ValidationProfile> profiles, final boolean logPassedChecks,
                                final int maxFailedTests, final boolean showErrorMessages, boolean showProgress,
                                int maxNumberOfDisplayedFailedChecks) {
        super(profiles, maxNumberOfDisplayedFailedChecks, logPassedChecks, showErrorMessages, showProgress);
        this.maxFailedTests = maxFailedTests;
    }

    @Override
    protected void processAssertionResult(FlavourValidator flavourValidator, final boolean assertionResult,
            final String locationContext, final Rule rule, final Object obj) {
        super.processAssertionResult(flavourValidator, assertionResult, locationContext, rule, obj);
        if (!assertionResult) {
            this.failureCount++;
            if ((this.maxFailedTests > 0) && (this.failureCount >= this.maxFailedTests)) {
                this.abortProcessing = true;
            }
        }
    }

}
