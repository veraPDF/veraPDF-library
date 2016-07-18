package org.verapdf.features;

import java.io.InputStream;

/**
 * @author Maksim Bezrukov
 */
public class MetadataFeaturesData extends FeaturesData {

    /**
     * Constructs new FeaturesData
     *
     * @param stream object stream
     */
    private MetadataFeaturesData(InputStream stream) {
        super(stream);
    }

    /**
     * Creates MetadataFeaturesData
     *
     * @param stream object stream
     */
    public static MetadataFeaturesData newInstance(InputStream stream) {
        if (stream == null) {
            throw new IllegalArgumentException("Metadata stream can not be null");
        }
        return new MetadataFeaturesData(stream);
    }
}
