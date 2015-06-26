package org.verapdf.model.impl.pb.pd;

import org.verapdf.model.pdlayer.PDMetadata;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDMetadata extends PBoxPDObject implements PDMetadata {

    public PBoxPDMetadata(org.apache.pdfbox.pdmodel.common.PDMetadata simplePDObject) {
        super(simplePDObject);
        setType("PDMetadata");
    }

    @Override
    public String getfilter() {
        //TODO : implement this method
        return null;
    }

    @Override
    public String getsubType() {
        //TODO : implement this method
        return null;
    }

    @Override
    public String gettypeValue() {
        //TODO : implement this method
        return null;
    }

}