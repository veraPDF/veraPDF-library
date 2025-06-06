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
package org.verapdf.processor.plugins;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name = "attribute")
public class Attribute {

    @XmlAttribute
    private final String key;
    @XmlAttribute
    private final String value;

    private Attribute(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private Attribute() {
        this("", "");
    }

    /**
     * Creates attribute key and value.
     *
     * @param key   an attribute key
     * @param value an attribute value
     *
     * @return an attribute a pair of key and value
     */
    public static Attribute fromValues(String key, String value) {
        return new Attribute(key, value);
    }

    /**
     * Gets attribute key.
     *
     * @return an attribute key
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets attribute value.
     *
     * @return an attribute value
     */
    public String getValue() {
        return value;
    }
}
