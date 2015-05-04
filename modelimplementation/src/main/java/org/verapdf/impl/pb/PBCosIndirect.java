package org.verapdf.impl.pb;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSObject;
import org.verapdf.factory.cos.PBFactory;
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

    private boolean isSpacingComplyPDFA;

    public PBCosIndirect(COSBase directObject,boolean isSpacingComplyPDFA) {
        super(directObject);
        this.isSpacingComplyPDFA = isSpacingComplyPDFA;
    }

    /** Get the direct contents of the indirect object
     */
    @Override
    public List<CosObject> getdirectObject() {
        List<CosObject> list = new ArrayList<CosObject>();
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
        if (o instanceof COSObject && baseObject instanceof COSDictionary)
            return ((COSObject) o).getObject().equals(baseObject);
        else if (o instanceof COSDictionary && baseObject instanceof COSObject)
            return o.equals(((COSObject) baseObject).getObject());
        else
            return super.equals(o);
    }
}
