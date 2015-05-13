package org.verapdf.model.factory.cos;

import org.apache.pdfbox.cos.COSStream;
import org.verapdf.model.impl.pb.cos.PBCosStream;
import org.verapdf.model.coslayer.CosObject;
import org.verapdf.model.coslayer.CosStream;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 5/4/15.
 * <p>
 *     Class for transforming COSStream of pdfbox to CosStream of abstract model.
 * </p>
 */
class PBCosStreamFactory extends PBLinkedCosFactory<CosStream, COSStream> {

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
    public CosStream generateCosObject(List<CosObject> convertedObjects, COSStream pdfBoxObject) {
        CosStream stream = checkInConvertedObjects(convertedObjects, pdfBoxObject);
        if (stream != null)
            return stream;

        stream = generateCosObject(pdfBoxObject);
        convertedObjects.add(stream);
        return stream;
    }
}
