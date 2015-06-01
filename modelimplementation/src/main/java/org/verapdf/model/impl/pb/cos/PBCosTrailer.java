package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSString;
import org.verapdf.model.baselayer.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosIndirect;
import org.verapdf.model.coslayer.CosTrailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Trailer of the document.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosTrailer extends PBCosDict implements CosTrailer {

    private final String CATALOG = "Catalog";
    /** if document is linearized its must have dictionary of linearization
     */
    private COSDictionary linearizedDictionary = null;
    /** if document is linearized first trailer is differ from last
     */
    private CosTrailer firstTrailer = null;
    /** length of the document
     */
    private Long length = null;

    public PBCosTrailer(COSDictionary pdfBoxObject) {
        super(pdfBoxObject);
        setType("CosTrailer");
    }

    public PBCosTrailer(COSDictionary mainTrailer, COSDictionary firstTrailer, COSDictionary linearizedDictionary, Long length) {
        super(mainTrailer);
        this.firstTrailer = new PBCosTrailer(firstTrailer);
        this.linearizedDictionary = linearizedDictionary;
        this.length = length;
    }

    /**
     * @return first part of ID if its present
     */
    public String getid1() {
        return getPartOfID(0);
    }

    /**
     * @return second part of ID if its present
     */
    public String getid2() {
        return getPartOfID(1);
    }

    private String getPartOfID(int index) {
        COSArray ids = (COSArray) ((COSDictionary) baseObject).getItem("ID");

        if (ids != null && ids.size() > index) {
            if (ids.get(index) instanceof COSString) {
                return new String(((COSString) ids.get(index)).getBytes());
            } else {
                // TODO : discuss about this case
                throw new IllegalArgumentException("ID not conforming defined type for ID.");
            }
        } else {
            return null;
        }
    }

    /**
     * @return true if the current document is linearized
     */
    // TODO : need to support of this feature
    public Boolean getisLinearized() {
        return !(baseObject == firstTrailer || linearizedDictionary == null);
    }

    /**
     * @return true if the current document is encrypted
     */
    public Boolean getisEncrypted() {
        return ((COSDictionary) baseObject).getItem("Encrypt") != null;
    }

    /**
     * @return true if ID of first and last trailers are match
     */
    @Override
    public Boolean getdoFirstLastTrailerIDsMatch() {
        return getid1().equals(firstTrailer.getid1()) && getid2().equals(firstTrailer.getid2());
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        List<? extends Object> list;
        switch (link) {
            case CATALOG:
                list = getCatalog();
                break;
            default:
                list = super.getLinkedObjects(link);
                break;
        }
        return list;
    }

    private List<CosIndirect> getCatalog() {
        List<CosIndirect> catalog = new ArrayList<>(1);
        catalog.add((CosIndirect) ((COSDictionary) baseObject).getItem(CATALOG));
        return catalog;
    }
}
