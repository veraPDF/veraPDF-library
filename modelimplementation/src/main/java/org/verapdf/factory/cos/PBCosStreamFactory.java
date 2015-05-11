package org.verapdf.factory.cos;

import org.apache.pdfbox.cos.COSStream;
import org.verapdf.impl.pb.PBCosStream;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.coslayer.CosStream;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 5/4/15.
 * <p>
 *     Class for transforming COSStream of pdfbox to CosStream of abstract model.
 * </p>
 */
class PBCosStreamFactory implements PBCosFactory<CosStream, COSStream> {

    /** Method for transforming COSStream to corresponding CosStream
     */
    @Override
    public CosStream generateCosObject(COSStream pdfBoxObject) {
        return new PBCosStream(pdfBoxObject);
    }

    /**
     * Method for transforming COSStream to corresponding CosStream. Also takes into account already
     * exists objects.
     */
    @Override
    public CosStream generateCosObject(List<CosObject> parents, COSStream pdfBoxObject) {
        for (CosObject object : parents)
            if (object.equals(pdfBoxObject))
                return (CosStream) object;

        CosStream stream = generateCosObject(pdfBoxObject);
        parents.add(stream);
        return stream;
    }
}
