package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpPathPaint;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBOpPathPaint extends PBOperator implements OpPathPaint {

    public static final String STROKE_CS = "strokeCS";
    public static final String FILL_CS = "fillCS";
    public static final int MAX_NUMBER_OF_COLOR_SPACES = 1;

    protected org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace pbStrokeColorSpace;
    protected org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace pbFillColorSpace;
    protected PDAbstractPattern pattern;

    public PBOpPathPaint(List<COSBase> arguments, PDAbstractPattern pattern,
            PDColorSpace pbStrokeColorSpace, PDColorSpace pbFillColorSpace) {
        super(arguments);
        this.pbStrokeColorSpace = pbStrokeColorSpace;
        this.pbFillColorSpace = pbFillColorSpace;
        this.pattern = pattern;
    }

    public PBOpPathPaint(List<COSBase> arguments, PDAbstractPattern pattern,
            PDColorSpace pbStrokeColorSpace, PDColorSpace pbFillColorSpace, final String opType) {
        super(arguments, opType);
        this.pbStrokeColorSpace = pbStrokeColorSpace;
        this.pbFillColorSpace = pbFillColorSpace;
        this.pattern = pattern;
    }

}
