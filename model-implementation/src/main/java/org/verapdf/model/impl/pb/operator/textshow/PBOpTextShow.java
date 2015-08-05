package org.verapdf.model.impl.pb.operator.textshow;

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
 * @author Timur Kamalov
 */
public abstract class PBOpTextShow extends PBOperator implements OpTextShow {

    public static final String FONT = "font";
    public static final String USED_GLYPHS = "usedGlyphs";

    protected final org.apache.pdfbox.pdmodel.font.PDFont pdfBoxFont;

    protected PBOpTextShow(List<COSBase> arguments,
            org.apache.pdfbox.pdmodel.font.PDFont font) {
        super(arguments);
        this.pdfBoxFont = font;
    }

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
        FontContainer fontContainer = FontHelper.getFontContainer(pdfBoxFont);
        List<byte[]> strings = getStrings();
        for (byte[] string : strings) {
            try (InputStream inputStream = new ByteArrayInputStream(string)) {
                while (inputStream.available() > 0) {
                    int code = pdfBoxFont.readCode(inputStream);
                    fontContainer.hasGlyph(code);
                }
            } catch (IOException e) {
                //TODO : process exception
            }

        }

        return new ArrayList<>();
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
