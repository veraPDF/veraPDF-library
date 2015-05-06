package org.verapdf.impl.pb;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSObject;
import org.verapdf.factory.cos.PBFactory;
import org.verapdf.model.baselayer.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosIndirect;
import org.verapdf.model.coslayer.CosObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 4/28/15.
 * <p>
 *     Current class is representation of CosIndirect interface of abstract model.
 *     This class is analogue of COSObject in pdfbox.
 * </p>
 */
public class PBCosIndirect extends PBCosObject implements CosIndirect {

    private final static String DIRECT_OBJECT = "directObject";
    private boolean isSpacingComplyPDFA;

    public PBCosIndirect(COSBase directObject,boolean isSpacingComplyPDFA) {
        super(directObject);
        this.isSpacingComplyPDFA = isSpacingComplyPDFA;
    }

    @Override
    public List<org.verapdf.model.baselayer.Object> getLinkedObjects(String s) {
        List<org.verapdf.model.baselayer.Object> list;
        switch (s) {
            case DIRECT_OBJECT:
                list = getdirectObject();
                break;
            default:
                throw new IllegalArgumentException("Unknown link " + s + " for " + get_type());
        }

        return list;
    }

    /** Get the direct contents of the indirect object
     */
    protected List<org.verapdf.model.baselayer.Object> getdirectObject() {
        List<Object> list = new ArrayList<>();
        COSBase base = baseObject instanceof COSObject ? ((COSObject) baseObject).getObject() : baseObject;
        list.add(PBFactory.generateCosObject(base));
        return list;
    }

    /**  true if the words 'obj' and 'endobj' are surrounded by the correct spacings accoring to PDF/A standard
     */
    @Override
    public Boolean getspacingComplyPDFA() {
        return isSpacingComplyPDFA;
    }

    @Override
    public boolean equals(Object o) {
        boolean isEquals;

        if (o instanceof COSObject && baseObject instanceof COSDictionary)
            isEquals = ((COSObject) o).getObject().equals(baseObject);
        else if (o instanceof COSDictionary && baseObject instanceof COSObject)
            isEquals = o.equals(((COSObject) baseObject).getObject());
        else
            isEquals = super.equals(o);

        return isEquals;
    }
}
