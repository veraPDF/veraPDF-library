package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosIndirect;
import org.verapdf.model.coslayer.CosTrailer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Trailer of the document.
 *
 * @author Evgeniy Muravitskiy
 */
public class PBCosTrailer extends PBCosDict implements CosTrailer {

    /** Type name for PBCosTrailer */
    public static final String COS_TRAILER_TYPE = "CosTrailer";

    public static final String CATALOG = "Catalog";

    private final boolean isEncrypted;

    /**
     * Default constructor
     * @param dictionary pdfbox COSDictionary
     */
    public PBCosTrailer(COSDictionary dictionary) {
        super(dictionary, COS_TRAILER_TYPE);
        this.isEncrypted = dictionary.getItem(COSName.ENCRYPT) != null;
    }

    /**
     * @return true if the current document is encrypted
     */
    @Override
    public Boolean getisEncrypted() {
        return Boolean.valueOf(this.isEncrypted);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        if (CATALOG.equals(link)) {
            return this.getCatalog();
        }
        return super.getLinkedObjects(link);
    }

    private List<CosIndirect> getCatalog() {
        List<CosIndirect> catalog = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        COSBase base = ((COSDictionary) this.baseObject)
				.getItem(COSName.ROOT);
        catalog.add(new PBCosIndirect((COSObject) base));
        return Collections.unmodifiableList(catalog);
    }
}
