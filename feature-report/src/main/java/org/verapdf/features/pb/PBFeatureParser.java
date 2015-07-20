package org.verapdf.features.pb;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDNameTreeNode;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesReporter;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

import java.io.IOException;

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

                reportEmbeddedFiles(catalog, reporter);

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

    private static void reportEmbeddedFiles(PDDocumentCatalog catalog, FeaturesReporter reporter) {
        if (catalog.getNames() != null && catalog.getNames().getEmbeddedFiles() != null) {
            PDEmbeddedFilesNameTreeNode efTree = catalog.getNames().getEmbeddedFiles();

            int index = 0;

            try {
                if (efTree.getNames() != null) {
                    for (PDComplexFileSpecification file : efTree.getNames().values()) {
                        reporter.report(PBFeaturesObjectCreator.createEmbeddedFileFeaturesObject(file, ++index));
                    }
                }
            } catch (IOException e) {
                try {
                    FeatureTreeNode embeddedFileNode = FeatureTreeNode.newInstance("embeddedFile", null);
                    embeddedFileNode.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.PARSINGEMBEDDEDFILEERROR_ID);
                    ErrorsHelper.addErrorIntoCollection(reporter.getCollection(),
                            ErrorsHelper.PARSINGEMBEDDEDFILEERROR_ID, ErrorsHelper.PARSINGEMBEDDEDFILEERROR_MESSAGE);
                } catch (FeaturesTreeNodeException e1) {
                    // This exception occurs when wrong node creates for feature tree.
                    // The logic of the method guarantees this doesn't occur.
                }
            }

            if (efTree.getKids() != null) {
                for (PDNameTreeNode<PDComplexFileSpecification> tree : efTree.getKids()) {
                    if (tree != null) {
                        index = reportEmbeddedFileNode(tree, index, reporter);
                    }
                }
            }
        }
    }

    private static int reportEmbeddedFileNode(PDNameTreeNode<PDComplexFileSpecification> node, int index, FeaturesReporter reporter) {
        int res = index;

        try {
            if (node.getNames() != null) {
                for (PDComplexFileSpecification file : node.getNames().values()) {
                    if (file != null) {
                        reporter.report(PBFeaturesObjectCreator.createEmbeddedFileFeaturesObject(file, ++res));
                    }
                }
            }
        } catch (IOException e) {
            try {
                FeatureTreeNode embeddedFileNode = FeatureTreeNode.newInstance("embeddedFile", null);
                embeddedFileNode.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.PARSINGEMBEDDEDFILEERROR_ID);
                ErrorsHelper.addErrorIntoCollection(reporter.getCollection(),
                        ErrorsHelper.PARSINGEMBEDDEDFILEERROR_ID, ErrorsHelper.PARSINGEMBEDDEDFILEERROR_MESSAGE);
            } catch (FeaturesTreeNodeException e1) {
                // This exception occurs when wrong node creates for feature tree.
                // The logic of the method guarantees this doesn't occur.
            }
        }

        if (node.getKids() != null) {
            for (PDNameTreeNode<PDComplexFileSpecification> tree : node.getKids()) {
                res = reportEmbeddedFileNode(tree, res, reporter);
            }
        }

        return res;
    }
}
