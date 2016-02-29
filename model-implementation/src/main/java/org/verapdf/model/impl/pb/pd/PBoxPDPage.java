package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.interactive.action.PDPageAdditionalActions;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceStream;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosBBox;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.impl.pb.cos.PBCosBBox;
import org.verapdf.model.impl.pb.cos.PBCosDict;
import org.verapdf.model.pdlayer.*;
import org.verapdf.model.tools.resources.PDInheritableResources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Page representation of pdf document
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDPage extends PBoxPDObject implements PDPage {

	private static final Logger LOGGER = Logger.getLogger(PBoxPDPage.class);

	/** Type name for {@code PBoxPDPage} */
	public static final String PAGE_TYPE = "PDPage";

	/** Link name for page annotations */
	public static final String ANNOTS = "annots";
	/** Link name for page additional actions */
	public static final String ACTION = "AA";
	/** Link name for page content stream */
	public static final String CONTENT_STREAM = "contentStream";
	/** Link name for page transparency group */
	public static final String GROUP = "Group";
	/** Link name for page media box */
	public static final String MEDIA_BOX = "MediaBox";
	/** Link name for page crop box */
	public static final String CROP_BOX = "CropBox";
	/** Link name for page bleed box */
	public static final String BLEED_BOX = "BleedBox";
	/** Link name for trim media box */
	public static final String TRIM_BOX = "TrimBox";
	/** Link name for page art box */
	public static final String ART_BOX = "ArtBox";
	/** Link name for page presentation steps */
	public static final String PRESENTATION_STEPS = "PresSteps";

	/** Maximal number of actions in page dictionary */
	public static final int MAX_NUMBER_OF_ACTIONS = 2;

	/**
	 * Default constructor.
	 *
	 * @param simplePDObject Apache PDFBox page representation
	 */
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
			case MEDIA_BOX:
				return this.getMediaBox();
			case CROP_BOX:
				return this.getCropBox();
			case BLEED_BOX:
				return this.getBleedBox();
			case TRIM_BOX:
				return this.getTrimBox();
			case ART_BOX:
				return this.getArtBox();
			case PRESENTATION_STEPS:
				return this.getPresentationSteps();
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
		org.apache.pdfbox.pdmodel.PDPage stream =
				(org.apache.pdfbox.pdmodel.PDPage) this.simplePDObject;
		PDInheritableResources resources = PDInheritableResources
				.getInstance(stream.getResources(), PDInheritableResources.EMPTY_RESOURCES);
		contentStreams.add(new PBoxPDContentStream(stream, resources));
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
		PDResources pageResources = ((org.apache.pdfbox.pdmodel.PDPage)
				this.simplePDObject).getResources();
		for (PDAnnotation annotation : pdfboxAnnotations) {
			if (annotation != null) {
				PDAppearanceStream stream = annotation.getNormalAppearanceStream();
				PDResources resources = stream != null ? stream.getResources() : PDInheritableResources.EMPTY_RESOURCES;
				PDInheritableResources extRes = PDInheritableResources.getInstance(pageResources, resources);
				annotations.add(new PBoxPDAnnot(annotation, extRes));
			}
		}
	}

	private List<CosBBox> getMediaBox() {
		return this.getCosBBox(COSName.MEDIA_BOX);
	}

	private List<CosBBox> getCropBox() {
		return this.getCosBBox(COSName.CROP_BOX);
	}

	private List<CosBBox> getBleedBox() {
		return this.getCosBBox(COSName.BLEED_BOX);
	}

	private List<CosBBox> getTrimBox() {
		return this.getCosBBox(COSName.TRIM_BOX);
	}

	private List<CosBBox> getArtBox() {
		return this.getCosBBox(COSName.ART_BOX);
	}

	private List<CosBBox> getCosBBox(COSName key) {
		COSBase array = ((org.apache.pdfbox.pdmodel.PDPage) this.simplePDObject)
				.getCOSObject().getDictionaryObject(key);
		if (array instanceof COSArray) {
			ArrayList<CosBBox> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBCosBBox((COSArray) array));
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}

	private List<CosDict> getPresentationSteps() {
		COSBase presSteps = ((org.apache.pdfbox.pdmodel.PDPage) this.simplePDObject)
				.getCOSObject().getDictionaryObject(COSName.getPDFName(PRESENTATION_STEPS));
		if (presSteps instanceof COSDictionary) {
			ArrayList<CosDict> list = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			list.add(new PBCosDict((COSDictionary) presSteps));
			return Collections.unmodifiableList(list);
		}
		return Collections.emptyList();
	}
}
