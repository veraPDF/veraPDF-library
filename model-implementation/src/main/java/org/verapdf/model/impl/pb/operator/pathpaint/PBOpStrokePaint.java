package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.baselayer.Object;

import java.util.List;

/**
 * Base class for path paint operators which strokes the path
 *
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpStrokePaint extends PBOpPathPaint {

    protected PBOpStrokePaint(List<COSBase> arguments, PDAbstractPattern pattern,
			PDColorSpace pbStrokeColorSpace, PDColorSpace pbFillColorSpace,
			final String opType) {
        super(arguments, pattern, pbStrokeColorSpace, pbFillColorSpace, opType);
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        if (STROKE_CS.equals(link)) {
            return this.getStrokeCS();
        }

        return super.getLinkedObjects(link);
    }

}
