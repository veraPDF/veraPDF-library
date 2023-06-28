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
import org.verapdf.model.xmplayer.XMPMMHistoryResourceEvent;

/**
 * @author Maksim Bezrukov
 */
public class AXLXMPMMHistoryResourceEvent extends AXLXMPObject implements XMPMMHistoryResourceEvent {

	public static final String XMPMM_HISTORY_RESOURCE_EVENT_TYPE = "XMPMMHistoryResourceEvent";

	protected final VeraPDFXMPNode xmpNode;

	public AXLXMPMMHistoryResourceEvent(VeraPDFXMPNode xmpNode) {
		super(XMPMM_HISTORY_RESOURCE_EVENT_TYPE);
		this.xmpNode = xmpNode;
	}

	@Override
	public String getaction() {
		for (VeraPDFXMPNode node : xmpNode.getChildren()) {
			if (XMPConst.TYPE_RESOURCEEVENT.equals(node.getNamespaceURI())
					&& "action".equals(node.getName())) {
				return node.getValue();
			}
		}

		return null;
	}

	@Override
	public String getparameters() {
		for (VeraPDFXMPNode node : xmpNode.getChildren()) {
			if (XMPConst.TYPE_RESOURCEEVENT.equals(node.getNamespaceURI())
					&& "parameters".equals(node.getName())) {
				return node.getValue();
			}
		}

		return null;
	}

	@Override
	public String getwhen() {
		for (VeraPDFXMPNode node : xmpNode.getChildren()) {
			if (XMPConst.TYPE_RESOURCEEVENT.equals(node.getNamespaceURI())
					&& "when".equals(node.getName())) {
				return node.getValue();
			}
		}

		return null;
	}
}
