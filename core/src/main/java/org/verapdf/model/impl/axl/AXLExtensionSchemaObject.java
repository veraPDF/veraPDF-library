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
package org.verapdf.model.impl.axl;

import org.verapdf.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.tools.xmp.ValidatorsContainer;
import org.verapdf.model.xmplayer.ExtensionSchemaObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Maksim Bezrukov
 */
public abstract class AXLExtensionSchemaObject extends AXLXMPObject implements ExtensionSchemaObject {

    protected final VeraPDFXMPNode xmpNode;
    protected final ValidatorsContainer containerForPDFA_1;
    protected final ValidatorsContainer containerForPDFA_2_3;

    public AXLExtensionSchemaObject(String type, VeraPDFXMPNode xmpNode, ValidatorsContainer containerForPDFA_1, ValidatorsContainer containerForPDFA_2_3) {
        super(type);
        this.xmpNode = xmpNode;
        this.containerForPDFA_1 = containerForPDFA_1;
        this.containerForPDFA_2_3 = containerForPDFA_2_3;
    }

    @Override
    public Boolean getcontainsUndefinedFields() {
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (!getValidNamespaceURI().equals(child.getNamespaceURI()) || !getValidChildNames().contains(child.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getundefinedFields() {
        List<String> undefinedFields = new LinkedList<>();
        for (VeraPDFXMPNode child : this.xmpNode.getChildren()) {
            if (!getValidNamespaceURI().equals(child.getNamespaceURI()) || !getValidChildNames().contains(child.getName())) {
                undefinedFields.add(child.getName() + "(" + child.getNamespaceURI() + ")");
            }
        }
        return String.join(",", undefinedFields);
    }

    protected abstract String getValidNamespaceURI();

    protected abstract Set<String> getValidChildNames();

}
