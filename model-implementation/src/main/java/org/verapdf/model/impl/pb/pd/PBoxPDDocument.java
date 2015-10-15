package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDDestinationOrAction;
import org.apache.pdfbox.pdmodel.interactive.action.PDDocumentCatalogAdditionalActions;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.*;

import java.io.IOException;
import java.util.*;

/**
 * High-level representation of pdf document
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDocument extends PBoxPDObject implements PDDocument {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDDocument.class);

    public static final String PAGES = "pages";
    public static final String METADATA = "metadata";
    public static final String OUTPUT_INTENTS = "outputIntents";
    public static final String ACRO_FORMS = "AcroForm";
    public static final String ACTIONS = "AA";
    public static final String OPEN_ACTION = "OpenAction";
    public static final String OUTLINES = "Outlines";

    public static final int MAX_NUMBER_OF_ACTIONS = 5;
    public static final String PD_DOCUMENT_TYPE = "PDDocument";

    public PBoxPDDocument(org.apache.pdfbox.pdmodel.PDDocument document) {
        super(document, PD_DOCUMENT_TYPE);
		clearGlyphs();
    }

	@Override
	public List<? extends Object> getLinkedObjects(String link) {
		switch (link) {
			case OUTLINES:
				return this.getOutlines();
			case OPEN_ACTION:
				return this.getOpenAction();
			case ACTIONS:
				return this.getActions();
			case PAGES:
				return this.getPages();
			case METADATA:
				return this.getMetadata();
			case OUTPUT_INTENTS:
				return this.getOutputIntents();
			case ACRO_FORMS:
				return this.getAcroForms();
			default:
				return super.getLinkedObjects(link);
		}
	}

    private List<PDOutline> getOutlines() {
        List<PDOutlineItem> outlines = this.getOutlinesList();
		List<PDOutline> result = new ArrayList<>(outlines.size());
		for (PDOutlineItem outlineItem : outlines) {
            result.add(new PBoxPDOutline(outlineItem));
        }
        outlines.clear();
        return result;
    }

    private List<PDOutlineItem> getOutlinesList() {
        PDDocumentOutline documentOutline = this.document
				.getDocumentCatalog().getDocumentOutline();

        if (documentOutline != null) {
			List<PDOutlineItem> result = new ArrayList<>();

			PDOutlineItem firstChild = documentOutline.getFirstChild();
            Deque<PDOutlineItem> stack = new ArrayDeque<>();

            if (firstChild != null) {
                stack.push(firstChild);
            }

            while (!stack.isEmpty()) {
                PDOutlineItem item = stack.pop();
                PDOutlineItem nextSibling = item.getNextSibling();
                firstChild = item.getFirstChild();
                if (nextSibling != null && !result.contains(nextSibling)) {
                    stack.add(nextSibling);
                }
                if (firstChild != null && !result.contains(firstChild)) {
                    stack.add(firstChild);
                }
                result.add(item);
            }

			return result;
        }

        return Collections.emptyList();
    }

    private List<PDAction> getOpenAction() {
        try {
            PDDestinationOrAction openAction = this.document.getDocumentCatalog()
                    .getOpenAction();
            if (openAction instanceof org.apache.pdfbox.pdmodel.interactive.action.PDAction) {
				List<PDAction> actions = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				this.addAction(
                        actions,
                        (org.apache.pdfbox.pdmodel.interactive.action.PDAction) openAction);
				return Collections.unmodifiableList(actions);
            }
        } catch (IOException e) {
            LOGGER.error(
                    "Problems with open action obtaining. " + e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    private List<PDAction> getActions() {
        PDDocumentCatalogAdditionalActions pbActions = this.document
                .getDocumentCatalog().getActions();
        if (pbActions != null) {
			List<PDAction> actions = new ArrayList<>(MAX_NUMBER_OF_ACTIONS);

			org.apache.pdfbox.pdmodel.interactive.action.PDAction buffer;

            buffer = pbActions.getDP();
            this.addAction(actions, buffer);

            buffer = pbActions.getDS();
            this.addAction(actions, buffer);

            buffer = pbActions.getWP();
            this.addAction(actions, buffer);

            buffer = pbActions.getWS();
            this.addAction(actions, buffer);

            buffer = pbActions.getWC();
            this.addAction(actions, buffer);

			return Collections.unmodifiableList(actions);
        }
        return Collections.emptyList();
    }

	private List<PDPage> getPages() {
		PDPageTree pageTree = this.document.getPages();
		List<PDPage> pages = new ArrayList<>(pageTree.getCount());
		for (org.apache.pdfbox.pdmodel.PDPage page : pageTree) {
			pages.add(new PBoxPDPage(page));
		}
		return pages;
	}

    private List<PDMetadata> getMetadata() {
        org.apache.pdfbox.pdmodel.common.PDMetadata meta = this.document
                .getDocumentCatalog().getMetadata();
        if (meta != null && meta.getCOSObject() != null) {
			List<PDMetadata> metadata = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			metadata.add(new PBoxPDMetadata(meta, Boolean.TRUE));
			return Collections.unmodifiableList(metadata);
        }
        return Collections.emptyList();
    }

    private List<PDOutputIntent> getOutputIntents() {
		List<org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent> pdfboxOutputIntents =
				this.document.getDocumentCatalog().getOutputIntents();
		List<PDOutputIntent> outputIntents = new ArrayList<>(pdfboxOutputIntents.size());
        for (org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent intent : pdfboxOutputIntents) {
			outputIntents.add(new PBoxPDOutputIntent(intent));
		}
        return outputIntents;
    }

    private List<PDAcroForm> getAcroForms() {
        org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm form =
				this.document.getDocumentCatalog().getAcroForm();
        if (form != null) {
			List<PDAcroForm> forms = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			forms.add(new PBoxPDAcroForm(form));
			return Collections.unmodifiableList(forms);
        }
        return Collections.emptyList();
    }

	public static final ThreadLocal<Map<COSDictionary, List<Integer>>> SAVED_GLYPHS;

	static {
		SAVED_GLYPHS  = new ThreadLocal<Map<COSDictionary, List<Integer>>>() {
			@Override
			protected Map<COSDictionary, List<Integer>> initialValue() {
				return new HashMap<>();
			}
		};
	}

	private void clearGlyphs() {
		SAVED_GLYPHS.get().clear();
	}

}
