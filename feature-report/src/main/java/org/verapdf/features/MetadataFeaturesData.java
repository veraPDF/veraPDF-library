package org.verapdf.features;

/**
 * @author Maksim Bezrukov
 */
public class MetadataFeaturesData extends FeaturesData {

    /**
     * Constructs new FeaturesData
     *
     * @param stream byte array represents object stream
     */
    private MetadataFeaturesData(byte[] stream) {
        super(stream);
    }

    /**
     * Creates MetadataFeaturesData
     *
     * @param stream byte array represents object stream
     */
    public static MetadataFeaturesData newInstance(byte[] stream) {
        if (stream == null) {
            throw new IllegalArgumentException("Metadata stream can not be null");
        }
        return new MetadataFeaturesData(stream);
    }
}
