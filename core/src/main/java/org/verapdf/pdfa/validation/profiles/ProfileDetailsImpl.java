/**
 * 
 */
package org.verapdf.pdfa.validation.profiles;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
final class ProfileDetailsImpl implements ProfileDetails {
    private static final ProfileDetailsImpl DEFAULT = new ProfileDetailsImpl();
    @XmlElement
    private final String name;
    @XmlElement
    private final String description;
    @XmlAttribute
    private final String creator;
    @XmlAttribute
    private final Date created;

    private ProfileDetailsImpl() {
        this("name", "description", "creator", new Date(0L));
    }

    private ProfileDetailsImpl(final String name, final String description,
            final String creator, final Date created) {
        super();
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.created = created;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getCreator() {
        return this.creator;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Date getDateCreated() {
        return this.created;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.created == null) ? 0 : this.created.hashCode());
        result = prime * result
                + ((this.creator == null) ? 0 : this.creator.hashCode());
        result = prime
                * result
                + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result
                + ((this.name == null) ? 0 : this.name.hashCode());
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
        if (!(obj instanceof ProfileDetails))
            return false;
        ProfileDetails other = (ProfileDetails) obj;
        if (this.created == null) {
            if (other.getDateCreated() != null)
                return false;
        } else if (!this.created.equals(other.getDateCreated()))
            return false;
        if (this.creator == null) {
            if (other.getCreator() != null)
                return false;
        } else if (!this.creator.equals(other.getCreator()))
            return false;
        if (this.description == null) {
            if (other.getDescription() != null)
                return false;
        } else if (!this.description.equals(other.getDescription()))
            return false;
        if (this.name == null) {
            if (other.getName() != null)
                return false;
        } else if (!this.name.equals(other.getName()))
            return false;
        return true;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "ProfileDetails [name=" + this.name + ", description="
                + this.description + ", creator=" + this.creator + ", created="
                + this.created + "]";
    }

    static ProfileDetailsImpl defaultInstance() {
        return DEFAULT;
    }

    static ProfileDetailsImpl fromValues(final String name,
            final String description, final String creator, final Date created) {
        return new ProfileDetailsImpl(name, description, creator, created);
    }


    static class Adapter extends
            XmlAdapter<ProfileDetailsImpl, ProfileDetails> {
        @Override
        public ProfileDetailsImpl unmarshal(ProfileDetailsImpl profileImpl) {
            return profileImpl;
        }

        @Override
        public ProfileDetailsImpl marshal(ProfileDetails profile) {
            return (ProfileDetailsImpl) profile;
        }
    }
}
