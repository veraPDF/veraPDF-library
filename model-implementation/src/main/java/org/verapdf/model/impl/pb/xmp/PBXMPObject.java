package org.verapdf.model.impl.pb.xmp;

import org.verapdf.model.GenericModelObject;
import org.verapdf.model.xmplayer.XMPObject;

/**
 * Current class is representation of XMPObject interface from abstract model based on xmpbox from pdfbox.
 *
 * @author Maksim Bezrukov
 */
public class PBXMPObject extends GenericModelObject implements XMPObject {

	protected PBXMPObject(final String type) {
		super(type);
	}

}
