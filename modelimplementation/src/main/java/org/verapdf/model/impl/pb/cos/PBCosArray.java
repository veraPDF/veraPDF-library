package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosArray;
import org.verapdf.model.coslayer.CosObject;

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

    public final static String ELEMENTS = "elements";

    public PBCosArray(COSArray array) {
        super(array);
        setType("CosArray");
    }

    /**
     * Getter for array size.
     * @return size of array
     */
    @Override
    public Integer getsize() {
        return ((COSArray) baseObject).size();
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (link.equals(ELEMENTS)) {
            return this.getElements();
        }
        else {
            return super.getLinkedObjects(link);
        }
    }
    /**
     * Get all elements of array.
     * @return elements of array
     */
    private List<CosObject> getElements() {
        List<CosObject> list = new ArrayList<>(this.getsize());
        for (COSBase base : ((COSArray) baseObject)) {
            list.add(getFromValue(base));
        }
        return list;
    }
}
