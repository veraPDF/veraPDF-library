package org.verapdf.model.factory.cos;

import org.apache.pdfbox.cos.COSDocument;
import org.verapdf.model.impl.pb.cos.PBCosDocument;
import org.verapdf.model.coslayer.CosDocument;
import org.verapdf.model.coslayer.CosObject;

import java.util.List;

/**
 * Created by Evgeniy Muravitskiy on 5/4/15.
 * <p>
 *     Class for transforming COSDocument of pdf box to CosDocument of abstract model.
 * </p>
 */
class PBCosDocumentFactory implements PBCosFactory<CosDocument, COSDocument> {

    /**
     * Method for transforming COSDocument to corresponding CosDocument
     */
    @Override
    public CosDocument generateCosObject(COSDocument pdfBoxObject) {
        return new PBCosDocument(pdfBoxObject);
    }

    /**
     * Method for transforming COSDocument to corresponding CosDocument. Also takes into account already
     * exists objects.
     */
    @Override
    public CosDocument generateCosObject(List<CosObject> convertedObjects, COSDocument pdfBoxObject) {
        return generateCosObject(pdfBoxObject);
    }
}
