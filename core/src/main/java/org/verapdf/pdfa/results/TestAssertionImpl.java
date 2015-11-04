/**
 * 
 */
package org.verapdf.pdfa.results;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.verapdf.pdfa.validation.Profiles;
import org.verapdf.pdfa.validation.RuleId;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlRootElement(name="testAssertion")
final class TestAssertionImpl implements TestAssertion {
    private static final TestAssertionImpl DEFAULT = new TestAssertionImpl();
    @XmlElement
    private final RuleId ruleId;
    @XmlAttribute
    private final Status status;
    @XmlElement
    private final String message;
    @XmlElement
    private final Location location;

    private TestAssertionImpl() {
        this(Profiles.defaultRuleId(), Status.FAILED, "message", LocationImpl
                .defaultInstance());
    }

    private TestAssertionImpl(final RuleId ruleId, final Status status,
            final String message, final Location location) {
        super();
        this.ruleId = ruleId;
        this.status = status;
        this.message = message;
        this.location = location;
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
    public Status getStatus() {
        return this.status;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getMessage() {
        return this.message;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Location getLocation() {
        return this.location;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.location == null) ? 0 : this.location.hashCode());
        result = prime * result + ((this.message == null) ? 0 : this.message.hashCode());
        result = prime * result + ((this.ruleId == null) ? 0 : this.ruleId.hashCode());
        result = prime * result + ((this.status == null) ? 0 : this.status.hashCode());
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
        if (!(obj instanceof TestAssertion))
            return false;
        TestAssertion other = (TestAssertion) obj;
        if (this.location == null) {
            if (other.getLocation() != null)
                return false;
        } else if (!this.location.equals(other.getLocation()))
            return false;
        if (this.message == null) {
            if (other.getMessage() != null)
                return false;
        } else if (!this.message.equals(other.getMessage()))
            return false;
        if (this.ruleId == null) {
            if (other.getRuleId() != null)
                return false;
        } else if (!this.ruleId.equals(other.getRuleId()))
            return false;
        if (this.status != other.getStatus())
            return false;
        return true;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "TestAssertion [ruleId=" + this.ruleId + ", status=" + this.status
                + ", message=" + this.message + ", location=" + this.location + "]";
    }
    
    static TestAssertionImpl defaultInstance() {
        return DEFAULT;
    }

    static TestAssertionImpl fromValues(final RuleId ruleId, final Status status,
            final String message, final Location location) {
        return new TestAssertionImpl(ruleId, status, message, location);
    }

    static class Adapter extends XmlAdapter<TestAssertionImpl, TestAssertion> {
        @Override
        public TestAssertion unmarshal(TestAssertionImpl testAssertionImpl) {
            return testAssertionImpl;
        }

        @Override
        public TestAssertionImpl marshal(TestAssertion testAssertion) {
            return (TestAssertionImpl) testAssertion;
        }
    }
}
