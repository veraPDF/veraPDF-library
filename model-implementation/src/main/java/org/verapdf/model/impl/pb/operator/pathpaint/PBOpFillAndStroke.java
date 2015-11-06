package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.util.List;

/**
 * Base class for path paint operators which strokes and fills path
 *
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpFillAndStroke extends PBOpPathPaint {

	protected PBOpFillAndStroke(List<COSBase> arguments, final GraphicState state,
			final PDInheritableResources resources, final String opType) {
		super(arguments, state, resources, opType);
	}

	@Override
	public List<? extends Object> getLinkedObjects(
			String link) {
		switch (link) {
			case STROKE_CS:
				return this.getStrokeCS();
			case FILL_CS:
				return this.getFillCS();
			default:
				return super.getLinkedObjects(link);
		}
	}

}
