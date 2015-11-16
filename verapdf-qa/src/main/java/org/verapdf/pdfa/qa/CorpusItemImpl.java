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

    static CorpusItemImpl fromValues(final CorpusItemId id, final String path) {
        return fromValues(id, path, "");
    }

    static CorpusItemImpl fromValues(final CorpusItemId id, final String path,
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

    static CorpusItemImpl fromFile(final File corpusFile,
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

    static CorpusItemImpl fromInputStream(final InputStream corpusStream, final CorpusItemId id, final String path) {
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
