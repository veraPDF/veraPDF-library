/**
 * 
 */
package org.verapdf.pdfa.validation.profiles;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * Convenience gathering of a set of properties that help identify and describe
 * a {@link ValidationProfile}.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlJavaTypeAdapter(ProfileDetailsImpl.Adapter.class)
public interface ProfileDetails {

    /**
     * @return a human interpretable name for this ValidationProfile
     */
    public abstract String getName();

    /**
     * @return a brief textual description of this ValidationProfile
     */
    public abstract String getDescription();

    /**
     * @return a String that identifies the creator of this ValidationProfile
     */
    public abstract String getCreator();

    /**
     * @return the Date that this ValidationProfile was created
     */
    public abstract Date getDateCreated();

}