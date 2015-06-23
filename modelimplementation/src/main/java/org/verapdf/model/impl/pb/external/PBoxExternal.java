package org.verapdf.model.impl.pb.external;

import org.verapdf.model.GenericModelObject;

/**
 * Parent type for all external objects embedded into the PDF document
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxExternal extends GenericModelObject implements org.verapdf.model.external.External{

    private String type;

    private String id;

    protected PBoxExternal() {

    }

    @Override
    public String getType() {
        return type;
    }

    protected void setType(String type) {
        this.type = type;
    }

    @Override
    public String getID() {
        return id;
    }
}
