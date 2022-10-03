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
package org.verapdf.processor;

import java.util.NoSuchElementException;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public enum FormatOption {
    /**
     * Output in XML format
     */
    RAW("raw"),
    /**
     * Output in {@link MachineReadableReport} XML format
     */
    MRR("mrr"),
    XML("xml"),
    /**
     * Output in brief format
     */
    TEXT("text"),
    /**
     * Output in HTML format
     */
    HTML("html"),
    /**
     * Output in JSON format
     */
    JSON("json");

    private final String option;

    private FormatOption(final String option) {
        this.option = option;
    }	

    /**
     * @return the option string for the {@code FormatType} instance.
     */
    public String getOption() {
        return this.option;
    }

    @Override
    public String toString() {
        return this.getOption();
    }

    /**
     * Performs a match against the parameter {@code String option} of each
     * {@code FormatType}'s option and returns a matching instance. Defaults to
     * returning {@link FormatOption#RAW} if no match can be made.
     * 
     * @param option
     *            the string CLI option to compare
     * @return a matching {@code FormatType} instance or {@code XML} if no match
     *         can be made.
     */
    public static FormatOption fromOption(final String option) {
        for (FormatOption format : FormatOption.values()) {
            if (format.toString().equalsIgnoreCase(option))
                return format;
        }
        throw new NoSuchElementException("No FormatOption with option value: " + option + " found.");
    }
}
