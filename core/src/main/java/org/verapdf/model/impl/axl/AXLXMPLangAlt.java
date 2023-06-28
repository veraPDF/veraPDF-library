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
package org.verapdf.model.impl.axl;

import org.verapdf.xmp.XMPConst;
import org.verapdf.xmp.impl.VeraPDFXMPNode;
import org.verapdf.xmp.impl.XMPNode;
import org.verapdf.model.tools.xmp.SchemasDefinition;
import org.verapdf.model.xmplayer.XMPLangAlt;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.List;

/**
 * @author Maxim Plushchov
 */
public class AXLXMPLangAlt extends AXLXMPProperty implements XMPLangAlt {

	public static final String XMPMM_LANGUAGE_ALTERNATIVE_PROPERTY_TYPE = "XMPLangAlt";

	public AXLXMPLangAlt(VeraPDFXMPNode xmpNode, boolean isMainMetadata, boolean isClosedChoiceCheck, SchemasDefinition mainPackageSchemasDefinition, SchemasDefinition currentSchemasDefinitionPDFA_1, SchemasDefinition currentSchemasDefinitionPDFA_2_3, PDFAFlavour flavour) {
		super(xmpNode, XMPMM_LANGUAGE_ALTERNATIVE_PROPERTY_TYPE, isMainMetadata, isClosedChoiceCheck, mainPackageSchemasDefinition, currentSchemasDefinitionPDFA_1, currentSchemasDefinitionPDFA_2_3, flavour);
	}

	@Override
	public Boolean getxDefault() {
		if (xmpNode.getChildren().size() != 1) {
			return false;
		}
		VeraPDFXMPNode child = xmpNode.getChildren().get(0);
		List elems = child.getQualifier();
		for(Object elem : elems) {
			if (((XMPNode)elem).isLanguageNode() && XMPConst.X_DEFAULT.equals(((XMPNode)elem).getValue())) {
				return true;
			}
		}
		return false;
	}
}
