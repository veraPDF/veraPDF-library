package org.verapdf.model.impl.pb.external;

import org.apache.fontbox.FontBoxFont;
import org.apache.fontbox.ttf.CmapTable;
import org.apache.fontbox.ttf.TrueTypeFont;
import org.apache.log4j.Logger;
import org.verapdf.model.external.TrueTypeFontProgram;

import java.io.IOException;

/**
 * Current class is representation of true type font program
 *
 * @author Timur Kamalov
 */
public class PBoxTrueTypeFontProgram extends PBoxFontProgram implements TrueTypeFontProgram {

	private static final Logger LOGGER = Logger.getLogger(PBoxTrueTypeFontProgram.class);

	/** Type name of {@code PBoxTrueTypeFontProgram} */
	public static final String TRUE_TYPE_PROGRAM_TYPE = "TrueTypeFontProgram";

	private final Boolean isSymbolic;

	/**
	 * Default constructor.
	 *
	 * @param fontProgram processed font program stream
	 * @param isSymbolic
	 */
	public PBoxTrueTypeFontProgram(FontBoxFont fontProgram, Boolean isSymbolic) {
		super(fontProgram, TRUE_TYPE_PROGRAM_TYPE);
		this.isSymbolic = isSymbolic;
	}

	/**
	 * @return number of CMap`s
	 */
	@Override
	public Long getnrCmaps() {
		try {
			CmapTable cmap = ((TrueTypeFont) this.fontProgram).getCmap();
			if (cmap != null) {
				int nrCmaps = cmap.getCmaps().length;
				return Long.valueOf(nrCmaps);
			}
		} catch (IOException e) {
			LOGGER.error(e);
		}
		return null;
	}

	@Override
	public Boolean getisSymbolic() {
		return this.isSymbolic;
	}

	@Override
	// TODO : implement me
	public Boolean getcmap30Present() {
		return Boolean.FALSE;
	}
}
