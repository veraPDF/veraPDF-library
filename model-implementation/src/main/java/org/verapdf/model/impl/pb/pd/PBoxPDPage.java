package org.verapdf.model.impl.pb.pd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.interactive.action.PDPageAdditionalActions;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.PDAction;
import org.verapdf.model.pdlayer.PDAnnot;
import org.verapdf.model.pdlayer.PDContentStream;
import org.verapdf.model.pdlayer.PDGroup;
import org.verapdf.model.pdlayer.PDPage;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDPage extends PBoxPDObject implements PDPage {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDPage.class);

    /**
     * String name for annotations
     */
    public static final String ANNOTS = "annots";
    /**
     * String name for actions
     */
    public static final String ACTION = "action";
    /**
     * String name for content streams
     */
    public static final String CONTENT_STREAM = "contentStream";
    /**
     * String name for destination groups
     */
	public static final String GROUP = "Group";

	private static final int MAX_NUMBER_OF_ACTIONS = 2;

    /**
     * @param simplePDObject
     *            a {@link org.apache.pdfbox.pdmodel.PDPage} used to
     *            populate the instance
     */
	public PBoxPDPage(org.apache.pdfbox.pdmodel.PDPage simplePDObject) {
        super((COSObjectable) simplePDObject);
        setType("PDPage");
    }

    @Override
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
		if (groupDictionary instanceof COSDictionary) {
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

    private List<PDAction> getActions() {
        List<PDAction> actions = new ArrayList<>(MAX_NUMBER_OF_ACTIONS);
        PDPageAdditionalActions pbActions = ((org.apache.pdfbox.pdmodel.PDPage) simplePDObject).getActions();
		if (pbActions != null) {
			org.apache.pdfbox.pdmodel.interactive.action.PDAction action;

			action = pbActions.getC();
			addAction(actions, action);

			action = pbActions.getO();
			addAction(actions, action);
		}
        return actions;
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
            LOGGER.error("Problems in obtaining pdfbox PDAnnotations. " + e.getMessage(), e);
        }
        return annotations;
    }
}
