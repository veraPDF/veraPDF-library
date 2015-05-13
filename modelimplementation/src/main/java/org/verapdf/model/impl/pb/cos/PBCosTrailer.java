package org.verapdf.model.impl.pb.cos;

import org.apache.pdfbox.cos.COSDictionary;
import org.verapdf.model.coslayer.CosTrailer;

/**
 * Created by Evgeniy Muravitskiy on 5/1/15.
 * <p>
 *     Trailer of the document. It has the same behavior as the PBCosDict.
 * </p>
 */
public class PBCosTrailer extends PBCosDict implements CosTrailer {

    public PBCosTrailer(COSDictionary pdfBoxObject) {
        super(pdfBoxObject);
        setType("CosTrailer");
    }
}
