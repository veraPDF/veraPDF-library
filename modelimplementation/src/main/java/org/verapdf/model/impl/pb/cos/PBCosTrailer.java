package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSString;
import org.verapdf.model.coslayer.CosTrailer;

/**
 * Created by Evgeniy Muravitskiy on 5/1/15.
 * <p>
 *     Trailer of the document. It has the same behavior as the PBCosDict.
 * </p>
 */
public class PBCosTrailer extends PBCosDict implements CosTrailer {

    private COSDictionary linearizedDictionary = null;
    private CosTrailer firstTrailer = null;
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

    public String getid1() {
        return getPartOfID(0);
    }

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

    // TODO : need to support of this feature
    public Boolean getisLinearized() {
        return !(baseObject == firstTrailer || linearizedDictionary == null);
    }

    public Boolean getisEncrypted() {
        return ((COSDictionary) baseObject).getItem("Encrypt") != null;
    }

    @Override
    public Boolean getdoFirstLastTrailerIDsMatch() {
        return getid1().equals(firstTrailer.getid1()) && getid2().equals(firstTrailer.getid2());
    }
}
