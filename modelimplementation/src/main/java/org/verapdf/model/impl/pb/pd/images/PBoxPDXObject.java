package org.verapdf.model.impl.pb.pd.images;

import org.apache.pdfbox.cos.*;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.impl.pb.pd.PBoxPDResources;
import org.verapdf.model.pdlayer.PDXObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDXObject extends PBoxPDResources implements PDXObject {

    public static final String OPI = "OPI";

    public PBoxPDXObject(org.apache.pdfbox.pdmodel.graphics.PDXObject simplePDObject) {
        super(simplePDObject);
        setType("PDXObject");
    }

    // TODO : check it
    @Override
    public String getSubtype() {
        COSDictionary dict = ((org.apache.pdfbox.pdmodel.graphics.PDXObject) simplePDObject).getCOSStream();
        return getSubtypeString(dict.getItem(COSName.SUBTYPE));
    }

    private String getSubtypeString(COSBase item) {
        if (item instanceof COSString) {
            return ((COSString) item).getString();
        } else if (item instanceof COSName) {
            return ((COSName) item).getName();
        } else if (item instanceof COSObject) {
            return getSubtypeString(((COSObject) item).getObject());
        } else {
            return null;
        }
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        List<? extends Object> list;
        switch (link) {
            case OPI:
                list = getOPI();
                break;
            default:
                list = super.getLinkedObjects(link);
                break;
        }

        return list;
    }

    // TODO : implement this
    private List<CosDict> getOPI() {
        return new ArrayList<>();
    }
}
