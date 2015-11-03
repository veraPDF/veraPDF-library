package org.verapdf.model.impl.pb.external;

import org.apache.pdfbox.cos.COSDictionary;
import org.verapdf.model.external.EmbeddedFile;

/**
 * Embedded file representation implemented by Apache PDFBox
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxEmbeddedFile extends PBoxExternal implements EmbeddedFile {

	/** Type name for {@code PBoxEmbeddedFile} */
	public static final String EMBEDDED_FILE_TYPE = "EmbeddedFile";

	private final COSDictionary dictionary;

	public PBoxEmbeddedFile(COSDictionary dictionary) {
		super(EMBEDDED_FILE_TYPE);
		this.dictionary = dictionary;
	}

	// TODO : implement me
	@Override
	public String getSubtype() {
		return null;
	}

	// TODO : implement me
	@Override
	public Boolean getisValidPDFA12() {
		return Boolean.FALSE;
	}
}
