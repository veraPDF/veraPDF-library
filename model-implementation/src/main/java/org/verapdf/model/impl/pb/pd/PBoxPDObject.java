package org.verapdf.model.impl.pb.pd;

import java.util.List;

import org.apache.fontbox.cmap.CMap;
import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionNamed;
import org.verapdf.model.GenericModelObject;
import org.verapdf.model.pdlayer.PDAction;
import org.verapdf.model.pdlayer.PDObject;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDObject extends GenericModelObject implements PDObject {

    protected COSObjectable simplePDObject;
    protected PDDocument document;
    protected PDContentStream contentStream;
    protected PDFontLike pdFontLike;
    protected CMap cMap;

    private final String type;
    private String id;

	protected PBoxPDObject(COSObjectable simplePDObject, final String type) {
		this.type = type;
		this.simplePDObject = simplePDObject;
	}

	protected PBoxPDObject(PDDocument document, final String type) {
		this.document = document;
		this.type = type;
	}

	protected PBoxPDObject(PDContentStream contentStream, final String type) {
		this.contentStream = contentStream;
		this.type = type;
	}

	protected PBoxPDObject(PDFontLike pdFontLike, final String type) {
		this.pdFontLike = pdFontLike;
		this.type = type;
	}

	protected PBoxPDObject(CMap cMap, final String type) {
		this.cMap = cMap;
		this.type = type;
	}

	@Override
    public String getType() {
        return type;
    }

    @Override
    public String getID() {
        return id;
    }

    protected void addAction(List<PDAction> actions,
            org.apache.pdfbox.pdmodel.interactive.action.PDAction buffer) {
        if (buffer != null) {
            actions.add(buffer instanceof PDActionNamed ? new PBoxPDNamedAction(
                    (PDActionNamed) buffer) : new PBoxPDAction(buffer));
        }
    }
}
