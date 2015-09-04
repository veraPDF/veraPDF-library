package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.baselayer.Object;

import java.util.List;

/**
 * Base class for path paint operators which fills the path
 *
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpFillPaint extends PBOpPathPaint {

    protected PBOpFillPaint(List<COSBase> arguments, PDAbstractPattern pattern,
            PDColorSpace pbStrokeColorSpace, PDColorSpace pbFillColorSpace,
            final String opType) {
        super(arguments, pattern, pbStrokeColorSpace, pbFillColorSpace, opType);
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        if (FILL_CS.equals(link)) {
            return this.getFillCS();
        }
        return super.getLinkedObjects(link);
    }

}
