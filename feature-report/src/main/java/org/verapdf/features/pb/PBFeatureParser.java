package org.verapdf.features.pb;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDNameTreeNode;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.verapdf.exceptions.featurereport.FeaturesTreeNodeException;
import org.verapdf.features.FeaturesObjectTypesEnum;
import org.verapdf.features.FeaturesReporter;
import org.verapdf.features.tools.ErrorsHelper;
import org.verapdf.features.tools.FeatureTreeNode;
import org.verapdf.features.tools.FeaturesCollection;

/**
 * Parses PDFBox PDDocument to generate features collection
 *
 * @author Maksim Bezrukov
 */
public final class PBFeatureParser {

    private static final Logger LOGGER = Logger
            .getLogger(PBFeatureParser.class);
    private static final String PAGE = "page";

    private PBFeatureParser() {
        // Ensure no public default constructor
    }

    /**
     * Parses the document and returns Feature collection by using default
     * Features Reporter
     *
     * @param document
     *            a PDDocument to parse for features
     * @return FeaturesCollection class with information about all featurereport
     */
    public static FeaturesCollection getFeaturesCollection(
            final PDDocument document) {
        return getFeaturesCollection(document, new FeaturesReporter());
    }

    /**
     * Parses the document and returns Feature collection by using given
     * Features Reporter
     *
     * @param document
     *            - the document for parsing
     * @param reporter
     *            - Features Reporter for report
     * @return FeaturesCollection class with information about all featurereport
     */
    public static FeaturesCollection getFeaturesCollection(
            final PDDocument document, final FeaturesReporter reporter) {

        if (document != null) {
            getDocumentFeatures(document, reporter);
        }

        return reporter.getCollection();
    }

    private static void getDocumentFeatures(final PDDocument document,
            final FeaturesReporter reporter) {
        reporter.report(PBFeaturesObjectCreator
                .createInfoDictFeaturesObject(document.getDocumentInformation()));

        reporter.report(PBFeaturesObjectCreator
                .createDocSecurityFeaturesObject(document.getEncryption()));

        PDDocumentCatalog catalog = document.getDocumentCatalog();
        if (catalog != null) {
            getCatalogFeatures(catalog, reporter);
        }

        reporter.report(PBFeaturesObjectCreator
                .createLowLvlInfoFeaturesObject(document.getDocument()));

    }

    private static void getCatalogFeatures(final PDDocumentCatalog catalog,
            final FeaturesReporter reporter) {
        reporter.report(PBFeaturesObjectCreator
                .createMetadataFeaturesObject(catalog.getMetadata()));
        reporter.report(PBFeaturesObjectCreator
                .createOutlinesFeaturesObject(catalog.getDocumentOutline()));

        if (catalog.getNames() != null
                && catalog.getNames().getEmbeddedFiles() != null) {
            reportEmbeddedFiles(catalog, reporter);
        }

        if (catalog.getOutputIntents() != null) {
            for (PDOutputIntent outInt : catalog.getOutputIntents()) {
                reporter.report(PBFeaturesObjectCreator
                        .createOutputIntentFeaturesObject(outInt));
            }
        }

        Map<String, PDAnnotation> annots = new HashMap<>();
        Map<String, Set<String>> annotPages = new HashMap<>();
        Map<String, String> annotChild = new HashMap<>();
        Map<String, String> annotParent = new HashMap<>();

        PDPageTree pageTree = catalog.getPages();
        if (pageTree != null) {
            getPageTreeFeatures(pageTree, annots, annotPages, annotChild, annotParent, reporter);
        }

        for (Map.Entry<String, PDAnnotation> annotEntry : annots.entrySet()) {
            if (annotEntry.getValue() != null) {
                String id = annotEntry.getKey();
                reporter.report(PBFeaturesObjectCreator
                        .createAnnotFeaturesObject(annotEntry.getValue(), id,
                                annotPages.get(id), annotParent.get(id),
                                annotChild.get(id), null));
                // TODO: replace null with appearance id of the XObject
            }
        }
    }

