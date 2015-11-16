/**
 * 
 */
package org.verapdf.pdfa.qa;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.verapdf.core.Directory;
import org.verapdf.core.MapBackedRegistry;
import org.verapdf.core.Registry;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.RuleId;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public class CorpusDirectory implements Directory<CorpusItemId, Path> {
    private final Registry<CorpusItemId, Path> corpusItems = new MapBackedRegistry<>(
            Collections.<CorpusItemId, Path> emptyMap());
    private final Registry<RuleId, Set<CorpusItemId>> ruleLookup = new MapBackedRegistry<>(
            Collections.<RuleId, Set<CorpusItemId>> emptyMap());
    private final PDFAFlavour flavour;

    private CorpusDirectory(final File root) {
        this.flavour = PDFAFlavour.fromString(root.getName());
        Set<Path> pathSet = rulesFromDir(root);
        for (Path path : pathSet) {
            CorpusItemId id = CorpusItemIdImpl.fromFileName(
                    this.flavour.getPart(), path.getFileName().toString());
            this.corpusItems.putdateItem(id, path);
            if (this.ruleLookup.getItem(id.getRuleId()) == null) {
                this.ruleLookup.registerItem(id.getRuleId(),
                        Collections.<CorpusItemId> emptySet());
            }
            Set<CorpusItemId> itemIdSet = new HashSet<>(
                    this.ruleLookup.getItem(id.getRuleId()));
            itemIdSet.add(id);
            this.ruleLookup.putdateItem(id.getRuleId(), itemIdSet);
        }
    }

    /**
     * @see org.verapdf.core.Directory#getItem(java.lang.Object)
     */
    @Override
    public Path getItem(CorpusItemId key) {
        return this.corpusItems.getItem(key);
    }

    /**
     * @see org.verapdf.core.Directory#getItems()
     */
    @Override
    public Collection<Path> getItems() {
        return this.corpusItems.getItems();
    }

    /**
     * @see org.verapdf.core.Directory#getKeys()
     */
    @Override
    public Set<CorpusItemId> getKeys() {
        return this.corpusItems.getKeys();
    }

    /**
     * @see org.verapdf.core.Directory#size()
     */
    @Override
    public int size() {
        return this.corpusItems.size();
    }

    /**
     * @see org.verapdf.core.Directory#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return this.corpusItems.isEmpty();
    }

    /**
     * @return the {@link PDFAFlavour} associated with the corpus
     */
    public PDFAFlavour getFlavour() {
        return this.flavour;
    }

    /**
     * Returns the {@code Set} of {@link CorpusItemId}s associated with a
     * particular {@link RuleId}.
     * 
     * @param id
     *            the {@code RuleId} to match
     * @return the {@code Set} of {@link CorpusItemId}s.
     */
    public Set<CorpusItemId> getCorpusIdForRule(final RuleId id) {
        if (this.ruleLookup.getItem(id) == null)
            return Collections.<CorpusItemId> emptySet();
        return this.ruleLookup.getItem(id);
    }

    /**
     * @param root
     *            the {@code File} root directory for the Corpus
     * @return a {@code CorpusDirectory} with {@code root} directory
     * @throws NullPointerException
     *             if {@code root} is null
     * @throws IllegalArgumentException
     *             if {@code root} is not an existing directory
     */
    public static CorpusDirectory loadFromDir(final File root) {
        if (root == null)
            throw new NullPointerException("Parameter root should not be null.");
        if (!root.isDirectory())
            throw new IllegalArgumentException(
                    "Parameter root MUST be an existing directory.");
        return new CorpusDirectory(root);
    }

    private static Set<Path> rulesFromDir(final File dir) {
        Set<Path> paths = new HashSet<>();
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isHidden())
                continue;
            if (file.isDirectory())
                paths.addAll(rulesFromDir(file));
            if (file.isFile() && file.canRead()) {
                paths.add(file.toPath());

            }
        }
        return paths;
    }
}
