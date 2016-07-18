package org.verapdf.model.impl.axl;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.impl.VeraPDFXMPNode;
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
