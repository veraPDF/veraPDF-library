package org.verapdf.model.impl.pb.external;

import org.apache.fontbox.FontBoxFont;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.verapdf.model.external.FontProgram;

/**
 * @author Timur Kamalov
 */
public class PBoxFontProgram extends PBoxExternal implements FontProgram {

    public static final String FONT_PROGRAM_TYPE = "FontProgram";

    protected FontBoxFont fontProgram;
    protected PDStream fontProgramStream;

    public PBoxFontProgram(PDStream fontProgramStream) {
        super(FONT_PROGRAM_TYPE);
        this.fontProgramStream = fontProgramStream;
    }

    public PBoxFontProgram(FontBoxFont fontProgram) {
        super(FONT_PROGRAM_TYPE);
        this.fontProgram = fontProgram;
    }

    public PBoxFontProgram(FontBoxFont fontProgram, String type) {
        super(type);
        this.fontProgram = fontProgram;
    }

}