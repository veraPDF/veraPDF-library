package org.verapdf.model.impl.pb.external;

import org.apache.pdfbox.pdmodel.common.PDStream;
import org.verapdf.model.external.FontProgram;

/**
 * @author Timur Kamalov
 */
public class PBoxFontProgram extends PBoxExternal implements FontProgram {

	public static final String FONT_PROGRAM_TYPE = "FontProgram";

	private PDStream fontProgram;

	//for validating widths array in simple fonts
	private Long firstChar;
	private Long lastChar;
	private Long widthSize;

	public PBoxFontProgram(PDStream fontProgram) {
		super();
		setType(FONT_PROGRAM_TYPE);
		this.fontProgram = fontProgram;
	}

	public PBoxFontProgram(PDStream fontProgram, Long firstChar, Long lastChar, Long widthSize) {
		this(fontProgram);
		this.firstChar = firstChar;
		this.lastChar = lastChar;
		this.widthSize = widthSize;
	}

}