package org.verapdf.model.impl.pb.operator.markedcontent;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosName;
import org.verapdf.model.impl.pb.cos.PBCosDict;
import org.verapdf.model.impl.pb.cos.PBCosName;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.operator.OpMarkedContent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Timur Kamalov
 */
public abstract class PBOpMarkedContent extends PBOperator implements OpMarkedContent {

	public static final String TAG = "tag";
	public static final String PROPERTIES = "properties";

	public PBOpMarkedContent(List<COSBase> arguments) {
        super(arguments);
    }

    protected List<CosName> getTag() {
        List<CosName> list = new ArrayList<>();
        if (!arguments.isEmpty() && arguments.get(0) instanceof COSName) {
            list.add(new PBCosName((COSName) arguments.get(0)));
        }
        return list;
    }

    protected List<CosDict> getPropertiesDict() {
        List<CosDict> list = new ArrayList<>();
        if (arguments.size() > 1 && arguments.get(1) instanceof COSDictionary) {
            list.add(new PBCosDict((COSDictionary) arguments.get(1)));
        }
        return list;
    }

}
