package org.verapdf.features.pb;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.pdmodel.*;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Parses PDFBox PDDocument to generate features collection
 *
 * @author Maksim Bezrukov
 */
public final class PBFeatureParser {

    private static final String PAGE = "page";

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

        Map<String, PDAnnotation> annots = new HashMap<>();
        Map<String, Set<String>> annotPages = new HashMap<>();
        Map<String, String> annotChild = new HashMap<>();
        Map<String, String> annotParent = new HashMap<>();

        if (document != null) {
            reporter.report(PBFeaturesObjectCreator.createInfoDictFeaturesObject(document.getDocumentInformation()));
            reporter.report(PBFeaturesObjectCreator.createDocSecurityFeaturesObject(document.getEncryption()));

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

                        int pageIndex = pageTree.indexOf(page) + 1;
                        Set<String> annotsId = addAnnotsDependencies(page, pageIndex, annots, annotPages, annotChild, annotParent, reporter.getCollection());

                        reporter.report(PBFeaturesObjectCreator.createPageFeaturesObject(page, annotsId, PAGE + pageIndex, pageIndex));
                    }
                }

                for (Map.Entry<String, PDAnnotation> annotEntry : annots.entrySet()) {
                    if (annotEntry.getValue() != null) {
                        String id = annotEntry.getKey();
                        reporter.report(PBFeaturesObjectCreator.createAnnotFeaturesObject(annotEntry.getValue(), id,
                                annotPages.get(id), annotParent.get(id), annotChild.get(id), null));
                        // TODO: replace null with appearance id of the XObject
                    }
                }
            }

            reporter.report(PBFeaturesObjectCreator.createLowLvlInfoFeaturesObject(document.getDocument()));

        }

        return reporter.getCollection();
    }

    private static Set<String> addAnnotsDependencies(PDPage page, int pageIndex, Map<String, PDAnnotation> annots,
                                                     Map<String, Set<String>> annotPages, Map<String, String> annotChild,
                                                     Map<String, String> annotParent, FeaturesCollection collection) {

        COSArray annotsArray = (COSArray) page.getCOSObject().getDictionaryObject(COSName.ANNOTS);

        if (annotsArray == null) {
            return null;
        } else {
            Set<String> annotsId = new HashSet<>();

            for (int i = 0; i < annotsArray.size(); ++i) {
                COSBase item = annotsArray.get(i);
                if (item != null) {
                    String id = getId(item, "annot", annots.keySet().size());

                    if (annotPages.get(id) == null) {
                        annotPages.put(id, new HashSet<String>());
                    }
                    annotPages.get(id).add(PAGE + pageIndex);

                    COSBase base = getBase(item);

                    PDAnnotation annotation = null;
                    try {
                        annotation = PDAnnotation.createAnnotation(base);

                        COSBase pop = annotation.getCOSObject().getItem(COSName.getPDFName("Popup"));

                        if (pop != null) {
                            addPopup(pop, id, annots, annotChild, annotParent, collection);
                        }
                    } catch (IOException e) {
                        generateUnknownAnnotation(collection, id);
                    }
                    annots.put(id, annotation);
                }
            }

            return annotsId;
        }
    }

    private static COSBase getBase(COSBase base) {
        COSBase item = base;

        while (item instanceof COSObject) {
            item = ((COSObject) item).getObject();
        }

        return item;
    }

    private static String getId(COSBase base, String prefix, int number) {
        int numb = number;
        COSBase item = base;
        String type = "Dir";

        while (item instanceof COSObject) {
            numb = ((COSObject) item).getGenerationNumber();
            type = "Indir";
            item = ((COSObject) item).getObject();
        }

        return prefix + type + numb;
    }

    private static void addPopup(COSBase item, String parentId, Map<String, PDAnnotation> annots,
                                 Map<String, String> annotChild, Map<String, String> annotParent, FeaturesCollection collection) {
        String id = getId(item, "annot", annots.keySet().size());
        COSBase base = getBase(item);

        annotChild.put(parentId, id);
        annotParent.put(id, parentId);

        PDAnnotation annotation = null;
        try {
            annotation = PDAnnotation.createAnnotation(base);
        } catch (IOException e) {
            generateUnknownAnnotation(collection, id);
        }
        annots.put(id, annotation);
    }

    private static void generateUnknownAnnotation(FeaturesCollection collection, String id) {
        try {
            FeatureTreeNode annot = FeatureTreeNode.newInstance("annotation", null);
            annot.addAttribute("id", id);
            annot.addAttribute(ErrorsHelper.ERRORID, ErrorsHelper.ANNOTATIONPARSER_ID);
            ErrorsHelper.addErrorIntoCollection(collection, ErrorsHelper.ANNOTATIONPARSER_ID, ErrorsHelper.ANNOTATIONPARSER_MESSAGE);
            collection.addNewFeatureTree(FeaturesObjectTypesEnum.ANNOTATION, annot);
        } catch (FeaturesTreeNodeException e) {
            // This exception occurs when wrong node creates for feature tree.
            // The logic of the method guarantees this doesn't occur.
        }

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
