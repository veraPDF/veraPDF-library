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
        super(document);
        setType(PD_DOCUMENT_TYPE);
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        List<? extends Object> list;

        switch (link) {
			case OUTLINES:
				list = this.getOutlines();
				break;
			case OPEN_ACTION:
				list = this.getOpenAction();
				break;
			case ACTIONS:
				list = this.getActions();
				break;
            case PAGES:
                list = this.getPages();
                break;
            case METADATA:
                list = this.getMetadata();
                break;
            case OUTPUT_INTENTS:
                list = this.getOutputIntents();
                break;
            case ACRO_FORMS:
                list = this.getAcroForms();
                break;
            default:
                list = super.getLinkedObjects(link);
                break;
        }

        return list;
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
		PDDocumentOutline documentOutline = document.getDocumentCatalog().getDocumentOutline();

		if (documentOutline != null) {
			PDOutlineItem firstChild = documentOutline.getFirstChild();
			Stack<PDOutlineItem> stack = new Stack<>();

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
		List<PDAction> actions = new ArrayList<>(1);
		try {
			PDDestinationOrAction openAction = document.getDocumentCatalog().getOpenAction();
			if (openAction instanceof org.apache.pdfbox.pdmodel.interactive.action.PDAction) {
				addAction(actions, (org.apache.pdfbox.pdmodel.interactive.action.PDAction) openAction);
			}
		} catch (IOException e) {
			LOGGER.error("Problems with open action obtaining. " + e.getMessage(), e);
		}
		return actions;
	}

	private List<PDAction> getActions() {
		List<PDAction> actions = new ArrayList<>(MAX_NUMBER_OF_ACTIONS);
		PDDocumentCatalogAdditionalActions pbActions = document.getDocumentCatalog().getActions();
		if (pbActions != null) {
			org.apache.pdfbox.pdmodel.interactive.action.PDAction buffer;

			buffer = pbActions.getDP();
			addAction(actions, buffer);

			buffer = pbActions.getDS();
			addAction(actions, buffer);

			buffer = pbActions.getWP();
			addAction(actions, buffer);

			buffer = pbActions.getWS();
			addAction(actions, buffer);

			buffer = pbActions.getWC();
			addAction(actions, buffer);
		}
		return actions;
	}

	private List<PDPage> getPages() {
		PDPageTree pageTree = document.getPages();
		List<PDPage> pages = new ArrayList<>(pageTree.getCount());
		for (org.apache.pdfbox.pdmodel.PDPage page : pageTree) {
            if (page != null) {
                pages.add(new PBoxPDPage(page));
            }
        }
        return pages;
    }

    private List<PDMetadata> getMetadata() {
        List<PDMetadata> metadata = new ArrayList<>(1);
        org.apache.pdfbox.pdmodel.common.PDMetadata meta = document.getDocumentCatalog().getMetadata();
        if (meta != null && meta.getCOSObject() != null) {
            metadata.add(new PBoxPDMetadata(meta, Boolean.TRUE));
        }
        return metadata;
    }

    private List<PDOutputIntent> getOutputIntents() {
        List<PDOutputIntent> outputIntents = new ArrayList<>();
        List<org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent> pdfboxOutputIntents =
                                                                document.getDocumentCatalog().getOutputIntents();
        for (org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent intent : pdfboxOutputIntents) {
            if (intent != null) {
                outputIntents.add(new PBoxPDOutputIntent(intent));
            }
        }
        return outputIntents;
    }

    private List<PDAcroForm> getAcroForms() {
        List<PDAcroForm> forms = new ArrayList<>();
        final org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm form = document.getDocumentCatalog().getAcroForm();
        if (form != null) {
            forms.add(new PBoxPDAcroForm(form));
        }
        return forms;
    }
}
