/**
 * 
 */
package org.verapdf.pdfa.validation;

import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlJavaTypeAdapter(ErrorDetailsImpl.Adapter.class)
public interface ErrorDetails {
    /**
     * @return the Error message as a String
     */
    public String getMessage();

    /**
     * @return a List of String arguments for the error
     */
    public List<String> getArguments();
}
