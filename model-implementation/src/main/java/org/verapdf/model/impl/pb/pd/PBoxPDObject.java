package org.verapdf.model.impl.pb.pd;

import org.apache.fontbox.cmap.CMap;
import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionNamed;
import org.verapdf.model.GenericModelObject;
import org.verapdf.model.pdlayer.PDAction;
import org.verapdf.model.pdlayer.PDObject;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDObject extends GenericModelObject implements PDObject {

    protected COSObjectable simplePDObject;
    protected PDDocument document;
    protected PDContentStream contentStream;
    protected PDFontLike pdFontLike;
    protected CMap cMap;

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

    public PBoxPDObject(PDFontLike pdFontLike) {
        this.pdFontLike = pdFontLike;
    }

    public PBoxPDObject(CMap cMap) {
        this.cMap = cMap;
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

	protected void addAction(List<PDAction> actions, org.apache.pdfbox.pdmodel.interactive.action.PDAction buffer) {
		if (buffer != null) {
			actions.add(buffer instanceof PDActionNamed ?
					new PBoxPDNamedAction((PDActionNamed) buffer) : new PBoxPDAction(buffer));
		}
	}
}
