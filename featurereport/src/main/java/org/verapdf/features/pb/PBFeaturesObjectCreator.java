package org.verapdf.features.pb;

import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.verapdf.features.pb.objects.PBInfoDictFeaturesObject;
import org.verapdf.features.pb.objects.PBMetadataFeaturesObject;
import org.verapdf.features.pb.objects.PBPageFeaturesObject;

/**
 * Creates Feature Objects and report them to Features Reporter
 *
 * @author Maksim Bezrukov
 */
public final class PBFeaturesObjectCreator {

    private PBFeaturesObjectCreator() {
    }

    /**
     * Creates new PBPageFeatureObject
     * @param page - PDPage class from pdfbox, which represents a page for feature report
     */
    public static PBPageFeaturesObject createPageFeaturesObject(PDPage page, int index) {
        return new PBPageFeaturesObject(page, index);
    }

    /**
     * Creates new PBInfoDictFeaturesObject
     * @param info - PDDocumentInformation class from pdfbox, which represents a document info dictionary for feature report
     */
    public static PBInfoDictFeaturesObject createInfoDictFeaturesObject(PDDocumentInformation info) {
        return new PBInfoDictFeaturesObject(info);
    }

    /**
     * Creates new PBMetadataFeaturesObject
     * @param metadata - PDMetadata class from pdfbox, which represents a metadata for feature report
     */
    public static PBMetadataFeaturesObject createMetadataFeaturesObject(PDMetadata metadata) {
        return new PBMetadataFeaturesObject(metadata);
    }
}
