package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSNumber;
import org.verapdf.model.coslayer.CosNumber;

import java.util.Locale;

/**
 * Current class is representation of CosNumber interface of abstract model.
 * Methods of this class using in PBCosInteger and PBCosReal.
 * This class is analogue of COSNumber in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 * @see PBCosInteger
 * @see PBCosReal
 */
public abstract class PBCosNumber extends PBCosObject implements CosNumber {

	protected PBCosNumber(COSNumber number) {
		super(number);
	}

	/**
	 * Get the string representing this object
	 */
	@Override
	public String getstringValue() {
		return String.valueOf(((COSNumber) baseObject).doubleValue());
	}

	/**
	 * Get truncated integer value
	 */
	@Override
	public Long getintValue() {
		return Long.valueOf(((COSNumber) baseObject).longValue());
	}

	/**
	 * Get original decimal value
	 */
	@Override
	public Double getrealValue() {
		String value = String.format(Locale.US, "%.5f", ((COSNumber) baseObject).doubleValue());
		return Double.valueOf(value);
	}
}
