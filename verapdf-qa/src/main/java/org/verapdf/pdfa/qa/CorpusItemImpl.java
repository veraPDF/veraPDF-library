/**
 * 
 */
package org.verapdf.pdfa.qa;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.verapdf.pdfa.flavours.PDFAFlavour.Specification;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public class CorpusItemImpl implements CorpusItem {

    private final CorpusItemId id;
    private final String path;
    private final String sha1;

    private CorpusItemImpl(final CorpusItemId id, final String path) {
        this(id, path, "");
    }

    private CorpusItemImpl(final CorpusItemId id, final String path,
            final String sha1) {
        this.id = id;
        this.path = path;
        this.sha1 = sha1;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public CorpusItemId getId() {
        return this.id;
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
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
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
        if (this.id == null) {
            if (other.getId() != null)
                return false;
        } else if (!this.id.equals(other.getId()))
            return false;
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
        return "CorpusItemImpl [id=" + this.id + ", path=" + this.path + ", sha1=" + this.sha1
                + "]";
    }

    public static CorpusItem fromValues(final CorpusItemId id, final String path) {
        return fromValues(id, path, "");
    }

    public static CorpusItem fromValues(final CorpusItemId id, final String path,
            final String sha1) {
        if (id == null)
            throw new NullPointerException("id can not be null.");
        if (path == null)
            throw new NullPointerException("path can not be null.");
        if (path.isEmpty())
            throw new IllegalArgumentException("path can not be empty.");
        if (sha1 == null)
            throw new NullPointerException("sha1 can not be null.");
        return new CorpusItemImpl(id, path, sha1);
    }

    public static CorpusItem fromFile(final File corpusFile,
            final Specification specification) {
        if (corpusFile == null)
            throw new NullPointerException("file can not be null.");
        if (!corpusFile.isFile())
            throw new IllegalArgumentException("file must be an existing file.");
        CorpusItemId id = CorpusItemIdImpl.fromFileName(specification,
                corpusFile.getName());
        try (FileInputStream fis = new FileInputStream(corpusFile)) {
            return fromInputStream(fis, id, corpusFile.getPath());
        } catch (IOException e) {
            // Ignore stream close error
            return fromValues(id, corpusFile.getPath(), "");
        }
    }

    static CorpusItem fromInputStream(final InputStream corpusStream, final CorpusItemId id, final String path) {
        if (corpusStream == null) throw new NullPointerException("corpusStream can not be null.");
        if (path == null)
            throw new NullPointerException("path can not be null.");
        if (path.isEmpty())
            throw new IllegalArgumentException("path can not be empty.");
        try {
            return CorpusItemImpl.fromValues(id, path, DigestUtils.sha1Hex(corpusStream));
        } catch (IOException e) {
            return CorpusItemImpl.fromValues(id, path);
        }
    }
}
