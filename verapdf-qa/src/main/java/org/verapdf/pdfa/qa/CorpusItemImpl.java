/**
 * 
 */
package org.verapdf.pdfa.qa;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public class CorpusItemImpl implements CorpusItem {

    private final String path;
    private final String sha1;

    private CorpusItemImpl(final String path) {
        this(path, "");
    }

    private CorpusItemImpl(final String path,
            final String sha1) {
        this.path = path;
        this.sha1 = sha1;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getPath() {
        return this.path;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getSha1() {
        return this.sha1;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.path == null) ? 0 : this.path.hashCode());
        result = prime * result + ((this.sha1 == null) ? 0 : this.sha1.hashCode());
        return result;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof CorpusItem))
            return false;
        CorpusItem other = (CorpusItem) obj;
        if (this.path == null) {
            if (other.getPath() != null)
                return false;
        } else if (!this.path.equals(other.getPath()))
            return false;
        if (this.sha1 == null) {
            if (other.getSha1() != null)
                return false;
        } else if (!this.sha1.equals(other.getSha1()))
            return false;
        return true;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "CorpusItem [path=" + this.path + ", sha1=" + this.sha1
                + "]";
    }

    /**
     * @param path
     * @return
     */
    public static CorpusItem fromValues(final String path) {
        return fromValues(path, "");
    }

    /**
     * @param path
     * @param sha1
     * @return
     */
    public static CorpusItem fromValues(final String path,
            final String sha1) {
        if (path == null)
            throw new NullPointerException("path can not be null.");
        if (path.isEmpty())
            throw new IllegalArgumentException("path can not be empty.");
        if (sha1 == null)
            throw new NullPointerException("sha1 can not be null.");
        return new CorpusItemImpl(path, sha1);
    }

    /**
     * @param corpusFile
     * @return
     */
    public static CorpusItem fromFile(final File corpusFile) {
        if (corpusFile == null)
            throw new NullPointerException("file can not be null.");
        if (!corpusFile.isFile())
            throw new IllegalArgumentException("file must be an existing file.");
        try (FileInputStream fis = new FileInputStream(corpusFile)) {
            return fromInputStream(fis, corpusFile.getPath());
        } catch (IOException e) {
            // Ignore stream close error
            return fromValues(corpusFile.getPath(), "");
        }
    }

    static CorpusItem fromInputStream(final InputStream corpusStream, final String path) {
        if (corpusStream == null) throw new NullPointerException("corpusStream can not be null.");
        if (path == null)
            throw new NullPointerException("path can not be null.");
        if (path.isEmpty())
            throw new IllegalArgumentException("path can not be empty.");
        try {
            return CorpusItemImpl.fromValues(path, DigestUtils.sha1Hex(corpusStream));
        } catch (IOException e) {
            return CorpusItemImpl.fromValues(path);
        }
    }
}
