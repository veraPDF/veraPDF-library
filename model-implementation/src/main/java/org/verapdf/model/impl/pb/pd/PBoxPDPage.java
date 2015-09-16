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
import java.util.Collections;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDPage extends PBoxPDObject implements PDPage {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDPage.class);

	public static final String PAGE_TYPE = "PDPage";

	public static final String ANNOTS = "annots";
	public static final String ACTION = "action";
	public static final String CONTENT_STREAM = "contentStream";
	public static final String GROUP = "Group";

	public static final int MAX_NUMBER_OF_ACTIONS = 2;

	public PBoxPDPage(org.apache.pdfbox.pdmodel.PDPage simplePDObject) {
		super((COSObjectable) simplePDObject, PAGE_TYPE);
	}

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case GROUP:
				return this.getGroup();
			case ANNOTS:
				return this.getAnnotations();
			case ACTION:
				return this.getActions();
			case CONTENT_STREAM:
				return this.getContentStream();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<PDGroup> getGroup() {
		COSDictionary dictionary = ((org.apache.pdfbox.pdmodel.PDPage) this.simplePDObject)
				.getCOSObject();
		COSBase groupDictionary = dictionary.getDictionaryObject(COSName.GROUP);
		if (groupDictionary instanceof COSDictionary) {
			List<PDGroup> groups = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			org.apache.pdfbox.pdmodel.graphics.form.PDGroup group =
					new org.apache.pdfbox.pdmodel.graphics.form.PDGroup(
							(COSDictionary) groupDictionary);
			groups.add(new PBoxPDGroup(group));
			return Collections.unmodifiableList(groups);
		}
		return Collections.emptyList();
	}

	private List<PDContentStream> getContentStream() {
		List<PDContentStream> contentStreams = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
		contentStreams.add(new PBoxPDContentStream(
				(org.apache.pdfbox.pdmodel.PDPage) this.simplePDObject));
		return contentStreams;
	}

	private List<PDAction> getActions() {
		PDPageAdditionalActions pbActions = ((org.apache.pdfbox.pdmodel.PDPage) this.simplePDObject)
				.getActions();
		if (pbActions != null) {
			List<PDAction> actions = new ArrayList<>(MAX_NUMBER_OF_ACTIONS);

			org.apache.pdfbox.pdmodel.interactive.action.PDAction action;

			action = pbActions.getC();
			this.addAction(actions, action);

			action = pbActions.getO();
			this.addAction(actions, action);

			return Collections.unmodifiableList(actions);
		}
		return Collections.emptyList();
	}

	private List<PDAnnot> getAnnotations() {
		try {
			List<PDAnnotation> pdfboxAnnotations = ((org.apache.pdfbox.pdmodel.PDPage) this.simplePDObject)
					.getAnnotations();
			if (pdfboxAnnotations != null) {
				List<PDAnnot> annotations = new ArrayList<>(pdfboxAnnotations.size());
				this.addAllAnnotations(annotations, pdfboxAnnotations);
				return Collections.unmodifiableList(annotations);
			}
		} catch (IOException e) {
			LOGGER.error(
					"Problems in obtaining pdfbox PDAnnotations. "
							+ e.getMessage(), e);
		}
		return Collections.emptyList();
	}

	private void addAllAnnotations(List<PDAnnot> annotations,
								   List<PDAnnotation> pdfboxAnnotations) {
		for (PDAnnotation annotation : pdfboxAnnotations) {
			if (annotation != null) {
				annotations.add(new PBoxPDAnnot(annotation));
			}
		}
	}
}
