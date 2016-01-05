package org.verapdf.model.impl.axl;

import org.verapdf.model.GenericModelObject;
import org.verapdf.model.xmplayer.XMPObject;

/**
 * Current class is representation of XMPObject interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class AXLXMPObject extends GenericModelObject implements XMPObject {

	protected AXLXMPObject(final String type) {
		super(type);
	}

}
