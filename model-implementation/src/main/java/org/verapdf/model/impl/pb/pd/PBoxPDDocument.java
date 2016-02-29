package org.verapdf.model.impl.pb.pd;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.*;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDDestinationOrAction;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureTreeRoot;
import org.apache.pdfbox.pdmodel.interactive.action.PDDocumentCatalogAdditionalActions;
import org.verapdf.model.baselayer.Object;
import org.verapdf.model.coslayer.CosDict;
import org.verapdf.model.impl.pb.cos.PBCosDict;
import org.verapdf.model.pdlayer.*;
import org.verapdf.model.tools.OutlinesHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	/** Link name for alternate presentation of names tree of document */
	public static final String ALTERNATE_PRESENTATIONS = "AlternatePresentations";

	/** Maximal number of additional actions for AA key */
	public static final int MAX_NUMBER_OF_ACTIONS = 5;

	private final PDDocumentCatalog catalog;

	/**
	 * Default constructor
	 * @param document high level document representation
	 */
    public PBoxPDDocument(org.apache.pdfbox.pdmodel.PDDocument document) {
        super(document, PD_DOCUMENT_TYPE);
		this.catalog = this.getDocumentCatalog();
    }

	private PDDocumentCatalog getDocumentCatalog() {
		try {
			COSDictionary object = (COSDictionary)
					this.document.getDocument().getCatalog().getObject();
			return new PDDocumentCatalog(this.document, object);
		} catch (IOException e) {
			LOGGER.warn("Problems with catalog obtain.", e);
		}
		return null;
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
			case ALTERNATE_PRESENTATIONS:
				return this.getAlternatePresentations();
			default:
				return super.getLinkedObjects(link);
		}
	}

	private List<PDOutline> getOutlines() {
		return OutlinesHelper.getOutlines(this.catalog);
	}

	private List<PDAction> getOpenAction() {
		if (this.catalog != null) {
			try {
				PDDestinationOrAction openAction = this.catalog.getOpenAction();
				if (openAction instanceof org.apache.pdfbox.pdmodel.interactive.action.PDAction) {
					List<PDAction> actions = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
					this.addAction(actions,
							(org.apache.pdfbox.pdmodel.interactive.action.PDAction) openAction);
					return Collections.unmodifiableList(actions);
				}
			} catch (IOException e) {
				LOGGER.error(
						"Problems with open action obtaining. " + e.getMessage(), e);
			}
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
		if (this.catalog != null) {
			COSDictionary catalog = this.catalog.getCOSObject();
			COSBase aaDictionary = catalog.getDictionaryObject(COSName.AA);
			if (aaDictionary instanceof COSDictionary) {
				return new PDDocumentCatalogAdditionalActions((COSDictionary) aaDictionary);
			}
		}
		return null;
	}

	private List<PDPage> getPages() {
		PDPageTree pageTree = this.document.getPages();
		List<PDPage> pages = new ArrayList<>(pageTree.getCount());
		for (org.apache.pdfbox.pdmodel.PDPage page : pageTree) {
			pages.add(new PBoxPDPage(page));
		}
		return Collections.unmodifiableList(pages);
	}

    private List<PDMetadata> getMetadata() {
		if (this.catalog != null) {
			org.apache.pdfbox.pdmodel.common.PDMetadata meta = this.catalog.getMetadata();
			if (meta != null && meta.getCOSObject() != null) {
				List<PDMetadata> metadata = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				metadata.add(new PBoxPDMetadata(meta, Boolean.TRUE));
				return Collections.unmodifiableList(metadata);
			}
		}
        return Collections.emptyList();
    }

    private List<PDOutputIntent> getOutputIntents() {
		if (this.catalog != null) {
			List<org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent> pdfboxOutputIntents =
					this.catalog.getOutputIntents();
			List<PDOutputIntent> outputIntents = new ArrayList<>(pdfboxOutputIntents.size());
			for (org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent intent : pdfboxOutputIntents) {
				outputIntents.add(new PBoxPDOutputIntent(intent));
			}
			return Collections.unmodifiableList(outputIntents);
		}
		return Collections.emptyList();
    }

    private List<PDAcroForm> getAcroForms() {
		if (this.catalog != null) {
			org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm form =
					this.catalog.getAcroForm();
			if (form != null) {
				List<PDAcroForm> forms = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				forms.add(new PBoxPDAcroForm(form));
				return Collections.unmodifiableList(forms);
			}
		}
        return Collections.emptyList();
    }

	private List<PDStructTreeRoot> getStructureTreeRoot() {
		if (this.catalog != null) {
			PDStructureTreeRoot root = this.catalog.getStructureTreeRoot();
			if (root != null) {
				List<PDStructTreeRoot> treeRoot = new ArrayList<>(MAX_NUMBER_OF_ELEMENTS);
				treeRoot.add(new PBoxPDStructTreeRoot(root));
				return Collections.unmodifiableList(treeRoot);
			}
		}
		return Collections.emptyList();
	}

	private List<CosDict> getAlternatePresentations() {
		if (this.catalog != null) {
			COSDictionary buffer = this.catalog.getCOSObject();
			buffer = (COSDictionary) buffer.getDictionaryObject(
					COSName.NAMES);
			if (buffer != null) {
				COSName key = COSName.getPDFName(ALTERNATE_PRESENTATIONS);
				COSBase base = buffer.getDictionaryObject(key);
				if (base instanceof COSDictionary) {
					List<CosDict> presentations = new ArrayList<>();
					this.getAlternatePresentations(presentations, (COSDictionary) base);
					return Collections.unmodifiableList(presentations);
				}
			}
		}
		return Collections.emptyList();
	}

	private void getAlternatePresentations(List<CosDict> presentations, COSDictionary base) {
		COSBase tmp = base.getDictionaryObject(COSName.NAMES);
		this.getAlternatesDictionaries(presentations, tmp);
		tmp = base.getDictionaryObject(COSName.RESOURCES);
		if (tmp instanceof COSDictionary) {
			this.getAlternatesDictionaries(presentations,
					((COSDictionary) tmp).getDictionaryObject(COSName.NAMES));
		}
	}

	private void getAlternatesDictionaries(List<CosDict> presentations, COSBase names) {
		if (names instanceof COSArray) {
			COSArray array = (COSArray) names;
			for (int i = 1; i < array.size(); i += 2) {
				COSBase element = array.get(i);
				if (element instanceof COSObject) {
					element = ((COSObject) element).getObject();
				}
				if (element instanceof COSDictionary) {
					presentations.add(new PBCosDict((COSDictionary) element));
					this.getAlternatePresentations(presentations, (COSDictionary) element);
				}
			}
		}
	}

}
