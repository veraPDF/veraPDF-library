package org.verapdf.features.pb;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.verapdf.features.FeaturesReporter;
import org.verapdf.features.tools.FeaturesCollection;

/**
 * Parses PDFBox PDDocument to generate features collection
 *
 * @author Maksim Bezrukov
 */
public final class PBFeatureParser {

    private PBFeatureParser() {
    }

    /**
     * Parses the document and returns Feature collection by using default Features Reporter
     *
     * @param document - the document for parsing
     * @return FeaturesCollection class with information about all featurereport
     */
    public static FeaturesCollection getFeaturesCollection(PDDocument document) {
        return getFeaturesCollection(document, new FeaturesReporter());
    }

    /**
     * Parses the document and returns Feature collection by using given Features Reporter
     *
     * @param document - the document for parsing
     * @param reporter - Features Reporter for report
     * @return FeaturesCollection class with information about all featurereport
     */
    public static FeaturesCollection getFeaturesCollection(PDDocument document, FeaturesReporter reporter) {

        if (document != null) {
            reporter.report(PBFeaturesObjectCreator.createInfoDictFeaturesObject(document.getDocumentInformation()));

            PDDocumentCatalog catalog = document.getDocumentCatalog();
            if (catalog != null) {
                reporter.report(PBFeaturesObjectCreator.createMetadataFeaturesObject(catalog.getMetadata()));
                reporter.report(PBFeaturesObjectCreator.createOutlinesFeaturesObject(catalog.getDocumentOutline()));

                if (catalog.getOutputIntents() != null) {
                    for (PDOutputIntent outInt : document.getDocumentCatalog().getOutputIntents()) {
                        reporter.report(PBFeaturesObjectCreator.createOutputIntentFeaturesObject(outInt));
                    }
                }

                PDPageTree pageTree = catalog.getPages();
                if (pageTree != null) {
                    for (PDPage page : pageTree) {
                        reporter.report(PBFeaturesObjectCreator.createPageFeaturesObject(page, pageTree.indexOf(page) + 1));
                    }
                }
            }

            reporter.report(PBFeaturesObjectCreator.createLowLvlInfoFeaturesObject(document.getDocument()));

        }

        return reporter.getCollection();
    }
}
