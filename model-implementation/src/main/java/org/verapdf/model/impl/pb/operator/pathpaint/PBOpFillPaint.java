package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.factory.operator.GraphicState;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.util.List;

/**
 * Base class for path paint operators which fills the path
 *
 * @author Evgeniy Muravitskiy
 */
public abstract class PBOpFillPaint extends PBOpPathPaint {

    protected PBOpFillPaint(List<COSBase> arguments, final GraphicState state,
            final PDInheritableResources resources, final String opType) {
        super(arguments, state, resources, opType);
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        if (FILL_CS.equals(link)) {
            return this.getFillCS();
        }
        return super.getLinkedObjects(link);
    }

}
