package org.verapdf.features.pb;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.encryption.PDEncryption;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.verapdf.features.pb.objects.*;

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
     * @return created PBOutputIntentsFeaturesObject
     */
    public static PBOutputIntentsFeaturesObject createOutputIntentFeaturesObject(PDOutputIntent outInt) {
        return new PBOutputIntentsFeaturesObject(outInt);
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
     * Creates new PBPageFeaturesObject
     * @param page - PDPage class from pdfbox, which represents a page for feature report
     * @return created PBPageFeaturesObject
     */
    public static PBPageFeaturesObject createPageFeaturesObject(PDPage page, int index) {
        return new PBPageFeaturesObject(page, index);
    }
}
