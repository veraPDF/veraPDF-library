package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.interactive.action.PDPageAdditionalActions;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDPage extends PBoxPDObject implements PDPage {

    public static final Logger logger = Logger.getLogger(PBoxPDPage.class);

    public static final String ANNOTS = "annots";
    public static final String ACTION = "action";
    public static final String CONTENT_STREAM = "contentStream";
	public static final String GROUP = "Group";

    public PBoxPDPage(org.apache.pdfbox.pdmodel.PDPage simplePDObject) {
        super((COSObjectable) simplePDObject);
        setType("PDPage");
    }

    public List<? extends Object> getLinkedObjects(String link) {
        List<? extends Object> list;

        switch (link) {
			case GROUP:
				list = getGroup();
				break;
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

	private List<PDGroup> getGroup() {
		List<PDGroup> groups = new ArrayList<>(1);
		COSDictionary dictionary = ((org.apache.pdfbox.pdmodel.PDPage) simplePDObject).getCOSObject();
		COSBase groupDictionary = dictionary.getDictionaryObject(COSName.GROUP);
		if (groupDictionary != null && groupDictionary instanceof COSDictionary) {
			org.apache.pdfbox.pdmodel.graphics.form.PDGroup group =
					new org.apache.pdfbox.pdmodel.graphics.form.PDGroup((COSDictionary) groupDictionary);
			groups.add(new PBoxPDGroup(group));
		}
		return groups;
	}

	private List<PDContentStream> getContentStream() {
		List<PDContentStream> contentStreams = new ArrayList<>();
		contentStreams.add(new PBoxPDContentStream((org.apache.pdfbox.pdmodel.PDPage) simplePDObject));
		return contentStreams;
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
