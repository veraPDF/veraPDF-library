/**
 * 
 */
package org.verapdf.pdfa.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private final List<String> arguments;

    private ErrorDetailsImpl() {
        this("message", Collections.EMPTY_LIST);
    }

    private ErrorDetailsImpl(final String message, final List<String> arguments) {
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
    public List<String> getArguments() {
        return Collections.unmodifiableList(this.arguments);
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.arguments == null) ? 0 : this.arguments.hashCode());
        result = prime * result
                + ((this.message == null) ? 0 : this.message.hashCode());
        return result;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ErrorDetails))
            return false;
        ErrorDetails other = (ErrorDetails) obj;
        if (this.arguments == null) {
            if (other.getArguments() != null)
                return false;
        } else if (!this.arguments.equals(other.getArguments()))
            return false;
        if (this.message == null) {
            if (other.getMessage() != null)
                return false;
        } else if (!this.message.equals(other.getMessage()))
            return false;
        return true;
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
            final List<String> arguments) {
        return new ErrorDetailsImpl(message, arguments);
    }

    static class Adapter extends XmlAdapter<ErrorDetailsImpl, ErrorDetails> {
        @Override
        public ErrorDetailsImpl unmarshal(ErrorDetailsImpl efforDetailsImpl) {
            return efforDetailsImpl;
        }

        @Override
        public ErrorDetailsImpl marshal(ErrorDetails errorDetails) {
            return (ErrorDetailsImpl) errorDetails;
        }
    }
}
