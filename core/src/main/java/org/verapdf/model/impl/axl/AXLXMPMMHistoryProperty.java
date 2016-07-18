package org.verapdf.model.impl.axl;

import com.adobe.xmp.impl.VeraPDFXMPNode;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.tools.xmp.SchemasDefinition;
import org.verapdf.model.xmplayer.XMPMMHistoryProperty;
import org.verapdf.model.xmplayer.XMPMMHistoryResourceEvent;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public class AXLXMPMMHistoryProperty extends AXLXMPProperty implements XMPMMHistoryProperty {

	public static final String XMPMM_HISTORY_PROPERTY_TYPE = "XMPMMHistoryProperty";

	public static final String RESOURCE_EVENTS = "ResourceEvents";

	public AXLXMPMMHistoryProperty(VeraPDFXMPNode xmpNode, boolean isMainMetadata, boolean isClosedChoiceCheck, SchemasDefinition mainPackageSchemasDefinition, SchemasDefinition currentSchemasDefinitionPDFA_1, SchemasDefinition currentSchemasDefinitionPDFA_2_3, PDFAFlavour flavour) {
		super(xmpNode, XMPMM_HISTORY_PROPERTY_TYPE, isMainMetadata, isClosedChoiceCheck, mainPackageSchemasDefinition, currentSchemasDefinitionPDFA_1, currentSchemasDefinitionPDFA_2_3, flavour);
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
		if (this.getisValueTypeCorrect()) {
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
