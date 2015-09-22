package org.verapdf.model.impl.pb.operator.inlineimage;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.impl.pb.cos.PBCosDict;
import org.verapdf.model.operator.Op_ID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Operator representing inline image. Described by
 * BI and ID
 *
 * @author Evgeniy Muravitskiy
 */
public class PBOp_ID extends PBOpInlineImage implements Op_ID {

	/** Type name for {@code PBOp_ID} operator */
	public static final String OP_ID_TYPE = "Op_ID";

	/** Name of link to the inline image dictionary */
	public static final String INLINE_IMAGE_DICTIONARY =
			"inlineImageDictionary";

	public PBOp_ID(List<COSBase> arguments) {
		super(arguments, OP_ID_TYPE);
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
		if (!this.arguments.isEmpty()) {
			COSBase dict = this.arguments
					.get(this.arguments.size() - 1);
			if (dict instanceof COSDictionary) {
				List<CosDict> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				list.add(new PBCosDict((COSDictionary) dict));
				return Collections.unmodifiableList(list);
			}
		}
		return Collections.emptyList();
	}
}
