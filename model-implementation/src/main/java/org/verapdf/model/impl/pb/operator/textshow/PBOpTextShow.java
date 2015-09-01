package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.preflight.font.container.FontContainer;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.font.FontFactory;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpTextShow;
import org.verapdf.model.pdlayer.PDColorSpace;
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

	/** Name of link to the used font */
    public static final String FONT = "font";
	/** Name of link to the used glyphs */
    public static final String USED_GLYPHS = "usedGlyphs";
    /** Name of link to the fill color space */
    public static final String FILL_COLOR_SPACE = "fillCS";
    /** Name of link to the stroke color space */
    public static final String STROKE_COLOR_SPACE = "strokeCS";

    protected final org.apache.pdfbox.pdmodel.font.PDFont pdfBoxFont;

    protected PBOpTextShow(List<COSBase> arguments,
            org.apache.pdfbox.pdmodel.font.PDFont font, final String opType) {
        super(arguments, opType);
        this.pdfBoxFont = font;
    }

	@Override
	public List<? extends Object> getLinkedObjects(
			String link) {
		switch (link) {
			case FONT:
				return this.getFont();
			case USED_GLYPHS:
				return this.getUsedGlyphs();
            case FILL_COLOR_SPACE:
                return this.getFillColorSpace();
            case STROKE_COLOR_SPACE:
                return this.getStrokeColorSpace();
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

    private List<PDColorSpace> getFillColorSpace() {
        return new ArrayList<>();
    }

    private List<PDColorSpace> getStrokeColorSpace() {
        return new ArrayList<>();
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
