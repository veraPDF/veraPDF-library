package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSObject;
import org.verapdf.model.coslayer.CosIndirect;
import org.verapdf.model.coslayer.CosObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Current class is representation of CosIndirect interface of abstract model.
 * This class is analogue of COSObject in pdfbox.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosIndirect extends PBCosObject implements CosIndirect {

    public static final String DIRECT_OBJECT = "directObject";
    /** Type name for PBCosBool */
    public static final String COS_INDIRECT_TYPE = "CosIndirect";

    private final boolean isSpacingPDFACompliant;

    // private List<CosObject> directObjects;

    public PBCosIndirect(COSBase indirectObject) {
        super(indirectObject, COS_INDIRECT_TYPE);
        this.isSpacingPDFACompliant = getspacingCompliesPDFA(indirectObject);
        /**
         * FIXME: Why do the COSDocment tests go dive into a stack overflow when
         * I uncomment below?:
         *
         * this.directObjects = parseDirectObject(indirectObject instanceof
         * COSObject ? ((COSObject) indirectObject).getObject() : indirectObject);
         */
    }

    @Override
    public List<? extends org.verapdf.model.baselayer.Object> getLinkedObjects(
            String link) {

        if (DIRECT_OBJECT.equals(link)) {
            return parseDirectObject();
        }
        return super.getLinkedObjects(link);

    }

    /**
     * Get the direct contents of the indirect object
     */
    private List<CosObject> parseDirectObject() {
        List<CosObject> list = new ArrayList<>();
        COSBase base = ((COSObject) baseObject).getObject();
        list.add(base != null ? getFromValue(base) : PBCosNull.NULL);
        return Collections.unmodifiableList(list);
    }

    /**
     * true if the words 'obj' and 'endobj' are surrounded by the correct
     * spacings according to PDF/A standard
     */
    @Override
    public Boolean getspacingCompliesPDFA() {
        return Boolean.valueOf(this.isSpacingPDFACompliant);
    }

    /**
     * Get the direct contents of the indirect object
     */
    private static boolean getspacingCompliesPDFA(COSBase base) {
        return ((COSObject) base).isEndOfObjectComplyPDFA().booleanValue()
                && ((COSObject) base).isHeaderFormatComplyPDFA().booleanValue()
                && ((COSObject) base).isHeaderOfObjectComplyPDFA()
                        .booleanValue();
    }
}
