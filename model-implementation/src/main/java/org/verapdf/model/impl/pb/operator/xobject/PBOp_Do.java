package org.verapdf.model.impl.pb.operator.xobject;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.impl.pb.operator.base.PBOperator;
import org.verapdf.model.impl.pb.pd.images.PBoxPDXForm;
import org.verapdf.model.impl.pb.pd.images.PBoxPDXImage;
import org.verapdf.model.impl.pb.pd.images.PBoxPDXObject;
import org.verapdf.model.operator.Op_Do;
import org.verapdf.model.pdlayer.PDXObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Operator which paints the specified XObject
 *
 * @author Timur Kamalov
 */
public class PBOp_Do extends PBOperator implements Op_Do {

	/** Type name for {@code PBOp_Do} */
    public static final String OP_DO_TYPE = "Op_Do";

	/** Name of link to the XObject */
    public static final String X_OBJECT = "xObject";

    private org.apache.pdfbox.pdmodel.graphics.PDXObject pbXObject;

    public PBOp_Do(List<COSBase> arguments,
            org.apache.pdfbox.pdmodel.graphics.PDXObject pbXObject) {
        super(arguments, OP_DO_TYPE);
        this.pbXObject = pbXObject;
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (X_OBJECT.equals(link)) {
            return this.getXObject();
        }
        return super.getLinkedObjects(link);
    }

	private List<PDXObject> getXObject() {
		PDXObject typedPDXObject = PBoxPDXObject.getTypedPDXObject(this.pbXObject);
		if (typedPDXObject != null) {
			List<PDXObject> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(typedPDXObject);
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

}
