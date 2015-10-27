/**
 * 
 */
package org.verapdf.pdfa.validation;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlJavaTypeAdapter(VariableImpl.Adapter.class)
public interface Variable {
    /**
     * @return the name of the Variable
     */
    public String getName();
    /**
     * @return the String name of the Object??
     */
    public String getObject();
    /**
     * @return the default value for the Variable
     */
    public String getDefaultValue();
    /**
     * @return the Variable's value as a String
     */
    public String getValue();
}
