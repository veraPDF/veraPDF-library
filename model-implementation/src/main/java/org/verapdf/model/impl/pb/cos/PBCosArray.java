package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosArray;
import org.verapdf.model.coslayer.CosObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Current class is representation of CosArray interface of abstract model. This
 * class is analogue of COSArray in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosArray extends PBCosObject implements CosArray {

    /** Type name for PBCosArray */
    public static final String COS_ARRAY_TYPE = "CosArray";

    public static final String ELEMENTS = "elements";
    private final int size;

    /**
     * Default constructor
     * @param array pdfbox COSArray
     */
    public PBCosArray(COSArray array) {
        this(array, COS_ARRAY_TYPE);
    }

	/**
	 * Constructor used by child classes
	 *
	 * @param array pdfbox COSArray
	 * @param type type of object
	 */
	public PBCosArray(COSArray array, String type) {
		super(array, type);
		this.size = array.size();
	}

    /**
     * Getter for array size.
     *
     * @return size of array
     */
    @Override
    public Long getsize() {
        return Long.valueOf(this.size);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (link.equals(ELEMENTS)) {
            return this.getElements();
        }
        return super.getLinkedObjects(link);
    }

    /**
     * Get all elements of array.
     *
     * @return elements of array
     */
    private List<CosObject> getElements() {
        List<CosObject> list = new ArrayList<>(this.getsize().intValue());
        for (COSBase base : (COSArray) this.baseObject) {
            if (base != null) {
                list.add(getFromValue(base));
            }
        }
        return Collections.unmodifiableList(list);
    }
}
