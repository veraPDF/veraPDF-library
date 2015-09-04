package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDDestinationOrAction;
import org.apache.pdfbox.pdmodel.interactive.action.*;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.*;
import org.verapdf.model.pdlayer.PDAction;

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
        List<PDOutline> result = new ArrayList<>();
        List<PDOutlineItem> outlines = this.getOutlinesList();
        for (PDOutlineItem outlineItem : outlines) {
            result.add(new PBoxPDOutline(outlineItem));
        }
        outlines.clear();
        return result;
    }

    private List<PDOutlineItem> getOutlinesList() {
        List<PDOutlineItem> result = new ArrayList<>();
        PDDocumentOutline documentOutline = this.document
				.getDocumentCatalog().getDocumentOutline();

        if (documentOutline != null) {
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
        }

        return result;
    }

    private List<PDAction> getOpenAction() {
        List<PDAction> actions = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        try {
            PDDestinationOrAction openAction = this.document.getDocumentCatalog()
                    .getOpenAction();
            if (openAction instanceof org.apache.pdfbox.pdmodel.interactive.action.PDAction) {
                this.addAction(
                        actions,
                        (org.apache.pdfbox.pdmodel.interactive.action.PDAction) openAction);
            }
        } catch (IOException e) {
            LOGGER.error(
                    "Problems with open action obtaining. " + e.getMessage(), e);
        }
        return actions;
    }

    private List<PDAction> getActions() {
        List<PDAction> actions = new ArrayList<>(MAX_NUMBER_OF_ACTIONS);
        PDDocumentCatalogAdditionalActions pbActions = this.document
                .getDocumentCatalog().getActions();
        if (pbActions != null) {
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
        }
        return actions;
    }

    private List<PDPage> getPages() {
        PDPageTree pageTree = this.document.getPages();
        List<PDPage> pages = new ArrayList<>(pageTree.getCount());
        for (org.apache.pdfbox.pdmodel.PDPage page : pageTree) {
            if (page != null) {
                pages.add(new PBoxPDPage(page));
            }
        }
        return pages;
    }

    private List<PDMetadata> getMetadata() {
        List<PDMetadata> metadata = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
        org.apache.pdfbox.pdmodel.common.PDMetadata meta = this.document
                .getDocumentCatalog().getMetadata();
        if (meta != null && meta.getCOSObject() != null) {
            metadata.add(new PBoxPDMetadata(meta, Boolean.TRUE));
        }
        return metadata;
    }

    private List<PDOutputIntent> getOutputIntents() {
        List<PDOutputIntent> outputIntents = new ArrayList<>();
        List<org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent> pdfboxOutputIntents =
				this.document.getDocumentCatalog().getOutputIntents();
        for (org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent intent : pdfboxOutputIntents) {
            if (intent != null) {
                outputIntents.add(new PBoxPDOutputIntent(intent));
            }
        }
        return outputIntents;
    }

    private List<PDAcroForm> getAcroForms() {
        List<PDAcroForm> forms = new ArrayList<>();
        org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm form =
				this.document.getDocumentCatalog().getAcroForm();
        if (form != null) {
            forms.add(new PBoxPDAcroForm(form));
        }
        return forms;
    }
}
