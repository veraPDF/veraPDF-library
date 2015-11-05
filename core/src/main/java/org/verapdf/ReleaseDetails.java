/**
 * 
 */
package org.verapdf;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
public final class ReleaseDetails {
    private static final String APPLICATION_PROPERTIES_PATH = "org/verapdf/verapdf.properties";
    private static final String RAW_DATE_FORMAT = "${maven.build.timestamp.format}";
    private static final String RIGHTS = "Developed and released by the veraPDF Consortium.\n"
            + "Funded by the PREFORMA project.\n"
            + "Released under the GNU General Public License v3.\n";

    private static final ReleaseDetails INSTANCE = fromPropertyResource(APPLICATION_PROPERTIES_PATH);

    private final String version;
    private final Date buildDate;

    private ReleaseDetails() {
        throw new AssertionError("Should never enter JhoveReleaseDetails().");
    }

    private ReleaseDetails(final String version, final Date buildDate) {
        this.version = version;
        this.buildDate = new Date(buildDate.getTime());
    }

    /**
     * @return the veraPDF library version number
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * @return the veraPDF library build date
     */
    public Date getBuildDate() {
        return new Date(this.buildDate.getTime());
    }

    /**
     * @return the veraPDF library rights statement
     */
    @SuppressWarnings("static-method")
    public String getRights() {
        return RIGHTS;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.buildDate == null) ? 0 : this.buildDate.hashCode());
        result = prime * result
                + ((this.version == null) ? 0 : this.version.hashCode());
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
        if (getClass() != obj.getClass())
            return false;
        ReleaseDetails other = (ReleaseDetails) obj;
        if (this.buildDate == null) {
            if (other.buildDate != null)
                return false;
        } else if (!this.buildDate.equals(other.buildDate))
            return false;
        if (this.version == null) {
            if (other.version != null)
                return false;
        } else if (!this.version.equals(other.version))
            return false;
        return true;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String toString() {
        return "ReleaseDetails [version=" + this.version + ", buildDate="
                + this.buildDate + "]";
    }

    /**
     * @return the static immutable ReleaseDetails instance
     */
    public static ReleaseDetails getInstance() {
        return INSTANCE;
    }

    private static ReleaseDetails fromPropertyResource(
            final String propertyResourceName) {
        try (InputStream is = ReleaseDetails.class.getClassLoader()
                .getResourceAsStream(propertyResourceName)) {
            if (is == null) {
                throw new IllegalStateException(
                        "No application properties found: "
                                + propertyResourceName);
            }
            Properties props = new Properties();
            props.load(is);
            return fromProperties(props);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Application properties could not be loaded: "
                            + propertyResourceName, e);
        }
    }

    private static ReleaseDetails fromProperties(final Properties props) {
        String release = props.getProperty("verapdf.release.version");
        String dateFormat = props.getProperty("verapdf.date.format");
        Date date = new Date();
        if (!dateFormat.equals(RAW_DATE_FORMAT)) {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            try {
                date = formatter.parse(props.getProperty("verapdf.release.date"));
            } catch (ParseException e) {
                /**
                 * Safe to ignore this exception as release simply set to new date.
                 */
            }
        }
        return new ReleaseDetails(release, date);
    }
}
