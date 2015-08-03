package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.factory.colors.ColorSpaceFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpFillPaint extends PBOpPathPaint {

    protected PBOpFillPaint(List<COSBase> arguments, PDAbstractPattern pattern,
            PDColorSpace pbStrokeColorSpace, PDColorSpace pbFillColorSpace) {
        super(arguments, pattern, pbStrokeColorSpace, pbFillColorSpace);
    }

    protected PBOpFillPaint(List<COSBase> arguments, PDAbstractPattern pattern,
            PDColorSpace pbStrokeColorSpace, PDColorSpace pbFillColorSpace,
            final String opType) {
        super(arguments, pattern, pbStrokeColorSpace, pbFillColorSpace, opType);
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(
            String link) {
        if (FILL_CS.equals(link)) {
            return this.getFillCS();
        }

        return super.getLinkedObjects(link);
    }

    private List<org.verapdf.model.pdlayer.PDColorSpace> getFillCS() {
        List<org.verapdf.model.pdlayer.PDColorSpace> list = new ArrayList<>(
                MAX_NUMBER_OF_COLOR_SPACES);
        org.verapdf.model.pdlayer.PDColorSpace colorSpace = ColorSpaceFactory
                .getColorSpace(pbFillColorSpace, pattern);
        if (colorSpace != null) {
            list.add(colorSpace);
        }
        return list;
    }
}
