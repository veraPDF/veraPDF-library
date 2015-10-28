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
    private final Status status;

    private CorpusItemIdImpl() {
        this(Profiles.defaultRuleId(), "testCode", Status.UNKNOWN);
    }

    /**
     * @param ruleId
     * @param testCode
     * @param status
     */
    private CorpusItemIdImpl(final RuleId ruleId, final String testCode,
            final Status status) {
        super();
        this.ruleId = ruleId;
        this.testCode = testCode;
        this.status = status;
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
    public Status getExpectedStatus() {
        return this.status;
    }

    
    /**
     * { @inheritDoc }
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.ruleId == null) ? 0 : this.ruleId.hashCode());
        result = prime * result + ((this.status == null) ? 0 : this.status.hashCode());
        result = prime * result
                + ((this.testCode == null) ? 0 : this.testCode.hashCode());
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
        if (getClass() != obj.getClass())
            return false;
        CorpusItemIdImpl other = (CorpusItemIdImpl) obj;
        if (this.ruleId == null) {
            if (other.ruleId != null)
                return false;
        } else if (!this.ruleId.equals(other.ruleId))
            return false;
        if (this.status != other.status)
            return false;
        if (this.testCode == null) {
            if (other.testCode != null)
                return false;
        } else if (!this.testCode.equals(other.testCode))
            return false;
        return true;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "CorpusItemIdImpl [ruleId=" + this.ruleId + ", testCode=" + this.testCode
                + ", status=" + this.status + "]";
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
            final String testCode, final Status status) {
        return new CorpusItemIdImpl(ruleId, testCode, status);
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
        Status status = Status.UNKNOWN;
        String testCode = "";
        int testNumber = 0;
        for (String part : code.split(SEPARATOR)) {
            if (part.endsWith(TEST_FILE_EXT)){
                testCode = part.substring(0, 1);
                break;
            } else if (testPassFail(part) != Status.UNKNOWN) {
                status = testPassFail(part);
            } else if (part.startsWith(TEST_PREFIX)) {
                testNumber = Integer.parseInt(part.substring(1));
            } else if (status == Status.UNKNOWN){
                builder.append(separator);
                builder.append(part);
                separator = ".";
            } 
        }
        RuleId ruleId = Profiles.ruleIdFromValues(specification, builder.toString(), testNumber);
        return CorpusItemIdImpl.fromValues(ruleId, testCode, status);
    }
    
    private static Status testPassFail(final String code) {
        if (code.equals("fail")) return Status.FAILED;
        if (code.equals("pass")) return Status.PASSED;
        return Status.UNKNOWN;
    }
}