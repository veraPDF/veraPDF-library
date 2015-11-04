/**
 * 
 */
package org.verapdf.pdfa.qa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.verapdf.core.Directory;
import org.verapdf.core.MapBackedRegistry;
import org.verapdf.core.ProfileException;
import org.verapdf.core.Registry;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.Rule;
import org.verapdf.pdfa.validation.RuleId;
import org.verapdf.pdfa.validation.ValidationProfile;
import org.verapdf.validation.profile.parser.LegacyProfileConverter;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public final class RuleDirectory implements Directory<RuleId, Rule> {

    private final Registry<RuleId, Rule> rules = new MapBackedRegistry<>(
            Collections.EMPTY_MAP);
    private final PDFAFlavour flavour;

    private RuleDirectory(final File root) {
        this.flavour = PDFAFlavour.fromString(root.getName());
        Set<Rule> ruleSet = rulesFromDir(root, this.flavour);
        for (Rule rule : ruleSet) {
            this.rules.putdateItem(rule.getRuleId(), rule);
        }
    }

    /**
     * @param key
     * @return
     * @see org.verapdf.core.Directory#getItem(java.lang.Object)
     */
    @Override
    public Rule getItem(RuleId key) {
        return this.rules.getItem(key);
    }

    /**
     * @return
     * @see org.verapdf.core.Directory#getItems()
     */
    @Override
    public Collection<Rule> getItems() {
        return this.rules.getItems();
    }

    /**
     * @return
     * @see org.verapdf.core.Directory#getKeys()
     */
    @Override
    public Set<RuleId> getKeys() {
        return this.rules.getKeys();
    }

    /**
     * @return
     * @see org.verapdf.core.Directory#size()
     */
    @Override
    public int size() {
        return this.rules.size();
    }

    /**
     * @return
     * @see org.verapdf.core.Directory#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return this.rules.isEmpty();
    }

    /**
     * @return
     */
    public PDFAFlavour getFlavour() {
        return this.flavour;
    }

    public static RuleDirectory loadFromDir(final File root)
            throws FileNotFoundException, IOException {
        return new RuleDirectory(root);
    }

    /**
     * @param dir
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ProfileException
     */
    public static Set<Rule> rulesFromDir(final File dir,
            final PDFAFlavour flavour) {
        Set<Rule> rules = new HashSet<>();
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isHidden())
                continue;
            if (file.isDirectory())
                rules.addAll(rulesFromDir(file, flavour));
            if (file.isFile() && file.canRead()) {
                try (InputStream fis = new FileInputStream(file)) {
                    Rule rule = getRuleFromLegacyProfile(fis, flavour);
                    rules.add(rule);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ProfileException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return rules;
    }

    private static Rule getRuleFromLegacyProfile(final InputStream toParse,
            final PDFAFlavour flavour) throws ProfileException {
        ValidationProfile profile = LegacyProfileConverter.fromLegacyStream(
                toParse, flavour);
        return profile.getRules().iterator().next();
    }
}
