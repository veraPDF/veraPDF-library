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
public abstract class PBOpStrokePaint extends PBOpPathPaint{

	protected PBOpStrokePaint(List<COSBase> arguments, PDAbstractPattern pattern,
							  PDColorSpace pbStrokeColorSpace, PDColorSpace pbFillColorSpace) {
		super(arguments, pattern, pbStrokeColorSpace, pbFillColorSpace);
	}

	@Override
	public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
		List<? extends org.verapdf.model.baselayer.Object> list;

		switch (link) {
			case STROKE_CS:
				list = this.getStrokeCS();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<org.verapdf.model.pdlayer.PDColorSpace> getStrokeCS() {
		List<org.verapdf.model.pdlayer.PDColorSpace> list = new ArrayList<>(MAX_NUMBER_OF_COLOR_SPACES);
		org.verapdf.model.pdlayer.PDColorSpace colorSpace = ColorSpaceFactory.getColorSpace(pbStrokeColorSpace);
		if (colorSpace != null) {
			list.add(colorSpace);
		}
		return list;
	}
}
