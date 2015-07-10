package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpPathPaint;
import org.verapdf.model.pdlayer.PDColorSpace;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBOpPathPaint extends PBOperator implements OpPathPaint {

	public static final String STROKE_CS = "strokeCS";
	public static final String FILL_CS = "fillCS";
	public static final Integer MAX_NUMBER_OF_COLOR_SPACES = Integer.valueOf(1);

	protected org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace pbStrokeColorSpace;
	protected org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace pbFillColorSpace;

    public PBOpPathPaint(List<COSBase> arguments,
						 org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace pbStrokeColorSpace,
						 org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace pbFillColorSpace) {
        super(arguments);
		this.pbStrokeColorSpace = pbStrokeColorSpace;
		this.pbFillColorSpace = pbFillColorSpace;
    }

}
