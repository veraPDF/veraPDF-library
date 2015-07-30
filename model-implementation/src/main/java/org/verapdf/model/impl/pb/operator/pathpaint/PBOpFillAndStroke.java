package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
import org.verapdf.model.baselayer.*;
import org.verapdf.model.factory.colors.ColorSpaceFactory;
import org.verapdf.model.pdlayer.PDColorSpace;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpFillAndStroke extends PBOpPathPaint {

	public PBOpFillAndStroke(List<COSBase> arguments, PDAbstractPattern pattern,
							 org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace pbStrokeColorSpace,
							 org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace pbFillColorSpace) {
		super(arguments, pattern, pbStrokeColorSpace, pbFillColorSpace);
	}

	@Override
	public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
		List<? extends org.verapdf.model.baselayer.Object> list;

		switch (link) {
			case STROKE_CS:
				list = this.getStrokeCS();
				break;
			case FILL_CS:
				list = this.getFillCS();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<PDColorSpace> getFillCS() {
		List<PDColorSpace> list = new ArrayList<>(MAX_NUMBER_OF_COLOR_SPACES);
		PDColorSpace colorSpace = ColorSpaceFactory.getColorSpace(pbFillColorSpace, pattern);
		if (colorSpace != null) {
			list.add(colorSpace);
		}
		return list;
	}

	private List<PDColorSpace> getStrokeCS() {
		List<PDColorSpace> list = new ArrayList<>(MAX_NUMBER_OF_COLOR_SPACES);
		PDColorSpace colorSpace = ColorSpaceFactory.getColorSpace(pbStrokeColorSpace, pattern);
		if (colorSpace != null) {
			list.add(colorSpace);
		}
		return list;
	}
}
