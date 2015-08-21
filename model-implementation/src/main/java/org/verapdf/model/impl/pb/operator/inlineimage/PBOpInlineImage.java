package org.verapdf.model.impl.pb.operator.inlineimage;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.impl.pb.cos.PBCosDict;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpInlineImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for operators, such as BI, ID and EI
 *
 * @author Timur Kamalov
 */
public class PBOpInlineImage extends PBOperator implements OpInlineImage {

    protected PBOpInlineImage(List<COSBase> arguments, final String type) {
        super(arguments, type);
    }

}
