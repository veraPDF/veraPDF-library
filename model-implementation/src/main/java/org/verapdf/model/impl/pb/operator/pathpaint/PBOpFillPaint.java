package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.graphics.color.PDColorSpace;
import org.verapdf.model.factory.colors.ColorSpaceFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpFillPaint extends PBOpPathPaint {

	protected PBOpFillPaint(List<COSBase> arguments, PDColorSpace pbStrokeColorSpace, PDColorSpace pbFillColorSpace) {
		super(arguments, pbStrokeColorSpace, pbFillColorSpace);
	}

	@Override
	public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
		List<? extends org.verapdf.model.baselayer.Object> list;

		switch (link) {
			case FILL_CS:
				list = this.getFillCS();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<org.verapdf.model.pdlayer.PDColorSpace> getFillCS() {
		List<org.verapdf.model.pdlayer.PDColorSpace> list = new ArrayList<>(MAX_NUMBER_OF_COLOR_SPACES);
		org.verapdf.model.pdlayer.PDColorSpace colorSpace = ColorSpaceFactory.getColorSpace(pbFillColorSpace);
		if (colorSpace != null) {
			list.add(colorSpace);
		}
		return list;
	}
}
