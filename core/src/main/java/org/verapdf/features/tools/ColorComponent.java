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
package org.verapdf.features.tools;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author  Maksim Bezrukov
 */

public enum ColorComponent {
	GRAY_COMPONENTS(EnumSet.of(Colors.GRAY)),
	RGB_COMPONENTS(EnumSet.of(Colors.RED, Colors.GREEN, Colors.BLUE)),
	CMKY_COMPONENTS(EnumSet.of(Colors.CYAN, Colors.MAGENTA, Colors.YELLOW, Colors.BLACK));

	private final EnumSet<Colors> colors;

	ColorComponent(final EnumSet<Colors> colors) {
		this.colors = colors;
	}

	public int getSize() {
		return this.colors.size();
	}

	public EnumSet<Colors> getColors() {
		return this.colors;
	}

	public Map<String, String> createAttributesMap(double[] componentValues) {
		Map<String, String> attMap = new HashMap<>();
		for (Colors color : this.getColors()) {
			attMap.put(color.getName(), CreateNodeHelper.formatDouble(componentValues[color.getPosition()], 6));
		}
		return attMap;
	}

	public enum Colors {
		GRAY(0, "gray"),
		RED(0, "red"),
		GREEN(1, "green"),
		BLUE(2, "blue"),
		CYAN(0, "cyan"),
		MAGENTA(1, "magenta"),
		YELLOW(2, "yellow"),
		BLACK(3, "black");

		private final int position;
		private final String name;

		Colors(final int position, final String name) {
			this.position = position;
			this.name = name;
		}

		public int getPosition() {
			return this.position;
		}

		public String getName() {
			return this.name;
		}
	}
}
