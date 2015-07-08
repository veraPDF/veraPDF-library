package org.verapdf.model.impl.pb.operator.pathpaint;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpPathPaint;

import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBOpPathPaint extends PBOperator implements OpPathPaint {

    public PBOpPathPaint(List<COSBase> arguments) {
        super(arguments);
    }

}
