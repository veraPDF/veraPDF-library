package org.verapdf.model.impl.pb.pd.images;

import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.coslayer.CosStream;
import org.verapdf.model.impl.pb.cos.PBCosStream;
import org.verapdf.model.impl.pb.pd.PBoxPDContentStream;
import org.verapdf.model.impl.pb.pd.PBoxPDGroup;
import org.verapdf.model.pdlayer.PDContentStream;
import org.verapdf.model.pdlayer.PDGroup;
import org.verapdf.model.pdlayer.PDXForm;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDXForm extends PBoxPDXObject implements PDXForm {

    public static final String X_FORM_TYPE = "PDXForm";

    public static final String GROUP = "Group";
    public static final String PS = "PS";
    public static final String REF = "Ref";
    public static final String CONTENT_STREAM = "contentStream";

    public PBoxPDXForm(PDFormXObject simplePDObject) {
        super(simplePDObject, X_FORM_TYPE);
    }

    @Override
    public String getSubtype2() {
        final COSStream subtype2 = ((PDFormXObject) simplePDObject)
                .getCOSStream();
        return getSubtypeString(subtype2
                .getDictionaryObject(COSName.getPDFName("Subtype2")));
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        List<? extends Object> list;

        switch (link) {
        case GROUP:
            list = this.getGroup();
            break;
        case PS:
            list = this.getPS();
            break;
        case REF:
            list = this.getREF();
            break;
        case CONTENT_STREAM:
            list = this.getContentStream();
            break;
        default:
            list = super.getLinkedObjects(link);
            break;
        }

        return list;
    }

    private List<PDGroup> getGroup() {
        List<PDGroup> groups = new ArrayList<>(1);
        org.apache.pdfbox.pdmodel.graphics.form.PDGroup group = ((PDFormXObject) simplePDObject)
                .getGroup();
        if (group != null) {
            groups.add(new PBoxPDGroup(group));
        }
        return groups;
    }

    private List<CosStream> getPS() {
        List<CosStream> postScript = new ArrayList<>(1);
        final COSStream cosStream = ((PDFormXObject) simplePDObject)
                .getCOSStream();
        COSStream ps = (COSStream) cosStream.getDictionaryObject(COSName.PS);
        if (ps != null) {
            postScript.add(new PBCosStream(ps));
        }
        return postScript;
    }

    private List<CosDict> getREF() {
        return getLinkToDictionary(REF);
    }

    private List<PDContentStream> getContentStream() {
        List<PDContentStream> contentStreams = new ArrayList<>(1);
        contentStreams.add(new PBoxPDContentStream(
                (PDFormXObject) simplePDObject));
        return contentStreams;
    }
}
