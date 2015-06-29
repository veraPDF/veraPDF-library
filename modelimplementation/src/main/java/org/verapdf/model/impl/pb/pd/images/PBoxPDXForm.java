package org.verapdf.model.impl.pb.pd.images;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNull;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosStream;
import org.verapdf.model.impl.pb.cos.PBCosStream;
import org.verapdf.model.impl.pb.pd.PBoxPDContentStream;
import org.verapdf.model.pdlayer.PDContentStream;
import org.verapdf.model.pdlayer.PDXForm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDXForm extends PBoxPDXObject implements PDXForm {

    public static final Logger logger = Logger.getLogger(PBoxPDXForm.class);

    public static final String PS = "PS";
    public static final String REF = "Ref";
    public static final String CONTENT_STREAM = "contentStream";

    public PBoxPDXForm(PDFormXObject simplePDObject) {
        super(simplePDObject);
        setType("PDXForm");
    }

    @Override
    public String getSubtype2() {
        final COSStream subtype2 = ((PDFormXObject) simplePDObject).getCOSStream();
        return getSubtypeString(subtype2.getItem(COSName.getPDFName("Subtype2")));
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        List<? extends Object> list;

        switch (link) {
            case PS:
                list = getPS();
                break;
            case REF:
                list = getREF();
                break;
            case CONTENT_STREAM:
                list = getContentStream();
                break;
            default:
                list = super.getLinkedObjects(link);
                break;
        }

        return list;
    }

    private List<CosStream> getPS() {
        List<CosStream> postScript = new ArrayList<>(1);
        final COSBase item = ((PDFormXObject) simplePDObject).getCOSStream().getItem(COSName.PS);
        if (item != null) {
            COSStream ps = getStream(item);
            if (ps != null) {
                postScript.add(new PBCosStream(ps));
            }
        }
        return postScript;
    }

    private List<CosDict> getREF() {
        return getLinkToDictionary(REF);
    }

    private List<PDContentStream> getContentStream() {
        List<PDContentStream> contentStreams = new ArrayList<>(1);
        final PDStream pdStream = ((PDFormXObject) simplePDObject).getPDStream();
        if (pdStream != null && pdStream.getCOSObject() != null && !(pdStream.getCOSObject() instanceof COSNull)) {
            contentStreams.add(new PBoxPDContentStream(pdStream));
        }
        return contentStreams;
    }
}
