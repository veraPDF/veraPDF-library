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
package org.verapdf.model.tools.xmp.validators;

import org.verapdf.xmp.impl.VeraPDFXMPNode;
import org.verapdf.xmp.options.PropertyOptions;
import org.verapdf.model.tools.xmp.ValidatorsContainer;
import org.verapdf.model.tools.xmp.XMPConstants;

/**
 * @author Maksim Bezrukov
 */
public class ArrayTypeValidator {

    private final ArrayTypeEnum type;
    private final ValidatorsContainer parentContainer;

    private ArrayTypeValidator(ArrayTypeEnum type, ValidatorsContainer parentContainer) {
        this.type = type;
        this.parentContainer = parentContainer;
    }

    public static ArrayTypeValidator fromValues(ArrayTypeEnum type, ValidatorsContainer parentContainer) {
        if (parentContainer == null) {
            throw new IllegalArgumentException("Argument parent container can not be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Argument type can not be null");
        }
        return new ArrayTypeValidator(type, parentContainer);
    }

    public boolean isCorresponding(VeraPDFXMPNode node, String childType) {
        if (node == null) {
            throw new IllegalArgumentException("Argument node can not be null");
        }
        if (childType == null) {
            throw new IllegalArgumentException("Argument child type can not be null");
        }
        PropertyOptions options = node.getOptions();
        boolean isValidArrayType;
        switch (this.type) {
            case ALT:
                isValidArrayType = options.isArrayAlternate();
                break;
            case SEQ:
                isValidArrayType = options.isArrayOrdered() && !options.isArrayAlternate();
                break;
            case BAG:
                isValidArrayType = options.isArray() && !(options.isArrayOrdered() || options.isArrayAlternate());
                break;
            default:
                throw new IllegalStateException("Array type validator must be created with valid type");
        }

        if (!isValidArrayType) {
            return false;
        }
        for (VeraPDFXMPNode child : node.getChildren()) {
            if (!this.parentContainer.validate(child, childType)) {
                return false;
            }
        }
        return true;
    }

    public enum ArrayTypeEnum {

        BAG(XMPConstants.BAG),
        ALT(XMPConstants.ALT),
        SEQ(XMPConstants.SEQ);

        private final String type;

        ArrayTypeEnum(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }

        public static ArrayTypeEnum fromString(String type) {
            if (type != null) {
                for (ArrayTypeEnum typeEnum : ArrayTypeEnum.values()) {
                    if (type.equalsIgnoreCase(typeEnum.type)) {
                        return typeEnum;
                    }
                }
            }
            return null;
        }
    }
}
