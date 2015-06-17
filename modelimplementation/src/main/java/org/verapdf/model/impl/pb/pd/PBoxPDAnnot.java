package org.verapdf.model.impl.pb.pd;

import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDAnnot;
import org.verapdf.model.pdlayer.PDContentStream;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDAnnot extends PBoxPDObject implements PDAnnot {

    public final static String APPEARANCE = "appearance";

    public PBoxPDAnnot(PDAnnotation simplePDObject) {
        super(simplePDObject);
        setType("PDAnnot");
    }

    @Override
    public String getSubtype() {
        return ((PDAnnotation) simplePDObject).getSubtype();
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        List<? extends Object> list;
        switch (link) {
            case APPEARANCE:
                list = getAppearance();
                break;
            default:
                list = super.getLinkedObjects(link);
                break;
        }
        return list;
    }

    //TODO : implement this
    private List<PDContentStream> getAppearance() {
        List<PDContentStream> appearance = new ArrayList<>();
        return appearance;
    }
}
