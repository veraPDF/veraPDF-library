package org.verapdf.model.impl.pb.operator.textshow;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.verapdf.model.coslayer.CosString;
import org.verapdf.model.impl.pb.cos.PBCosString;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpStringTextShow extends PBOpTextShow {

    public static final String SHOW_STRING = "showString";

    protected PBOpStringTextShow(List<COSBase> arguments, PDFont font) {
        super(arguments, font);
    }

    protected PBOpStringTextShow(List<COSBase> arguments, PDFont font,
            final String opType) {
        super(arguments, font, opType);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(
            String link) {
        if (SHOW_STRING.equals(link)) {
            return this.getShowString();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosString> getShowString() {
        List<CosString> string = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        COSBase base = !this.arguments.isEmpty() ? this.arguments
                .get(this.arguments.size() - 1) : null;
        if (base instanceof COSString) {
            string.add(new PBCosString((COSString) base));
        }
        return string;
    }
}
