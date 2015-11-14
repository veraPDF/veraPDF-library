/**
 * 
 */
package org.verapdf.pdfa.qa;

import org.verapdf.pdfa.flavours.PDFAFlavour.Specification;
import org.verapdf.pdfa.results.TestAssertion.Status;
import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.RuleId;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public class CorpusItemIdImpl implements CorpusItemId {
    private static final CorpusItemIdImpl DEFAULT = new CorpusItemIdImpl();
    private static final String SEPARATOR = "-";
    private static final String TEST_PREFIX = "t";
    private static final String TEST_FILE_EXT = ".pdf";
    private final RuleId ruleId;
    private final String testCode;
    private final boolean result;

    private CorpusItemIdImpl() {
        this(Profiles.defaultRuleId(), "testCode", false);
    }

    /**
     * @param ruleId
     * @param testCode
     * @param status
     */
    private CorpusItemIdImpl(final RuleId ruleId, final String testCode,
            final boolean result) {
        super();
        this.ruleId = ruleId;
        this.testCode = testCode;
        this.result = result;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public RuleId getRuleId() {
        return this.ruleId;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getTestCode() {
        return this.testCode;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public boolean getExpectedResult() {
        return this.result;
    }

    

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.result ? 1231 : 1237);
        result = prime * result
                + ((this.ruleId == null) ? 0 : this.ruleId.hashCode());
        result = prime * result
                + ((this.testCode == null) ? 0 : this.testCode.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof CorpusItemIdImpl))
            return false;
        CorpusItemIdImpl other = (CorpusItemIdImpl) obj;
        if (this.result != other.result)
            return false;
        if (this.ruleId == null) {
            if (other.ruleId != null)
                return false;
        } else if (!this.ruleId.equals(other.ruleId))
            return false;
        if (this.testCode == null) {
            if (other.testCode != null)
                return false;
        } else if (!this.testCode.equals(other.testCode))
            return false;
        return true;
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CorpusItemIdImpl [ruleId=" + this.ruleId + ", testCode="
                + this.testCode + ", result=" + this.result + "]";
    }

    /**
     * @return
     */
    public static CorpusItemId defaultInstance() {
        return DEFAULT;
    }

    /**
     * @param ruleId
     * @param testCode
     * @param status
     * @return
     */
    public static CorpusItemId fromValues(final RuleId ruleId,
            final String testCode, final boolean result) {
        return new CorpusItemIdImpl(ruleId, testCode, result);
    }

    /**
     * @param specification 
     * @param name
     * @return
     */
    public static CorpusItemId fromFileName(final Specification specification, final String name) {
        for (String part : name.split(" ")) {
            if (part.endsWith(TEST_FILE_EXT)) {
                return fromCode(specification, part);
            }
        }
        return DEFAULT;
    }

    /**
     * @param specification
     * @param code
     * @return
     */
    public static CorpusItemId fromCode(final Specification specification, final String code) {
        StringBuilder builder = new StringBuilder();
        String separator = "";
        boolean status = false;
        String testCode = "";
        int testNumber = 0;
        for (String part : code.split(SEPARATOR)) {
            if (part.endsWith(TEST_FILE_EXT)){
                testCode = part.substring(0, 1);
                break;
            } else if (testPassFail(part)) {
                status = testPassFail(part);
            } else if (part.startsWith(TEST_PREFIX)) {
                testNumber = Integer.parseInt(part.substring(1));
            } else {
                builder.append(separator);
                builder.append(part);
                separator = ".";
            } 
        }
        RuleId ruleId = Profiles.ruleIdFromValues(specification, builder.toString(), testNumber);
        return CorpusItemIdImpl.fromValues(ruleId, testCode, status);
    }
    
    private static boolean testPassFail(final String code) {
        return code.equals("pass");
    }
}