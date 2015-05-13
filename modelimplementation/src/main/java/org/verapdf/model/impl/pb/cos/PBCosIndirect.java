package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSObject;
import org.verapdf.model.factory.cos.PBFactory;
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

    public final static String DIRECT_OBJECT = "directObject";

    private Boolean isSpacingComplyPDFA;

    public PBCosIndirect(COSBase directObject, Boolean isSpacingComplyPDFA) {
        super(directObject);
        this.isSpacingComplyPDFA = isSpacingComplyPDFA;
        setType("CosIndirect");
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(String link) {
        List<? extends org.verapdf.model.baselayer.Object> list;

        switch (link) {
            case DIRECT_OBJECT:
                list = getdirectObject();
                break;
            default:
                throw new IllegalArgumentException("Unknown link " + link + " for " + get_type());
        }

        return list;
    }

    /** Get the direct contents of the indirect object
     */
    protected List<CosObject> getdirectObject() {
        List<CosObject> list = new ArrayList<>();
        COSBase base = baseObject instanceof COSObject ? ((COSObject) baseObject).getObject() : baseObject;
        list.add(PBFactory.generateCosObject(base));
        return list;
    }

    /**  true if the words 'obj' and 'endobj' are surrounded by the correct spacings according to PDF/A standard
     */
    @Override
    public Boolean getspacingComplyPDFA() {
        return isSpacingComplyPDFA;
    }

    @Override
    public boolean compareTo(java.lang.Object object) {
        boolean isEquals;

        if (object instanceof COSObject && !(baseObject instanceof COSObject)) {
            isEquals = ((COSObject) object).getObject().equals(baseObject);
        } else if (!(object instanceof COSObject) && baseObject instanceof COSObject) {
            isEquals = object.equals(((COSObject) baseObject).getObject());
        } else {
            isEquals = super.compareTo(object);
        }

        return isEquals;
    }
}
