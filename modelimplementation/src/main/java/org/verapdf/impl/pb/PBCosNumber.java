package org.verapdf.impl.pb;

import org.apache.pdfbox.cos.COSNumber;
import org.verapdf.model.coslayer.CosNumber;

import java.math.BigDecimal;

/**
 * Created by Evgeniy Muravitskiy on 4/27/15.
 * <p>
 *     Current class is representation of CosNumber interface of abstract model.
 *     Methods of this class using in PBCosInteger and PBCosReal.
 *     This class is analogue of COSNumber in pdfbox.
 * </p>
 *
 * @see org.verapdf.impl.pb.PBCosInteger
 * @see org.verapdf.impl.pb.PBCosReal
 */
public abstract class PBCosNumber extends PBCosObject implements CosNumber {

    protected PBCosNumber(COSNumber number) {
        super(number);
    }

    /** Get the string representing this object
     */
    @Override
    public String getstringValue() {
        return ((COSNumber)baseObject).doubleValue() + "";
    }

    /** Get truncated integer value
     */
    @Override
    public Integer getintValue() {
        return ((COSNumber)baseObject).intValue();
    }

    /** Get original decimal value
     */
    @Override
    public BigDecimal getrealValue() {
        return new BigDecimal(((COSNumber)baseObject).doubleValue());
    }
}
