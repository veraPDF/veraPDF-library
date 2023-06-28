/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
/**
 * 
 */
package org.verapdf.pdfa.results;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.verapdf.pdfa.validation.profiles.ErrorArgument;
import org.verapdf.pdfa.validation.profiles.Profiles;
import org.verapdf.pdfa.validation.profiles.RuleId;

import java.util.List;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlRootElement(name="testAssertion")
final class TestAssertionImpl implements TestAssertion {
    private static final TestAssertionImpl DEFAULT = new TestAssertionImpl();
    @XmlAttribute
    private final int ordinal;
    @XmlElement
    private final RuleId ruleId;
    @XmlAttribute
    private final Status status;
    @XmlElement
    private final String message;
    @XmlElement
    private final Location location;
    @XmlElement
    private final String locationContext;
    @XmlElement
    private final String errorMessage;

    private final List<ErrorArgument> errorArguments;

    private TestAssertionImpl() {
        this(0, Profiles.defaultRuleId(), Status.FAILED, "message", LocationImpl
                .defaultInstance(), null, null, null);
    }

    private TestAssertionImpl(final int ordinal, final RuleId ruleId, final Status status, final String message,
                              final Location location, final String locationContext, final String errorMessage,
                              final List<ErrorArgument> errorArguments) {
        super();
        this.ordinal = ordinal;
        this.ruleId = ruleId;
        this.status = status;
        this.message = message;
        this.location = location;
        this.locationContext = locationContext;
        this.errorMessage = errorMessage;
        this.errorArguments = errorArguments;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public int getOrdinal() {
        return this.ordinal;
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
    public String getLocationContext() {
        return this.locationContext;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public List<ErrorArgument> getErrorArguments() {
        return errorArguments;
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
        result = prime * result + ((this.locationContext == null) ? 0 : this.locationContext.hashCode());
        result = prime * result + ((this.errorMessage == null) ? 0 : this.errorMessage.hashCode());
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
        if (this.locationContext == null) {
            if (other.getLocationContext() != null)
                return false;
        } else if (!this.locationContext.equals(other.getLocationContext()))
            return false;
        if (this.errorMessage == null) {
            if (other.getErrorMessage() != null)
                return false;
        } else if (!this.errorMessage.equals(other.getErrorMessage()))
            return false;
        return true;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "TestAssertion [ruleId=" + this.ruleId + ", status=" + this.status + ", message=" + this.message +
                ", location=" + this.location + ", locationContext=" + this.locationContext + ", errorMessage=" +
                this.errorMessage + "]";
    }
    
    static TestAssertionImpl defaultInstance() {
        return DEFAULT;
    }

    static TestAssertionImpl fromValues(final int ordinal, final RuleId ruleId, final Status status,
            final String message, final Location location, final String locationContext, final String errorMessage,
            final List<ErrorArgument> errorArguments) {
        return new TestAssertionImpl(ordinal, ruleId, status, message, location, locationContext, errorMessage, errorArguments);
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
