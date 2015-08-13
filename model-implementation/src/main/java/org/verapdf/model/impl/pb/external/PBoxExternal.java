package org.verapdf.model.impl.pb.external;

import org.verapdf.model.GenericModelObject;

/**
 * Parent type for all external objects embedded into the PDF document
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxExternal extends GenericModelObject implements
        org.verapdf.model.external.External {

    private String type;

    private String id;

	protected PBoxExternal(String type) {
		this.type = type;
	}

    /**
     * @return type of current object
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Setting type of current object. Can be used only in constructor
     *
     * @param type
     *            type of current object
     */
    protected void setType(String type) {
        this.type = type;
    }

    /**
     * Getting id of current object. For external objects is always null.
     *
     * @return id of current object
     */
    @Override
    public String getID() {
        return id;
    }
}
