package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.preflight.font.container.FontContainer;
import org.verapdf.model.factory.font.FontFactory;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpTextShow;
import org.verapdf.model.pdlayer.PDFont;
import org.verapdf.model.tools.FontHelper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all text show operators
 *
 * @author Timur Kamalov
 */
public abstract class PBOpTextShow extends PBOperator implements OpTextShow {

    private static final Logger LOGGER = Logger.getLogger(PBOpTextShow.class);

	/** Link name of used font for OpTextShow */
    public static final String FONT = "font";
	/** Link name of used glyphs for OpTextShow */
    public static final String USED_GLYPHS = "usedGlyphs";

    protected final org.apache.pdfbox.pdmodel.font.PDFont pdfBoxFont;

    protected PBOpTextShow(List<COSBase> arguments,
            org.apache.pdfbox.pdmodel.font.PDFont font, final String opType) {
        super(arguments, opType);
        this.pdfBoxFont = font;
    }

	@Override
	public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(
			String link) {
		switch (link) {
			case FONT:
				return this.getFont();
			case USED_GLYPHS:
				return this.getUsedGlyphs();
			default:
				return super.getLinkedObjects(link);
		}
	}

    private List<PDFont> getFont() {
        List<PDFont> result = new ArrayList<>();
        PDFont font = FontFactory.parseFont(pdfBoxFont);
        result.add(font);
        return result;
    }

    private List<PBGlyph> getUsedGlyphs() {
        List<PBGlyph> res = new ArrayList<>();
        FontContainer fontContainer = FontHelper.getFontContainer(pdfBoxFont);
        List<byte[]> strings = getStrings();
        for (byte[] string : strings) {
            try (InputStream inputStream = new ByteArrayInputStream(string)) {
                while (inputStream.available() > 0) {
                    int code = pdfBoxFont.readCode(inputStream);
                    Boolean glyphPresent = fontContainer.hasGlyph(code);
                    Boolean widthsConsistent = checkWidths(code);
                    res.add(new PBGlyph(glyphPresent, widthsConsistent,
										pdfBoxFont.getName(), code));
                }
            } catch (IOException e) {
                LOGGER.error("Error processing text show operator's string argument : "
						+ new String(string));
                LOGGER.info(e);
            }
        }
        return res;
    }

    private Boolean checkWidths(int glyphCode) throws IOException {
        float expectedWidth = pdfBoxFont.getWidth(glyphCode);
        float foundWidth = pdfBoxFont.getWidthFromFont(glyphCode);
        // consistent is defined to be a difference of no more than 1/1000 unit.
        if (Math.abs(foundWidth - expectedWidth) > 1) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    private List<byte[]> getStrings() {
        List<byte[]> res = new ArrayList<>();
        COSBase arg = this.arguments.get(0);
        if (arg instanceof COSArray) {
            for (COSBase element : (COSArray) arg) {
                if (element instanceof COSString) {
                    res.add(((COSString) element).getBytes());
                }
            }
        } else {
            if (arg instanceof COSString) {
                res.add(((COSString) arg).getBytes());
            }
        }
        return res;
    }

}
