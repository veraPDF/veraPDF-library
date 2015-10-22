package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDDestinationOrAction;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;
import org.apache.pdfbox.pdmodel.interactive.action.*;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.*;
import org.verapdf.model.pdlayer.PDAction;

import java.io.IOException;
import java.util.*;

/**
 * High-level representation of pdf document.
 * Implemented by Apache PDFBox
 *
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDocument extends PBoxPDObject implements PDDocument {

    private static final Logger LOGGER = Logger.getLogger(PBoxPDDocument.class);

	/** Type name for {@code PBoxPDDocument} */
	public static final String PD_DOCUMENT_TYPE = "PDDocument";

	/** Link name for pages */
	public static final String PAGES = "pages";
	/** Link name for main metadata of document*/
	public static final String METADATA = "metadata";
	/** Link name for all output intents */
	public static final String OUTPUT_INTENTS = "outputIntents";
	/** Link name for acro forms */
	public static final String ACRO_FORMS = "AcroForm";
	/** Link name for additional actions of document */
	public static final String ACTIONS = "AA";
	/** Link name for open action of document */
	public static final String OPEN_ACTION = "OpenAction";
	/** Link name for all outlines of document */
    public static final String OUTLINES = "Outlines";
	/** Link name for annotations structure tree root of document */
	public static final String STRUCTURE_TREE_ROOT = "StructTreeRoot";

	/** Maximal number of additional actions for AA key */
	public static final int MAX_NUMBER_OF_ACTIONS = 5;

	/**
	 * Default constructor
	 * @param document high level document representation
	 */
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
			case STRUCTURE_TREE_ROOT:
				return this.getStructureTreeRoot();
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
		PDDocumentCatalogAdditionalActions pbActions = this.getAdditionalAction();
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

	private PDDocumentCatalogAdditionalActions getAdditionalAction() {
		COSDictionary catalog = this.document.getDocumentCatalog().getCOSObject();
		COSBase aaDictionary = catalog.getDictionaryObject(COSName.AA);
		return !(aaDictionary instanceof COSDictionary) ? null :
				new PDDocumentCatalogAdditionalActions((COSDictionary) aaDictionary);
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

	private List<PDStructTreeRoot> getStructureTreeRoot() {
		PDStructureTreeRoot root = this.document.getDocumentCatalog().getStructureTreeRoot();
		if (root != null) {
			List<PDStructTreeRoot> treeRoot = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
			treeRoot.add(new PBoxPDStructTreeRoot(root));
			return Collections.unmodifiableList(treeRoot);
		}
		return Collections.emptyList();
	}

}