    private static void getPageTreeFeatures(final PDPageTree pageTree,  final Map<String, PDAnnotation> annots,
            final Map<String, Set<String>> annotPages,
            final Map<String, String> annotChild,
            final Map<String, String> annotParent, final FeaturesReporter reporter) {
        for (PDPage page : pageTree) {

            int pageIndex = pageTree.indexOf(page) + 1;
            Set<String> annotsId = addAnnotsDependencies(page, pageIndex,
                    annots, annotPages, annotChild, annotParent,
                    reporter.getCollection());

            reporter.report(PBFeaturesObjectCreator
                    .createPageFeaturesObject(page, annotsId, PAGE
                            + pageIndex, pageIndex));
        }
    }
    
    private static Set<String> addAnnotsDependencies(final PDPage page,
            final int pageIndex, final Map<String, PDAnnotation> annots,
            final Map<String, Set<String>> annotPages,
            final Map<String, String> annotChild,
            final Map<String, String> annotParent,
            final FeaturesCollection collection) {

        COSArray annotsArray = (COSArray) page.getCOSObject()
                .getDictionaryObject(COSName.ANNOTS);

        if (annotsArray == null) {
            return Collections.emptySet();
        }
        Set<String> annotsId = new HashSet<>();

        for (int i = 0; i < annotsArray.size(); ++i) {
            COSBase item = annotsArray.get(i);
            if (item != null) {
                String id = getId(item, "annot", annots.keySet().size());
                annotsId.add(id);

                if (annotPages.get(id) == null) {
                    annotPages.put(id, new HashSet<String>());
                }
                annotPages.get(id).add(PAGE + pageIndex);

                COSBase base = getBase(item);

                try {
                    PDAnnotation annotation = PDAnnotation
                            .createAnnotation(base);
                    annots.put(id, annotation);
                    COSBase pop = annotation.getCOSObject().getItem(
                            COSName.getPDFName("Popup"));

                    if (pop != null) {
                        addPopup(pop, id, annots, annotChild, annotParent,
                                collection);
                    }
                } catch (IOException e) {
                    LOGGER.debug("Unknown annotation type detected.", e);
                    annots.put(id, null);
                    generateUnknownAnnotation(collection, id);
                }
            }
        }

        return annotsId;
    }

    private static COSBase getBase(final COSBase base) {
        COSBase item = base;

        while (item instanceof COSObject) {
            item = ((COSObject) item).getObject();
        }

        return item;
    }

    private static String getId(final COSBase base, final String prefix,
            final long number) {
        long numb = number;
        COSBase item = base;
        String type = "Dir";

        while (item instanceof COSObject) {
            numb = ((COSObject) item).getObjectNumber();
            type = "Indir";
            item = ((COSObject) item).getObject();
        }

        return prefix + type + numb;
    }

    private static void addPopup(final COSBase item, final String parentId,
            final Map<String, PDAnnotation> annots,
            final Map<String, String> annotChild,
            final Map<String, String> annotParent,
            final FeaturesCollection collection) {
        String id = getId(item, "annot", annots.keySet().size());
        COSBase base = getBase(item);

        annotChild.put(parentId, id);
        annotParent.put(id, parentId);

        PDAnnotation annotation = null;
        try {
            annotation = PDAnnotation.createAnnotation(base);
        } catch (IOException e) {
            LOGGER.debug("Unknown annotation type detected.", e);
            generateUnknownAnnotation(collection, id);
        }
        annots.put(id, annotation);
    }

    private static void generateUnknownAnnotation(
            final FeaturesCollection collection, final String id) {
        try {
            FeatureTreeNode annot = FeatureTreeNode
                    .newRootInstance("annotation");
            annot.addAttribute("id", id);
            annot.addAttribute(ErrorsHelper.ERRORID,
                    ErrorsHelper.ANNOTATIONPARSER_ID);
            ErrorsHelper.addErrorIntoCollection(collection,
                    ErrorsHelper.ANNOTATIONPARSER_ID,
                    ErrorsHelper.ANNOTATIONPARSER_MESSAGE);
            collection.addNewFeatureTree(FeaturesObjectTypesEnum.ANNOTATION,
                    annot);
        } catch (FeaturesTreeNodeException e) {
            // This exception occurs when wrong node creates for feature tree.
            // The logic of the method guarantees this doesn't occur.
            String message = "PBFeatureParser.generateUnknownAnnotation logic failure.";
            LOGGER.fatal(message, e);
            throw new IllegalStateException(message, e);
        }

    }

