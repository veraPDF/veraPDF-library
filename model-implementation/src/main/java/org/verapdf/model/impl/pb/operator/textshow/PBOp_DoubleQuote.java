package org.verapdf.model.impl.pb.operator.textshow;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSNumber;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosReal;
import org.verapdf.model.impl.pb.cos.PBCosReal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBOp_DoubleQuote extends PBOpStringTextShow {

	public static final String OP_DOUBLIE_QUOTE_TYPE = "Op_DoubleQuote";

	public static final String WORD_SPACING = "wordSpacing";
	public static final String CHARACTER_SPACING = "characterSpacing";
	public static final Integer COUNT_OF_OPERATOR_OPERANDS = Integer.valueOf(3);

	public PBOp_DoubleQuote(List<COSBase> arguments) {
		super(arguments);
		setType(OP_DOUBLIE_QUOTE_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		List<? extends Object> list;

		switch (link) {
			case WORD_SPACING:
				list = getWordSpacing();
				break;
			case CHARACTER_SPACING:
				list = getCharacterSpacing();
				break;
			default:
				list = super.getLinkedObjects(link);
				break;
		}

		return list;
	}

	private List<CosReal> getWordSpacing() {
		return getSpecialReal(Integer.valueOf(0));
	}

	private List<CosReal> getCharacterSpacing() {
		return getSpecialReal(Integer.valueOf(1));
	}

	private List<CosReal> getSpecialReal(Integer operandNumber) {
		List<CosReal> characterSpacing = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		if (this.arguments.size() >= COUNT_OF_OPERATOR_OPERANDS) {
			Integer index = Integer.valueOf(this.arguments.size() - COUNT_OF_OPERATOR_OPERANDS + operandNumber);
			COSBase base = this.arguments.get(index);
			if (base instanceof COSNumber) {
				characterSpacing.add(new PBCosReal((COSNumber) base));
			}
		}
		return characterSpacing;
	}
}
