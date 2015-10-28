/**
 * 
 */
package org.verapdf.pdfa.qa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
            Collections.EMPTY_MAP);
    private final Registry<RuleId, Set<CorpusItemId>> ruleLookup = new MapBackedRegistry<>(
            Collections.EMPTY_MAP);
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
                        Collections.EMPTY_SET);
            }
            Set<CorpusItemId> itemIdSet = new HashSet(
                    this.ruleLookup.getItem(id.getRuleId()));
            itemIdSet.add(id);
            this.ruleLookup.putdateItem(id.getRuleId(), itemIdSet);
        }
    }

    /**
     * @param key
     * @return
     * @see org.verapdf.core.Directory#getItem(java.lang.Object)
     */
    @Override
    public Path getItem(CorpusItemId key) {
        return this.corpusItems.getItem(key);
    }

    /**
     * @return
     * @see org.verapdf.core.Directory#getItems()
     */
    @Override
    public Collection<Path> getItems() {
        return this.corpusItems.getItems();
    }

    /**
     * @return
     * @see org.verapdf.core.Directory#getKeys()
     */
    @Override
    public Set<CorpusItemId> getKeys() {
        return this.corpusItems.getKeys();
    }

    /**
     * @return
     * @see org.verapdf.core.Directory#size()
     */
    @Override
    public int size() {
        return this.corpusItems.size();
    }

    /**
     * @return
     * @see org.verapdf.core.Directory#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return this.corpusItems.isEmpty();
    }

    /**
     * @return
     */
    public PDFAFlavour getFlavour() {
        return this.flavour;
    }
    /**
     * @param id
     * @return
     */
    public Set<CorpusItemId> getCorpusIdForRule(final RuleId id) {
        if (this.ruleLookup.getItem(id) == null)
            return Collections.EMPTY_SET;
        return this.ruleLookup.getItem(id);
    }

    /**
     * @param root
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static CorpusDirectory loadFromDir(final File root)
            throws FileNotFoundException, IOException {
        return new CorpusDirectory(root);
    }

    /**
     * @param dir
     * @return
     */
    public static Set<Path> rulesFromDir(final File dir) {
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
