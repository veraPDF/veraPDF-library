package org.verapdf.model.impl.pb.external;

import org.apache.fontbox.FontBoxFont;
import org.apache.fontbox.ttf.TrueTypeFont;
import org.apache.log4j.Logger;
import org.verapdf.model.external.TrueTypeFontProgram;

import java.io.IOException;

/**
 * @author Timur Kamalov
 */
public class PBoxTrueTypeFontProgram extends PBoxFontProgram implements TrueTypeFontProgram {

	private static final Logger LOGGER = Logger.getLogger(PBoxTrueTypeFontProgram.class);

	public static final String TRUE_TYPE_PROGRAM_TYPE = "TrueTypeFontProgram";

	private final Boolean isSymbolic;

	public PBoxTrueTypeFontProgram(FontBoxFont fontProgram, Boolean isSymbolic) {
		super(fontProgram, TRUE_TYPE_PROGRAM_TYPE);
		this.isSymbolic = isSymbolic;
	}

	@Override
	public Long getnrCmaps() {
		try {
			int nrCmaps = ((TrueTypeFont) this.fontProgram).getCmap().getCmaps().length;
			return Long.valueOf(nrCmaps);
		} catch (IOException e) {
			LOGGER.error(e);
		}
		return null;
	}

	@Override
	public Boolean getisSymbolic() {
		return this.isSymbolic;
	}
}
