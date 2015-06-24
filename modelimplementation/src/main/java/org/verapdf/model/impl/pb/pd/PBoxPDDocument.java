package org.verapdf.model.impl.pb.pd;

import org.verapdf.model.baselayer.Object;
import org.verapdf.model.pdlayer.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evgeniy Muravitskiy
 */
public class PBoxPDDocument extends PBoxPDObject implements PDDocument {

    public static final String PAGES = "pages";
    public static final String METADATA = "metadata";
    public static final String OUTPUT_INTENTS = "outputIntents";
    public static final String ACRO_FORMS = "acroForms";

    public PBoxPDDocument(org.apache.pdfbox.pdmodel.PDDocument document) {
        super(document);
        setType("PDDocument");
    }

    @Override
    public List<? extends Object> getLinkedObjects(String link) {
        List<? extends Object> list;

        switch (link) {
            case PAGES:
                list = getPages();
                break;
            case METADATA:
                list = getMetadata();
                break;
            case OUTPUT_INTENTS:
                list = getOutputIntents();
                break;
            case ACRO_FORMS:
                list = getAcroForms();
                break;
            default:
                list = super.getLinkedObjects(link);
                break;
        }

        return list;
    }

    private List<PDPage> getPages() {
        List<PDPage> pages = new ArrayList<>();
        for (org.apache.pdfbox.pdmodel.PDPage page : document.getPages()) {
            if (page != null) {
                pages.add(new PBoxPDPage(page));
            }
        }
        return pages;
    }

    private List<PDMetadata> getMetadata() {
        List<PDMetadata> metadata = new ArrayList<>(1);
        final org.apache.pdfbox.pdmodel.common.PDMetadata meta = document.getDocumentCatalog().getMetadata();
        if (meta != null) {
            metadata.add(new PBoxPDMetadata(meta));
        }
        return new ArrayList<>();
    }

    private List<PDOutputIntent> getOutputIntents() {
        List<PDOutputIntent> outputIntents = new ArrayList<>();
        final List<org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent> pdfboxOutputIntents =
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
        return new ArrayList<>();
    }
}
