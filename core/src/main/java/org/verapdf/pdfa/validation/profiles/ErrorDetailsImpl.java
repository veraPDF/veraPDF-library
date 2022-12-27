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
package org.verapdf.pdfa.validation.profiles;

import java.util.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlRootElement(name = "error")
final class ErrorDetailsImpl implements ErrorDetails {
    private final static ErrorDetailsImpl DEFAULT = new ErrorDetailsImpl();
    @XmlElement
    private final String message;
    @XmlElementWrapper
    @XmlElement(name = "argument")
    private final List<ErrorArgument> arguments;

    private ErrorDetailsImpl() {
        this("message", Collections.<ErrorArgument> emptyList());
    }

    private ErrorDetailsImpl(final String message, final List<ErrorArgument> arguments) {
        super();
        this.message = message;
        this.arguments = new ArrayList<>(arguments);
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
    public List<ErrorArgument> getArguments() {
        return Collections.unmodifiableList(this.arguments);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = ((this.arguments == null) ? 0 : this.arguments.hashCode());
        result = prime * result + ((this.message == null) ? 0 : this.message.hashCode());
        return result;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ErrorDetails)) {
            return false;
        }
        ErrorDetails other = (ErrorDetails) obj;
        if (!Objects.equals(this.arguments, other.getArguments())) {
            return false;
        }
        return Objects.equals(this.message, other.getMessage());

    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "ErrorDetails [message=" + this.message + ", arguments="
                + this.arguments + "]";
    }

    static ErrorDetailsImpl defaultInstance() {
        return DEFAULT;
    }

    static ErrorDetailsImpl fromValues(final String message,
            final List<ErrorArgument> arguments) {
        return new ErrorDetailsImpl(message, arguments);
    }

    static class Adapter extends XmlAdapter<ErrorDetailsImpl, ErrorDetails> {
        @Override
        public ErrorDetailsImpl unmarshal(ErrorDetailsImpl errorDetailsImpl) {
            return new ErrorDetailsImpl(RuleImpl.getStringWithoutProfilesTabulation(errorDetailsImpl.getMessage()),
                    errorDetailsImpl.getArguments());
        }

        @Override
        public ErrorDetailsImpl marshal(ErrorDetails errorDetails) {
            return (ErrorDetailsImpl) errorDetails;
        }
    }
}
