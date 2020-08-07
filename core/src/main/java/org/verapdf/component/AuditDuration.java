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
package org.verapdf.component;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Simple class that encapsulates the duration of an action in milliseconds.
 * Used for recording durations for process auditing.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1
 */
@XmlJavaTypeAdapter(AuditDurationImpl.Adapter.class)
public interface AuditDuration {
	/**
	 * @return the start time in millisecs
	 */
	long getStart();

	/**
	 * @return the finish time in millisecs
	 */
	long getFinish();

	/**
	 * @return the difference between the start and finish time in millisecs
	 */
	long getDifference();

	/**
	 * @return a string representation of the duration.
	 */
	String getDuration();
}