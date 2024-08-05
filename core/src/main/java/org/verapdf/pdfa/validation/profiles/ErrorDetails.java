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
package org.verapdf.pdfa.validation.profiles;

import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 * Encapsulates the details of an error message, a String message and a
 * <code>List&lt;String&gt;</code> of arguments to substitute into the error message
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlJavaTypeAdapter(ErrorDetailsImpl.Adapter.class)
public interface ErrorDetails {
    /**
     * @return the Error message as a String
     */
    public String getMessage();

    /**
     * @return a List of String arguments for the error, or an empty List if
     *         there are no args.
     */
    public List<ErrorArgument> getArguments();
}
