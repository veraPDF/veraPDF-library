package org.verapdf.metadata.fixer.utils.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class ProcessedObjects {

	private final List<RuleDescription> ruleDescriptions = new ArrayList<>();

	public List<RuleDescription> getRuleDescriptions() {
		return ruleDescriptions;
	}

	public void addCheckObject(RuleDescription object) {
		this.ruleDescriptions.add(object);
	}

	public boolean contains(String objectType, String test) {
		for (RuleDescription object : this.ruleDescriptions) {
			if (object.compareTo(objectType, test)) {
				return true;
			}
		}

		return false;
	}
}
