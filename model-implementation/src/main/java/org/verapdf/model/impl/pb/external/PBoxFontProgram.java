package org.verapdf.model.impl.pb.external;

import org.apache.fontbox.FontBoxFont;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.verapdf.model.external.FontProgram;

/**
 * @author Timur Kamalov
 */
public class PBoxFontProgram extends PBoxExternal implements FontProgram {

	/** Type name for {@code PBoxFontProgram} */
    public static final String FONT_PROGRAM_TYPE = "FontProgram";

    protected FontBoxFont fontProgram;
    protected PDStream fontProgramStream;

	/**
	 * Default constructor defined by not processed stream
	 *
	 * @param fontProgramStream font program stream
	 */
    public PBoxFontProgram(PDStream fontProgramStream) {
        super(FONT_PROGRAM_TYPE);
        this.fontProgramStream = fontProgramStream;
    }

	/**
	 * Default constructor defined by processed stream
	 * and represented by {@link FontBoxFont}
	 *
	 * @param fontProgram processed font program stream
	 */
    public PBoxFontProgram(FontBoxFont fontProgram) {
		this(fontProgram, FONT_PROGRAM_TYPE);
    }

	/**
	 * Constructor used by child classes
	 *
	 * @param fontProgram processed font program stream
	 * @param type type of child
	 */
    public PBoxFontProgram(FontBoxFont fontProgram, String type) {
        super(type);
        this.fontProgram = fontProgram;
    }

}