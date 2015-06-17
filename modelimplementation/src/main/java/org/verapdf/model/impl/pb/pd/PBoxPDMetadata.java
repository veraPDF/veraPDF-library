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
}