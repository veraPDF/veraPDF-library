/**
 * 
 */
package org.verapdf.pdfa.qa;

import java.util.Date;

import org.verapdf.ReleaseDetails;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public class ResultSetDetailsImpl implements ResultSetDetails {
    private final Date created;
    private final String libVersion;
    private final Date libBuild;

    private ResultSetDetailsImpl() {
        this(new Date(), ReleaseDetails.getInstance().getVersion(), ReleaseDetails.getInstance().getBuildDate());
    }
    private ResultSetDetailsImpl(final Date created, final String libVersion, final Date libBuild) {
        this.created = new Date(created.getTime());
        this.libVersion = libVersion;
        this.libBuild = new Date(libBuild.getTime());
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
    public String getLibraryVersion() {
        return this.libVersion;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public Date getLibraryBuildDate() {
        return this.libBuild;
    }
    /**
     * { @inheritDoc }
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.created == null) ? 0 : this.created.hashCode());
        result = prime * result
                + ((this.libBuild == null) ? 0 : this.libBuild.hashCode());
        result = prime * result
                + ((this.libVersion == null) ? 0 : this.libVersion.hashCode());
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
        if (!(obj instanceof ResultSetDetails))
            return false;
        ResultSetDetails other = (ResultSetDetails) obj;
        if (this.created == null) {
            if (other.getDateCreated() != null)
                return false;
        } else if (!this.created.equals(other.getDateCreated()))
            return false;
        if (this.libBuild == null) {
            if (other.getLibraryBuildDate() != null)
                return false;
        } else if (!this.libBuild.equals(other.getLibraryBuildDate()))
            return false;
        if (this.libVersion == null) {
            if (other.getLibraryVersion() != null)
                return false;
        } else if (!this.libVersion.equals(other.getLibraryVersion()))
            return false;
        return true;
    }
    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "ResultSetDetails [created=" + this.created + ", libVersion="
                + this.libVersion + ", libBuild=" + this.libBuild + "]";
    }

    /**
     * @return a new ResultSetDetails instance
     */
    public static ResultSetDetails getNewInstance() {
        return new ResultSetDetailsImpl();
    }
}
