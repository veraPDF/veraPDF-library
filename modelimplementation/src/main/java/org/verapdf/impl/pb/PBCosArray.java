package org.verapdf.impl.pb;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.verapdf.factory.cos.PBFactory;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 4/27/15.
 * <p>
 *     Current class is representation of CosArray interface of abstract model.
 *     This class is analogue of COSArray in pdfbox.
 * </p>
 */
public class PBCosArray extends PBCosObject implements CosArray {

    private final static String ELEMENTS = "elements";

    public PBCosArray(COSArray array) {
        super(array);
    }

    /**
     * Getter for array size.
     * @return size of array
     */
    @Override
    public Integer getsize() {
        return ((COSArray)baseObject).size();
    }

    @Override
    public List<org.verapdf.model.baselayer.Object> getLinkedObjects(String s) {
        if (s.equals(ELEMENTS))
            return getelements();
        throw new IllegalArgumentException("Unknown link " + s + " for " + get_type());
    }
    /**
     * Get all elements of array.
     * @return elements of array
     */
    protected List<Object> getelements() {
        List<Object> list = new ArrayList<>(getsize());
        for (COSBase base : ((COSArray)baseObject))
            list.add(PBFactory.generateCosObject(base));
        return list;
    }
}
