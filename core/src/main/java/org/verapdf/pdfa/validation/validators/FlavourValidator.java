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
