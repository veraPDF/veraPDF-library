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
package org.verapdf.processor;

import java.util.Collection;

import org.verapdf.ReleaseDetails;
import org.verapdf.component.Component;

/**
 * Base interface for veraPDF Processors, mandates some basic properties.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 2 Nov 2016:11:22:06
 */
public interface Processor extends Component {
	/**
	 * @return the {@link ProcessorConfig} that holds the details for the
	 *         configuration of this {@link Processor}
	 */
	public ProcessorConfig getConfig();

	/**
	 * @return a {@link Collection} of {@link ReleaseDetails} for the
	 *         {@link Processor}'s dependencies, for auditing.
	 */
	public Collection<ReleaseDetails> getDependencies();
}
