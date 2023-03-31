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


import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Objects;

@XmlRootElement(name = "argument")
@XmlAccessorType(XmlAccessType.NONE)
final class ErrorArgumentImpl implements ErrorArgument {
    private final static ErrorArgumentImpl DEFAULT = new ErrorArgumentImpl();
    @XmlValue
    private final String argument;
    @XmlAttribute(name = "name")
    private final String name;
    private String argumentValue;

    private ErrorArgumentImpl() {
        this("argument", "");
    }

    private ErrorArgumentImpl(final String argument, final String name) {
        super();
        this.argument = argument;
        this.name = name;
    }

    @Override
    public String getArgument() {
        return this.argument;
    }

    @Override
    public String getName() {
        return this.name;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = ((this.argument == null) ? 0 : this.argument.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
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
        if (!(obj instanceof ErrorArgument)) {
            return false;
        }
        ErrorArgument other = (ErrorArgument) obj;
        if (!Objects.equals(this.argument, other.getArgument())) {
            return false;
        }
        return Objects.equals(this.name, other.getName());
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "ErrorDetails [argument=" + this.argument + ", name=" + this.name + "]";
    }

    static ErrorArgumentImpl defaultInstance() {
        return DEFAULT;
    }

    static ErrorArgumentImpl fromValues(final String argument, final String name) {
        return new ErrorArgumentImpl(argument, name);
    }

    @Override
    public String getArgumentValue() {
        return argumentValue;
    }

    public void setArgumentValue(String argumentValue) {
        this.argumentValue = argumentValue;
    }

    static class Adapter extends XmlAdapter<ErrorArgumentImpl, ErrorArgument> {
        @Override
        public ErrorArgumentImpl unmarshal(ErrorArgumentImpl errorArgumentImpl) {
            return errorArgumentImpl;
        }

        @Override
        public ErrorArgumentImpl marshal(ErrorArgument errorArgument) {
            return (ErrorArgumentImpl) errorArgument;
        }
    }
}
