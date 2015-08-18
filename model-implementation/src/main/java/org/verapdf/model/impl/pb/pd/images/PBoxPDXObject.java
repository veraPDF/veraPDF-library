package org.verapdf.model.impl.pb.pd.images;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDPostScriptXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.impl.pb.cos.PBCosDict;
import org.verapdf.model.impl.pb.pd.PBoxPDResources;
import org.verapdf.model.pdlayer.PDXObject;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDXObject extends PBoxPDResources implements PDXObject {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDXObject.class);

    public static final String X_OBJECT_TYPE = "PDXObject";
    public static final String OPI = "OPI";
    public static final String S_MASK = "SMask";
    public static final int MAX_NUMBER_OF_ELEMENTS = 1;

    public PBoxPDXObject(
            org.apache.pdfbox.pdmodel.graphics.PDXObject simplePDObject) {
        super(simplePDObject, X_OBJECT_TYPE);
    }

	public PBoxPDXObject(
			org.apache.pdfbox.pdmodel.graphics.PDXObject simplePDObject,
			final String type) {
		super(simplePDObject, type);
	}

    @Override
    public String getSubtype() {
        COSDictionary dict = ((org.apache.pdfbox.pdmodel.graphics.PDXObject) simplePDObject)
                .getCOSStream();
        return getSubtypeString(dict.getItem(COSName.SUBTYPE));
    }

    protected String getSubtypeString(COSBase item) {
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
        case S_MASK:
            list = this.getSMask();
            break;
        case OPI:
            list = this.getOPI();
            break;
        default:
            list = super.getLinkedObjects(link);
            break;
        }

        return list;
    }

    private List<PDXObject> getSMask() {
        List<PDXObject> mask = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        try {
            COSStream cosStream = ((org.apache.pdfbox.pdmodel.graphics.PDXObject) simplePDObject)
                    .getCOSStream();
            COSBase smaskDictionary = cosStream
                    .getDictionaryObject(COSName.SMASK);
            if (smaskDictionary instanceof COSDictionary) {
                PDXObject xObject = getXObject(smaskDictionary);
                if (xObject != null) {
                    mask.add(xObject);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Problems with obtaining SMask. " + e.getMessage(), e);
        }
        return mask;
    }

    private PDXObject getXObject(COSBase smaskDictionary) throws IOException {
        COSName name = ((COSDictionary) smaskDictionary)
                .getCOSName(COSName.NAME);
        String nameAsString = name != null ? name.getName() : null;
        PDResources resources = null;
        if (simplePDObject instanceof PDFormXObject) {
            resources = ((PDFormXObject) simplePDObject).getResources();
        }
        org.apache.pdfbox.pdmodel.graphics.PDXObject pbObject = org.apache.pdfbox.pdmodel.graphics.PDXObject
                .createXObject(smaskDictionary, nameAsString, resources);
        return getTypedPDXObject(pbObject);
    }

    private static PDXObject getTypedPDXObject(
            org.apache.pdfbox.pdmodel.graphics.PDXObject pbObject) {
        if (pbObject instanceof PDFormXObject) {
            return new PBoxPDXForm((PDFormXObject) pbObject);
        } else if (pbObject instanceof PDImageXObject) {
            return new PBoxPDXImage((PDImageXObject) pbObject);
        } else if (pbObject instanceof PDPostScriptXObject) {
            return new PBoxPDXObject(pbObject);
        } else {
            return null;
        }
    }

    private List<CosDict> getOPI() {
        return getLinkToDictionary(OPI);
    }

    protected List<CosDict> getLinkToDictionary(String key) {
        List<CosDict> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        COSDictionary object = ((org.apache.pdfbox.pdmodel.graphics.PDXObject) simplePDObject)
                .getCOSStream();
        COSBase item = object.getItem(COSName.getPDFName(key));
        if (item != null) {
            final COSDictionary itemDictionary = getDictionary(item);
            if (itemDictionary != null) {
                list.add(new PBCosDict(itemDictionary));
            }
        }
        return list;
    }

    protected COSDictionary getDictionary(COSBase item) {
        if (item instanceof COSDictionary) {
            return (COSDictionary) item;
        } else if (item instanceof COSObject) {
            return getDictionary(((COSObject) item).getObject());
        } else {
            return null;
        }
    }
}
