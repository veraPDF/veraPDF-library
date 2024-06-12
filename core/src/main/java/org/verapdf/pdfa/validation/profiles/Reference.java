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
package org.verapdf.pdfa.validation.profiles;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * A Reference is used to correlate a validation {@link Rule} to a specific
 * clause in a PDF/A Specification Part.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@XmlJavaTypeAdapter(ReferenceImpl.Adapter.class)
public interface Reference {
    /**
     * @return a {@link String} that identifies the Specification referred to.
     */
    public String getSpecification();

    /**
     * @return a String identifier for the Specification clause referred to,
     *         e.g. 6.1.2
     */
    public String getClause();
}
