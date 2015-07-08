package org.verapdf.features.pb;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
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

        reporter.report(PBFeaturesObjectCreator.createInfoDictFeaturesObject(document.getDocumentInformation()));
        
        reporter.report(PBFeaturesObjectCreator.createMetadataFeaturesObject(document.getDocumentCatalog().getMetadata()));

        for (PDOutputIntent outInt : document.getDocumentCatalog().getOutputIntents()) {
            reporter.report(PBFeaturesObjectCreator.createOutputIntentFeaturesObject(outInt));
        }

        for (PDPage page : document.getPages()) {
            reporter.report(PBFeaturesObjectCreator.createPageFeaturesObject(page, document.getPages().indexOf(page) + 1));
        }

        return reporter.getCollection();
    }
}
