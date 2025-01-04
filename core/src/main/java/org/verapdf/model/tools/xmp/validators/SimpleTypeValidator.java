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
package org.verapdf.model.tools.xmp.validators;

import org.verapdf.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.tools.xmp.XMPConstants;

import java.util.regex.Pattern;

/**
 * @author Maksim Bezrukov
 */
public class SimpleTypeValidator implements TypeValidator {

    private final Pattern pattern;

    private SimpleTypeValidator(Pattern pattern) {
        this.pattern = pattern;
    }

    public static SimpleTypeValidator fromValue(SimpleTypeEnum type) {
        if (type == null) {
            throw new IllegalArgumentException("Argument type can not be null");
        }
        String regexp = type.getRegexp();
        return new SimpleTypeValidator(Pattern.compile(regexp));
    }

    public static SimpleTypeValidator fromValue(Pattern pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Argument type can not be null");
        }
        return new SimpleTypeValidator(pattern);
    }

    @Override
    public boolean isCorresponding(VeraPDFXMPNode node) {
        if (node == null) {
            throw new IllegalArgumentException("Argument node can not be null");
        }
        return node.getOptions().isSimple() && (this.pattern == null || this.pattern.matcher(node.getValue()).matches());
    }

    public enum SimpleTypeEnum {

        BOOLEAN(XMPConstants.BOOLEAN, "^True$|^False$"),
        INTEGER(XMPConstants.INTEGER, "^[+-]?\\d+$"),
        REAL(XMPConstants.REAL, "^[+-]?\\d+\\.?\\d*|[+-]?\\d*\\.?\\d+$"),
        MIME_TYPE(XMPConstants.MIME_TYPE, "^[-\\w+\\.]+/[-\\w+\\.]+$"),
        PROPER_NAME(XMPConstants.PROPER_NAME, "(?s)(^.*$)"),
        TEXT(XMPConstants.TEXT, "(?s)(^.*$)"),
        AGENT_NAME(XMPConstants.AGENT_NAME, "(?s)(^.*$)"),
        RATIONAL(XMPConstants.RATIONAL, "(?s)(^.*$)"),
        RENDITION_CLASS(XMPConstants.RENDITION_CLASS, "(?s)(^.*$)");

        private final String type;
        private final String regexp;

        SimpleTypeEnum(String type, String regexp) {
            this.type = type;
            this.regexp = regexp;
        }

        public String getType() {
            return this.type;
        }

        public String getRegexp() {
            return this.regexp;
        }

        public static SimpleTypeEnum fromString(String type) {
            if (type != null) {
                for (SimpleTypeEnum typeEnum : SimpleTypeEnum.values()) {
                    if (type.equalsIgnoreCase(typeEnum.type)) {
                        return typeEnum;
                    }
                }
            }
            return null;
        }
    }
}
