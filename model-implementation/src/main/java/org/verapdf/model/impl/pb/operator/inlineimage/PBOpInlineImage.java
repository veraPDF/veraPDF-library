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
 * Operator representing inline image. Described by
 * BI, ID and EI operators
 *
 * @author Timur Kamalov
 */
public class PBOpInlineImage extends PBOperator implements OpInlineImage {

	/** Type name for {@code PBOpInlineImage} */
    public static final String OP_INLINE_IMAGE_TYPE = "OpInlineImage";

	/** Name of link to the inline image dictionary */
    public static final String INLINE_IMAGE_DICTIONARY = "inlineImageDictionary";

    public PBOpInlineImage(List<COSBase> arguments) {
        super(arguments, OP_INLINE_IMAGE_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(
            String link) {
        if (INLINE_IMAGE_DICTIONARY.equals(link)) {
            return this.getInlineImageDictionary();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosDict> getInlineImageDictionary() {
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
