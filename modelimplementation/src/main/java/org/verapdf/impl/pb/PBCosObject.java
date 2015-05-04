package org.verapdf.impl.pb;

import org.apache.pdfbox.cos.COSBase;
import org.verapdf.tools.IDGenerator;
import org.verapdf.model.coslayer.CosObject;

/**
 * Created by Evgeniy Muravitskiy on 4/27/15.
 * <p>
 *     Current class is representation of CosObject interface of abstract model.
 *     This class is analogue of COSBase in pdfbox.
 * </p>
 */
public class PBCosObject implements CosObject{

    protected COSBase baseObject;
    private String id = IDGenerator.generateID();

    public PBCosObject(COSBase baseObject) {
        this.baseObject = baseObject;
    }

    /** Get type of current object
     */
    @Override
    public String get_type() {
        final String clas = this.getClass() + "";
        int index = clas.lastIndexOf((int) '$');
        return index < 1 ? clas : clas.substring(0, index - 1);
    }

    /** Get personal id of current object
     */
    @Override
    public String get_id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof COSBase)
            return baseObject.equals(o);
        else
            return o instanceof CosObject && o.equals(baseObject);
    }
}
