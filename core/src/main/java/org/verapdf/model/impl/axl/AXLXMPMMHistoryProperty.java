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

import org.verapdf.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.tools.xmp.SchemasDefinition;
import org.verapdf.model.xmplayer.XMPMMHistoryProperty;
import org.verapdf.model.xmplayer.XMPMMHistoryResourceEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class AXLXMPMMHistoryProperty extends AXLXMPProperty implements XMPMMHistoryProperty {

	public static final String XMPMM_HISTORY_PROPERTY_TYPE = "XMPMMHistoryProperty";

	public static final String RESOURCE_EVENTS = "ResourceEvents";

	public AXLXMPMMHistoryProperty(VeraPDFXMPNode xmpNode, boolean isMainMetadata, boolean isClosedChoiceCheck, SchemasDefinition mainPackageSchemasDefinition, SchemasDefinition currentSchemasDefinitionPDFA_1, SchemasDefinition currentSchemasDefinitionPDFA_2_3) {
		super(xmpNode, XMPMM_HISTORY_PROPERTY_TYPE, isMainMetadata, isClosedChoiceCheck, mainPackageSchemasDefinition, currentSchemasDefinitionPDFA_1, currentSchemasDefinitionPDFA_2_3);
	}

	/**
	 * @param link
	 *            name of the link
	 * @return List of all objects with link name
	 */
	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case RESOURCE_EVENTS:
				return this.getResourceEvents();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<XMPMMHistoryResourceEvent> getResourceEvents() {
		if (this.getisValueTypeCorrect() == Boolean.TRUE) {
			List<VeraPDFXMPNode> children = this.xmpNode.getChildren();
			List<XMPMMHistoryResourceEvent> res = new ArrayList<>(children.size());
			for (VeraPDFXMPNode node : children) {
				res.add(new AXLXMPMMHistoryResourceEvent(node));
			}
			return Collections.unmodifiableList(res);
		}

		return Collections.emptyList();
	}
}
