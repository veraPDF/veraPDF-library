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
import org.verapdf.model.tools.xmp.ValidatorsContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Maksim Bezrukov
 */
public class StructuredTypeValidator implements TypeValidator {

    private final String childNamespaceURI;
    private final Map<String, String> childrenTypes;
    private final ValidatorsContainer parentContainer;

    private StructuredTypeValidator(String childNamespaceURI, Map<String, String> childrenTypes, ValidatorsContainer parentContainer) {
        this.childNamespaceURI = childNamespaceURI;
        this.childrenTypes = childrenTypes;
        this.parentContainer = parentContainer;
    }

    public static StructuredTypeValidator fromValues(String childNamespaceURI, Map<String, String> childrenTypes, ValidatorsContainer parentContainer) {
        if (childNamespaceURI == null) {
            throw new IllegalArgumentException("Argument child namespace URI can not be null");
        }
        if (childrenTypes == null || childrenTypes.isEmpty()) {
            throw new IllegalArgumentException("Argument children types can not be null or empty");
        }
        if (parentContainer == null) {
            throw new IllegalArgumentException("Argument parent container can not be null");
        }

        return new StructuredTypeValidator(childNamespaceURI, new HashMap<>(childrenTypes), parentContainer);
    }

    @Override
    public boolean isCorresponding(VeraPDFXMPNode node) {
        if (!node.getOptions().isStruct()) {
            return false;
        }
        for (VeraPDFXMPNode child : node.getChildren()) {
            if (!(this.childrenTypes.containsKey(child.getName()) &&
                    this.childNamespaceURI.equals(child.getNamespaceURI()) &&
                    this.parentContainer.validate(child, this.childrenTypes.get(child.getName())))) {
                return false;
            }
        }
        return true;
    }
}