    private static void reportEmbeddedFiles(final PDDocumentCatalog catalog,
            final FeaturesReporter reporter) {
        int index = 0;
        PDEmbeddedFilesNameTreeNode efTree = catalog.getNames()
                .getEmbeddedFiles();

        try {
            if (efTree.getNames() != null) {
                for (PDComplexFileSpecification file : efTree.getNames()
                        .values()) {
                    reporter.report(PBFeaturesObjectCreator
                            .createEmbeddedFileFeaturesObject(file, ++index));
                }
            }
        } catch (IOException e) {
            LOGGER.debug("Error creating PDFBox SubType.", e);
            try {
                FeatureTreeNode embeddedFileNode = FeatureTreeNode
                        .newRootInstance("embeddedFile");
                embeddedFileNode.addAttribute(ErrorsHelper.ERRORID,
                        ErrorsHelper.PARSINGEMBEDDEDFILEERROR_ID);
                ErrorsHelper.addErrorIntoCollection(reporter.getCollection(),
                        ErrorsHelper.PARSINGEMBEDDEDFILEERROR_ID,
                        ErrorsHelper.PARSINGEMBEDDEDFILEERROR_MESSAGE);
            } catch (FeaturesTreeNodeException e1) {
                // This exception occurs when wrong node creates for feature
                // tree.
                // The logic of the method guarantees this doesn't occur.
                String message = "PBFeatureParser.reportEmbeddedFiles logic failure.";
                LOGGER.fatal(message, e1);
                throw new IllegalStateException(message, e1);
            }
        }

        if (efTree.getKids() != null) {
            for (PDNameTreeNode<PDComplexFileSpecification> tree : efTree
                    .getKids()) {
                if (tree != null) {
                    index = reportEmbeddedFileNode(tree, index, reporter);
                }
            }
        }
    }

    private static int reportEmbeddedFileNode(
            final PDNameTreeNode<PDComplexFileSpecification> node,
            final int index, final FeaturesReporter reporter) {
        int res = index;

        try {
            if (node.getNames() != null) {
                for (PDComplexFileSpecification file : node.getNames().values()) {
                    if (file != null) {
                        reporter.report(PBFeaturesObjectCreator
                                .createEmbeddedFileFeaturesObject(file, ++res));
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.debug("Subtype creation exception caught", e);
            handleSubtypeCreationProblem(reporter);
        }

        if (node.getKids() != null) {
            for (PDNameTreeNode<PDComplexFileSpecification> tree : node
                    .getKids()) {
                res = reportEmbeddedFileNode(tree, res, reporter);
            }
        }

        return res;
    }

    private static void handleSubtypeCreationProblem(
            final FeaturesReporter reporter) {
        try {
            FeatureTreeNode embeddedFileNode = FeatureTreeNode
                    .newRootInstance("embeddedFile");
            embeddedFileNode.addAttribute(ErrorsHelper.ERRORID,
                    ErrorsHelper.PARSINGEMBEDDEDFILEERROR_ID);
            ErrorsHelper.addErrorIntoCollection(reporter.getCollection(),
                    ErrorsHelper.PARSINGEMBEDDEDFILEERROR_ID,
                    ErrorsHelper.PARSINGEMBEDDEDFILEERROR_MESSAGE);
        } catch (FeaturesTreeNodeException e) {
            // This exception occurs when wrong node creates for feature
            // tree.
            // The logic of the method guarantees this doesn't occur.
            // In which case we throw an IllegalStateException as if this
            // occurs
            // we want to know there's something wrong with our logic
            String message = "PBFeatureParser.reportEmbeddedFileNode logic failure.";
            LOGGER.fatal(message, e);
            throw new IllegalStateException(message, e);
        }
    }
}
