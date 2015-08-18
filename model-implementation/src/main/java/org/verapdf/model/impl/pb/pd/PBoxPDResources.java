package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.font.PDFontLike;
import org.verapdf.model.pdlayer.PDResource;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDResources extends PBoxPDObject implements PDResource {

	protected PBoxPDResources(COSObjectable simplePDObject, final String type) {
		super(simplePDObject, type);
	}

	protected PBoxPDResources(PDFontLike pdFontLike, final String type) {
		super(pdFontLike, type);
	}

}
