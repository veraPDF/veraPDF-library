package org.verapdf.model.impl.pb.external;

import org.verapdf.model.GenericModelObject;

/**
 * Parent type for all external objects embedded into the PDF document
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxExternal extends GenericModelObject implements
        org.verapdf.model.external.External {

	protected PBoxExternal(String type) {
		super(type);
	}

}
