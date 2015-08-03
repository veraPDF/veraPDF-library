package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.font.FontFactory;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpTextShow;
import org.verapdf.model.pdlayer.PDFont;

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
            return getUsedGlyphs();
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

    private static List<? extends Object> getUsedGlyphs() {
        return new ArrayList<>();
    }

}
