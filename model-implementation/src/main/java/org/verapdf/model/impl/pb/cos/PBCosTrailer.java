package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
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
    private final List<CosIndirect> catalog;

    public PBCosTrailer(COSDictionary dictionary) {
        super(dictionary, COS_TRAILER_TYPE);
        this.isEncrypted = dictionary.getItem(COSName.ENCRYPT) != null;
        this.catalog = parseCatalog(dictionary);
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
            return catalog;
        }
        return super.getLinkedObjects(link);
    }

    private static List<CosIndirect> parseCatalog(COSDictionary dictionary) {
        List<CosIndirect> cat = new ArrayList<>(1);
        COSBase base = dictionary.getItem(COSName.ROOT);
        cat.add(new PBCosIndirect(base));
        return Collections.unmodifiableList(cat);
    }
}
