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
 * Base class for marked content operators
 *
 * @author Timur Kamalov
 */
public abstract class PBOpMarkedContent extends PBOperator implements
        OpMarkedContent {

    public static final String TAG = "tag";
    public static final String PROPERTIES = "properties";

    public PBOpMarkedContent(List<COSBase> arguments, final String opType) {
        super(arguments, opType);
    }

    protected List<CosName> getTag() {
        List<CosName> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        if (this.arguments.size() > 1) {
			COSBase name = this.arguments
					.get(this.arguments.size() - 2);
			if (name instanceof COSName) {
				list.add(new PBCosName((COSName) name));
			}
        }
        return list;
    }

    protected List<CosDict> getPropertiesDict() {
        List<CosDict> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        if (!this.arguments.isEmpty()) {
			COSBase dict = this.arguments
					.get(this.arguments.size() - 1);
			if (dict instanceof COSDictionary) {
				list.add(new PBCosDict((COSDictionary) dict));
			}
        }
        return list;
    }

}
