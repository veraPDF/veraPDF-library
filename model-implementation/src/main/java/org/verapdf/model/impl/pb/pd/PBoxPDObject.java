package org.verapdf.model.impl.pb.pd;

import org.apache.fontbox.cmap.CMap;
import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.verapdf.model.GenericModelObject;
import org.verapdf.model.impl.pb.pd.actions.PBoxPDAction;
import org.verapdf.model.pdlayer.PDAction;
import org.verapdf.model.pdlayer.PDObject;

import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDObject extends GenericModelObject implements PDObject {

	public static final int MAX_NUMBER_OF_ELEMENTS = 1;

    protected COSObjectable simplePDObject;
    protected PDDocument document;
    protected PDContentStream contentStream;
    protected PDFontLike pdFontLike;
    protected CMap cMap;

	protected PBoxPDObject(COSObjectable simplePDObject, final String type) {
		super(type);
		this.simplePDObject = simplePDObject;
	}

	protected PBoxPDObject(PDDocument document, final String type) {
		super(type);
		this.document = document;
	}

	protected PBoxPDObject(PDContentStream contentStream, final String type) {
		super(type);
		this.contentStream = contentStream;
	}

	protected PBoxPDObject(PDFontLike pdFontLike, final String type) {
		super(type);
		this.pdFontLike = pdFontLike;
	}

	protected PBoxPDObject(CMap cMap, COSStream cMapFile, final String type) {
		super(type);
		this.cMap = cMap;
		this.simplePDObject = cMapFile;
	}

    protected void addAction(List<PDAction> actions,
            org.apache.pdfbox.pdmodel.interactive.action.PDAction buffer) {
        PDAction action = PBoxPDAction.getAction(buffer);
		if (action != null) {
			actions.add(action);
		}
    }
}
