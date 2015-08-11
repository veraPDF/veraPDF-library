package org.verapdf.features.pb;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.encryption.PDEncryption;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.verapdf.features.pb.objects.*;

import java.io.InputStream;
import java.util.Set;

/**
 * Creates Feature Objects and report them to Features Reporter
 *
 * @author Maksim Bezrukov
 */
public final class PBFeaturesObjectCreator {

    private PBFeaturesObjectCreator() {
    }

    /**
     * Creates new PBInfoDictFeaturesObject
     * @param info - PDDocumentInformation class from pdfbox, which represents a document info dictionary for feature report
     * @return created PBInfoDictFeaturesObject
     */
    public static PBInfoDictFeaturesObject createInfoDictFeaturesObject(PDDocumentInformation info) {
        return new PBInfoDictFeaturesObject(info);
    }

    /**
     * Creates new PBMetadataFeaturesObject
     * @param metadata - PDMetadata class from pdfbox, which represents a metadata for feature report
     * @return created PBMetadataFeaturesObject
     */
    public static PBMetadataFeaturesObject createMetadataFeaturesObject(PDMetadata metadata) {
        return new PBMetadataFeaturesObject(metadata);
    }

    /**
     * Creates new PBDocSecurityFeaturesObject
     *
     * @param encryption - PDEncryption class from pdfbox, which represents an encryption for feature report
     * @return created PBDocSecurityFeaturesObject
     */
    public static PBDocSecurityFeaturesObject createDocSecurityFeaturesObject(PDEncryption encryption) {
        return new PBDocSecurityFeaturesObject(encryption);
    }

    /**
     * Creates new PBLowLvlInfoFeaturesObject
     * @param document - COSDocument class from pdfbox, which represents a document for feature report
     * @return created PBLowLvlInfoFeaturesObject
     */
    public static PBLowLvlInfoFeaturesObject createLowLvlInfoFeaturesObject(COSDocument document) {
        return new PBLowLvlInfoFeaturesObject(document);
    }

    /**
     * Creates new PBEmbeddedFileFeaturesObject
     *
     * @param embFile - PDComplexFileSpecification class from pdfbox, which represents a file specification with embedded
     *                file for feature report
     * @return created PBEmbeddedFileFeaturesObject
     */
    public static PBEmbeddedFileFeaturesObject createEmbeddedFileFeaturesObject(PDComplexFileSpecification embFile, int index) {
        return new PBEmbeddedFileFeaturesObject(embFile, index);
    }

    /**
     * Creates new PBOutputIntentsFeaturesObject
     * @param outInt - PDOutputIntent class from pdfbox, which represents an outputIntent for feature report
     * @param id - id of the outputIntent
     * @param iccProfileID - id of the icc profile which use in this outputIntent
     * @return created PBOutputIntentsFeaturesObject
     */
    public static PBOutputIntentsFeaturesObject createOutputIntentFeaturesObject(PDOutputIntent outInt, String id, String iccProfileID) {
        return new PBOutputIntentsFeaturesObject(outInt, id, iccProfileID);
    }

    /**
     * Creates new PBOutlinesFeaturesObject
     * @param outlines - PDPage class from pdfbox, which represents a page for feature report
     * @return created PBOutlinesFeaturesObject
     */
    public static PBOutlinesFeaturesObject createOutlinesFeaturesObject(PDDocumentOutline outlines) {
        return new PBOutlinesFeaturesObject(outlines);
    }

    /**
     * Creates new PBAnnotationFeaturesObject
     * @param annot - PDAnnotation class from pdfbox, which represents an annotation for feature report
     * @param id - page id
     * @param pages - set of pages for this annotation
     * @param parentId - parent annotation for this annotation
     * @param popupId - id of the popup annotation for this annotation
     * @param appearanceId - id of the appearance stream for this annotation
     * @return created PBAnnotationFeaturesObject
     */
    public static PBAnnotationFeaturesObject createAnnotFeaturesObject(PDAnnotation annot, String id, Set<String> pages,
                                                                       String parentId, String popupId, String appearanceId) {
        return new PBAnnotationFeaturesObject(annot, id, pages, parentId, popupId, appearanceId);
    }

    /**
     * Creates new PBPageFeaturesObject
     * @param page - PDPage class from pdfbox, which represents a page for feature report
     * @param annotsId - set of annotations id which contains in this page
     * @param id - page id
     * @param index - page index
     * @return created PBPageFeaturesObject
     */
    public static PBPageFeaturesObject createPageFeaturesObject(PDPage page, Set<String> annotsId, String id, int index) {
        return new PBPageFeaturesObject(page, annotsId, id, index);
    }

    /**
     * Creates new PBICCProfileFeaturesObject
     *
     * @param profile   input stream which represents the icc profile for feature report
     * @param id        - id of the profile
     * @param outInts   - set of ids of all parent output intents for this icc profile
     * @param iccBaseds - set of ids of all parent icc based color spaces for this icc profile
     * @return created PBICCProfileFeaturesObject
     */
    public static PBICCProfileFeaturesObject createICCProfileFeaturesObject(InputStream profile, String id,
                                                                            Set<String> outInts, Set<String> iccBaseds) {
        return new PBICCProfileFeaturesObject(profile, id, outInts, iccBaseds);
    }
}
