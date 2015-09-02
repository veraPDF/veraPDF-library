package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.apache.pdfbox.preflight.font.container.FontContainer;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.factory.font.FontFactory;
import org.verapdf.model.factory.operator.GraphicState;
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

    protected final GraphicState state;

    protected PBOpTextShow(List<COSBase> arguments,
            GraphicState state, final String opType) {
        super(arguments, opType);
        this.state = state;
    }

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
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
        List<PDFont> result = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        PDFont font = FontFactory.parseFont(this.state.getFont());
		if (font != null) {
			result.add(font);
		}
        return result;
    }

    private List<PBGlyph> getUsedGlyphs() {
        List<PBGlyph> res = new ArrayList<>();
		org.apache.pdfbox.pdmodel.font.PDFont font = this.state.getFont();
		FontContainer fontContainer = FontHelper.getFontContainer(font);
        List<byte[]> strings = getStrings(this.arguments);
        for (byte[] string : strings) {
            try (InputStream inputStream = new ByteArrayInputStream(string)) {
                while (inputStream.available() > 0) {
                    int code = font.readCode(inputStream);
                    Boolean glyphPresent = fontContainer.hasGlyph(code);
                    Boolean widthsConsistent = checkWidths(code, font);
                    res.add(new PBGlyph(glyphPresent, widthsConsistent,
										font.getName(), code));
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
		return getColorSpace(this.state.getFillColorSpace(), this.state.getPattern());
    }

	private List<PDColorSpace> getStrokeColorSpace() {
        return getColorSpace(this.state.getStrokeColorSpace(), this.state.getPattern());
    }

	private static List<PDColorSpace> getColorSpace(
			org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace fillColorSpace,
			PDAbstractPattern pattern) {
		List<PDColorSpace> colorSpaces = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		PDColorSpace colorSpace = ColorSpaceFactory.getColorSpace(
				fillColorSpace, pattern);
		if (colorSpace != null) {
			colorSpaces.add(colorSpace);
		}
		return colorSpaces;
	}

    private static Boolean checkWidths(int glyphCode,
								org.apache.pdfbox.pdmodel.font.PDFont font) throws IOException {
		float expectedWidth = font.getWidth(glyphCode);
        float foundWidth = font.getWidthFromFont(glyphCode);
        // consistent is defined to be a difference of no more than 1/1000 unit.
		return Math.abs(foundWidth - expectedWidth) > 1 ? Boolean.FALSE : Boolean.TRUE;
    }

    private static List<byte[]> getStrings(List<COSBase> arguments) {
        List<byte[]> res = new ArrayList<>();
		if (!arguments.isEmpty()) {
			COSBase arg = arguments.get(0);
			if (arg instanceof COSArray) {
				addArrayElements(res, (COSArray) arg);
			} else {
				if (arg instanceof COSString) {
					res.add(((COSString) arg).getBytes());
				}
			}
		}
        return res;
    }

	private static void addArrayElements(List<byte[]> res, COSArray arg) {
		for (COSBase element : arg) {
			if (element instanceof COSString) {
				res.add(((COSString) element).getBytes());
			}
		}
	}

}
