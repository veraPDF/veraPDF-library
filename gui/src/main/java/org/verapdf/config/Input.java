package org.verapdf.config;

/**
 * This class holds all params defining the input path.
 * @author Timur Kamalov
 */
public class Input {

    private final String path;
    private final boolean url;

    public Input(String path, boolean url) {
        this.path = path;
        this.url = url;
    }

    /**
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * @return url
     */
    public boolean isUrl() {
        return url;
    }
}
