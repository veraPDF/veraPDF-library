package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDResources;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.operator.GraphicState;

import java.util.List;

/**
 * Base class for path paint operators which strokes the path
 *
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpStrokePaint extends PBOpPathPaint {

	protected PBOpStrokePaint(List<COSBase> arguments, final GraphicState state,
							  final PDResources resources, final String opType) {
		super(arguments, state, resources, opType);
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
