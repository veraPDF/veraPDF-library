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
