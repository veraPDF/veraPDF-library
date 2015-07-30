package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.verapdf.model.pdlayer.PDResource;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDResources extends PBoxPDObject implements PDResource {

    public static final String PD_RESOURCE_TYPE = "PDResource";

    protected PBoxPDResources(COSObjectable simplePDObject) {
        super(simplePDObject);
        setType(PD_RESOURCE_TYPE);
    }

    protected PBoxPDResources(PDFontLike pdFontLike) {
        super(pdFontLike);
        setType(PD_RESOURCE_TYPE);
    }

}
