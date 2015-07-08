package org.verapdf.features.pb;

import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.verapdf.features.pb.objects.PBInfoDictFeaturesObject;
import org.verapdf.features.pb.objects.PBMetadataFeaturesObject;
import org.verapdf.features.pb.objects.PBOutputIntentsFeaturesObject;
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
     * Creates new PBOutputIntentsFeaturesObject
     * @param outInt - PDOutputIntent class from pdfbox, which represents an outputIntent for feature report
     * @return created PBOutputIntentsFeaturesObject
     */
    public static PBOutputIntentsFeaturesObject createOutputIntentFeaturesObject(PDOutputIntent outInt) {
        return new PBOutputIntentsFeaturesObject(outInt);
    }

    /**
     * Creates new PBPageFeatureObject
     * @param page - PDPage class from pdfbox, which represents a page for feature report
     * @return created PBPageFeaturesObject
     */
    public static PBPageFeaturesObject createPageFeaturesObject(PDPage page, int index) {
        return new PBPageFeaturesObject(page, index);
    }
}
