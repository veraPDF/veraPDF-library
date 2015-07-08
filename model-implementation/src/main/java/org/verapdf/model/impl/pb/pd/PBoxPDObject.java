package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.verapdf.model.GenericModelObject;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDObject extends GenericModelObject implements org.verapdf.model.baselayer.Object {

    protected COSObjectable simplePDObject;
    protected PDDocument document;
    protected PDContentStream contentStream;

    private String type = "PDObject";
    private String id;

    public PBoxPDObject (COSObjectable simplePDObject) {
        this.simplePDObject = simplePDObject;
    }

    public PBoxPDObject (PDDocument document) {
        this.document = document;
    }

    public PBoxPDObject (PDContentStream contentStream) {
        this.contentStream = contentStream;
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
