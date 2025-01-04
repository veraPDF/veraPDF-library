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
package org.verapdf.core;

/**
 * Exception type for PDFAParser problems.
 *
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */

public class ModelParsingException extends VeraPDFException {
    /**
     * 
     */
    private static final long serialVersionUID = -6507757998419697602L;

    /**
     * Default constructor for ModelParsingException.
     */
    public ModelParsingException() {
        super();
    }

    /**
     * Constructs new ModelParsingException with a String message
     *
     * @param message
     *            a String message describing the cause of the exception.
     */
    public ModelParsingException(String message) {
        super(message);
    }

    /**
     * Constructs new ModelParsingException with a String message and a Throwable
     * cause.
     *
     * @param message
     *            a String message describing the cause of the exception.
     * @param cause
     *            Throwable cause of the exception.
     */
    public ModelParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
