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

import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.tools.xmp.ValidatorsContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Maksim Bezrukov
 */
public class StructuredTypeWithRestrictedFieldsValidator implements TypeValidator {

    private String childNamespaceURI;
    private Map<String, String> childrenTypes;
    private Map<String, Pattern> childrenChoiceTypes;
    private ValidatorsContainer parentContainer;

    public StructuredTypeWithRestrictedFieldsValidator(String childNamespaceURI, Map<String, String> childrenTypes, Map<String, Pattern> childrenChoiceTypes, ValidatorsContainer parentContainer) {
        this.childNamespaceURI = childNamespaceURI;
        this.childrenTypes = childrenTypes;
        this.childrenChoiceTypes = childrenChoiceTypes;
        this.parentContainer = parentContainer;
    }

    public static StructuredTypeWithRestrictedFieldsValidator fromValues(String childNamespaceURI, Map<String, String> childrenTypes, Map<String, Pattern> childrenClosedTypes, ValidatorsContainer parentContainer) {
        if (childNamespaceURI == null) {
            throw new IllegalArgumentException("Argument child namespace URI can not be null");
        }
        if (childrenTypes == null) {
            throw new IllegalArgumentException("Argument children types can not be null");
        }
        if (childrenClosedTypes == null) {
            throw new IllegalArgumentException("Argument children closed types can not be null");
        }
        if (parentContainer == null) {
            throw new IllegalArgumentException("Argument parent container can not be null");
        }

        return new StructuredTypeWithRestrictedFieldsValidator(childNamespaceURI, new HashMap<>(childrenTypes), new HashMap<>(childrenClosedTypes), parentContainer);
    }

    @Override
    public boolean isCorresponding(VeraPDFXMPNode node) {
        if (!node.getOptions().isStruct()) {
            return false;
        }
        for (VeraPDFXMPNode child : node.getChildren()) {
            if (!this.childNamespaceURI.equals(child.getNamespaceURI())) {
                return false;
            }

            if (this.childrenChoiceTypes.containsKey(child.getName())) {
                Pattern p = this.childrenChoiceTypes.get(child.getName());
                if (!p.matcher(child.getValue()).matches()) {
                    return false;
                }
            } else if (this.childrenTypes.containsKey(child.getName())) {
                if (!this.parentContainer.validate(child, this.childrenTypes.get(child.getName()))) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }
}
