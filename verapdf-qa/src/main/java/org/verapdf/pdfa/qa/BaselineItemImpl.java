/**
 * 
 */
package org.verapdf.pdfa.qa;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public final class BaselineItemImpl implements BaselineItem {
    private static final String XML_PROLOG = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
    private static final String RESULT_CLOSE_XML = "</validationResult>";
    private static final String PATH_LINE_PREFIX = "Testing: ";

    private static final String[] TEST_PATHS = {
            "/home/cfw/GitHub/veraPDF/veraPDF-library/d0.7.10.txt",
            "/home/cfw/GitHub/veraPDF/veraPDF-library/d0.7.15.txt",
            "/home/cfw/GitHub/veraPDF/veraPDF-library/d0.7.20.txt",
            "/home/cfw/GitHub/veraPDF/veraPDF-library/d0.7.25.txt",
            "/home/cfw/GitHub/veraPDF/veraPDF-library/d0.7.30.txt" };

    @XmlElement
    private final CorpusItem item;
    @XmlElementWrapper
    @XmlElement(name = "assertion")
    private final Set<TestAssertion> expectedFailures;

    private BaselineItemImpl(final CorpusItem item,
            final Set<TestAssertion> expectedFailures) {
        this.item = item;
        this.expectedFailures = expectedFailures;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public CorpusItem getCorpusItem() {
        return this.item;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Set<TestAssertion> getExpectedFailures() {
        return Collections.unmodifiableSet(this.expectedFailures);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public int getExpectedFailedCount() {
        return this.expectedFailures.size();
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((this.expectedFailures == null) ? 0 : this.expectedFailures
                        .hashCode());
        result = prime * result
                + ((this.item == null) ? 0 : this.item.hashCode());
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
        if (!(obj instanceof BaselineItem))
            return false;
        BaselineItem other = (BaselineItem) obj;
        if (this.expectedFailures == null) {
            if (other.getExpectedFailures() != null)
                return false;
        } else if (!this.expectedFailures.equals(other.getExpectedFailures()))
            return false;
        if (this.item == null) {
            if (other.getCorpusItem() != null)
                return false;
        } else if (!this.item.equals(other.getCorpusItem()))
            return false;
        return true;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "BaselineItemImpl [item=" + this.item + ", expectedFailures="
                + this.expectedFailures + "]";
    }

    public static BaselineItem fromValues(final CorpusItem item,
            final Set<TestAssertion> expectedFailures) {
        return new BaselineItemImpl(item, expectedFailures);
    }

    public static void main(final String[] args) throws IOException,
            JAXBException {
        List<Set<BaselineItem>> itemSets = new ArrayList<>();
        for (String testPath : TEST_PATHS) {
            try (FileInputStream fis = new FileInputStream(
                    testPath)) {
                itemSets.add(fromTestOutput(fis));
            }
        }
        for (int index = 0; index < itemSets.size(); index++) {
            System.out.println();
            System.out.println(TEST_PATHS[index]);
            for (BaselineItem item : itemSets.get(index)) {
                System.out.println(item);
            }
        }
    }

    static Set<BaselineItem> fromTestOutput(final InputStream testOutput)
            throws IOException, JAXBException {
        Set<BaselineItem> items = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                testOutput))) {
            String line = null;
            String corpusPath = "";
            CorpusItemId id = CorpusItemIdImpl.defaultInstance();
            StringBuilder resultXml = new StringBuilder();
            boolean inResult = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(PATH_LINE_PREFIX)) {
                    corpusPath = pathFromOutputLine(line);
                    String fileName = nameFromPath(corpusPath);
                    id = CorpusItemIdImpl.fromFileName(
                            PDFAFlavour.Specification.NO_STANDARD, fileName);
                } else if (line.startsWith(XML_PROLOG)) {
                    inResult = true;
                } else if (line.startsWith(RESULT_CLOSE_XML)) {
                    resultXml.append(line.trim());
                    BaselineItem item = fromResultData(resultXml.toString(),
                            id, corpusPath);
                    items.add(item);
                    resultXml = new StringBuilder();
                    inResult = false;
                }
                if (inResult) {
                    resultXml.append(line.trim());
                }
            }
        }
        return items;
    }

    private static String pathFromOutputLine(final String line) {
        return line;
    }

    private static String nameFromPath(final String path) {
        String[] pathParts = path.split("/");
        return pathParts[pathParts.length - 1];
    }

    private static BaselineItem fromResultData(final String resultXml,
            final CorpusItemId id, final String corpusPath)
            throws JAXBException {
        ValidationResult result = ValidationResults.fromXml(resultXml);
        return BaselineItemImpl.fromValues(
                CorpusItemImpl.fromValues(id, corpusPath),
                result.getTestAssertions());
    }
}
