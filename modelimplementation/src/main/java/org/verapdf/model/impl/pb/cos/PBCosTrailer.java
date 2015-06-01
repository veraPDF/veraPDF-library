package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
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

    public final static String CATALOG = "Catalog";
    /** if document is linearized its must have dictionary of linearization
     */
    private COSDictionary linearizedDictionary = null;
    /** if document is linearized first trailer is differ from last
     */
    private COSDictionary firstTrailer = null;
    /** length of the document
     */
    private Long length = null;

    public PBCosTrailer(COSDictionary pdfBoxObject) {
        super(pdfBoxObject);
        setType("CosTrailer");
    }

    public PBCosTrailer(COSDictionary mainTrailer, COSDictionary firstTrailer, COSDictionary linearizedDictionary, Long length) {
        super(mainTrailer);
        this.firstTrailer = firstTrailer;
        this.linearizedDictionary = linearizedDictionary;
        this.length = length;
    }

    /**
     * @return ID of first page trailer
     */
    public String getfirstPageID() {
        return getTrailerID((COSArray) firstTrailer.getItem("ID"));
    }

    /**
     * @return ID of last document trailer
     */
    public String lastID() {
        return getTrailerID((COSArray) ((COSDictionary) baseObject).getItem("ID"));
    }

    private String getTrailerID(COSArray ids) {
        StringBuilder builder = new StringBuilder();
        for (COSBase id : ids) {
            builder.append(((COSString) id).getASCII()).append(' ');
        }
        // need to discard last whitespace
        return builder.toString().substring(0, builder.length() - 2);
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
