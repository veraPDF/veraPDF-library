package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSNull;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.interactive.action.PDPageAdditionalActions;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDAction;
import org.verapdf.model.pdlayer.PDAnnot;
import org.verapdf.model.pdlayer.PDContentStream;
import org.verapdf.model.pdlayer.PDPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDPage extends PBoxPDObject implements PDPage {

    public static final Logger logger = Logger.getLogger(PBoxPDPage.class);

    public final static String ANNOTS = "annots";
    public final static String ACTION = "action";
    public static final String CONTENT_STREAM = "contentStream";

    public PBoxPDPage(org.apache.pdfbox.pdmodel.PDPage simplePDObject) {
        super((COSObjectable) simplePDObject);
        setType("PDPage");
    }

    public List<? extends Object> getLinkedObjects(String link) {
        List<? extends Object> list;

        switch (link) {
            case ANNOTS:
                list = getAnnotations();
                break;
            case ACTION:
                list = getActions();
                break;
            case CONTENT_STREAM:
                list = getContentStream();
                break;
            default:
                list = super.getLinkedObjects(link);
        }

        return list;
    }

    //TODO : implement this
    private List<PDContentStream> getContentStream() {
        List<PDContentStream> contentStreams = new ArrayList<>();
        try {
            final PDStream stream = ((org.apache.pdfbox.pdmodel.PDPage) simplePDObject).getStream();
            if (stream != null && stream.getCOSObject() != null && !(stream.getCOSObject() instanceof COSNull)) {
                contentStreams.add(new PBoxPDContentStream(stream));
            }
        } catch (IOException e) {
            logger.error("Can not get content stream of page. " + e.getMessage());
        }
        return new ArrayList<>();
    }

    //TODO : implement this
    private List<PDAction> getActions() {
        List<PDAction> action = new ArrayList<>(1);
        final PDPageAdditionalActions actions = ((org.apache.pdfbox.pdmodel.PDPage) simplePDObject).getActions();
        return action;
    }

    private List<PDAnnot> getAnnotations() {
        List<PDAnnot> annotations = new ArrayList<>();
        try {
            List<PDAnnotation> pdfboxAnnotations = ((org.apache.pdfbox.pdmodel.PDPage) simplePDObject).getAnnotations();
            if (pdfboxAnnotations != null) {
                for (PDAnnotation annotation : pdfboxAnnotations) {
                    if (annotation != null) {
                        annotations.add(new PBoxPDAnnot(annotation));
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Problems in obtaining pdfbox PDAnnotations. " + e.getMessage());
        }
        return annotations;
    }
}
