package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.verapdf.model.pdlayer.PDResource;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDResources extends PBoxPDObject implements PDResource {

    protected PBoxPDResources(COSObjectable simplePDObject) {
        super(simplePDObject);
        setType("PDResource");
    }
}
