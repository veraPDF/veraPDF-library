package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpPathPaint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBOpPathPaint extends PBOperator implements OpPathPaint {

    public static final String STROKE_CS = "strokeCS";
    public static final String FILL_CS = "fillCS";
    public static final int MAX_NUMBER_OF_COLOR_SPACES = 1;

    protected PDColorSpace pbStrokeColorSpace;
    protected PDColorSpace pbFillColorSpace;
    protected PDAbstractPattern pattern;

    public PBOpPathPaint(List<COSBase> arguments, PDAbstractPattern pattern,
			PDColorSpace pbStrokeColorSpace, PDColorSpace pbFillColorSpace,
			final String opType) {
        super(arguments, opType);
        this.pbStrokeColorSpace = pbStrokeColorSpace;
        this.pbFillColorSpace = pbFillColorSpace;
        this.pattern = pattern;
    }

	protected List<org.verapdf.model.pdlayer.PDColorSpace> getFillCS() {
		return this.getColorSpace(pbFillColorSpace);
	}

	protected List<org.verapdf.model.pdlayer.PDColorSpace> getStrokeCS() {
		return this.getColorSpace(pbStrokeColorSpace);
	}

	protected List<org.verapdf.model.pdlayer.PDColorSpace> getColorSpace(
			PDColorSpace colorSpace) {
		List<org.verapdf.model.pdlayer.PDColorSpace> list =
				new ArrayList<>(MAX_NUMBER_OF_COLOR_SPACES);
		org.verapdf.model.pdlayer.PDColorSpace veraColorSpace =
				ColorSpaceFactory.getColorSpace(colorSpace, pattern);
		if (veraColorSpace != null) {
			list.add(veraColorSpace);
		}
		return list;
	}

}
