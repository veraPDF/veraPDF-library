package org.verapdf.metadata.fixer.utils.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Current class represent list of rule descriptions
 *
 * @author Evgeniy Muravitskiy
 */
public class ProcessedObjects implements Iterable<RuleDescription> {

	private final List<RuleDescription> ruleDescriptions = new ArrayList<>();

	/**
	 * @return list of all rule descriptions
	 */
	public List<RuleDescription> getRuleDescriptions() {
		return this.ruleDescriptions;
	}

	/**
	 * Add another rule description
	 *
	 * @param object description of the rule
	 */
	public void addCheckObject(RuleDescription object) {
		this.ruleDescriptions.add(object);
	}

	/**
	 * Check if string representation of rule description contains in
	 * processed objects
	 *
	 * @param objectType passed type of applied object
	 * @param test passed test of rule description
	 * @return
	 */
	public boolean contains(String objectType, String test) {
		for (RuleDescription object : this.ruleDescriptions) {
			if (object.compareTo(objectType, test)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public Iterator<RuleDescription> iterator() {
		return this.ruleDescriptions.iterator();
	}
}
