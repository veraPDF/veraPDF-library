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
package org.verapdf.pdfa.validation.validators;

import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.validation.profiles.Rule;
import org.verapdf.pdfa.validation.profiles.RuleId;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlavourValidator {

    private final ValidationProfile profile;

    protected boolean isCompliant = true;
    private final Map<Rule, List<BaseValidator.ObjectWithContext>> deferredRules = new HashMap<>();
    protected final List<TestAssertion> results = new ArrayList<>();
    private final HashMap<RuleId, Integer> failedChecks = new HashMap<>();
    protected int testCounter = 0;

    public FlavourValidator(ValidationProfile profile) {
        this.profile = profile;
    }

    public ValidationProfile getProfile() {
		return this.profile;
	}

    public Map<Rule, List<BaseValidator.ObjectWithContext>> getDeferredRules() {
        return deferredRules;
    }

    public HashMap<RuleId, Integer> getFailedChecks() {
        return failedChecks;
    }
}
