/**
 * 
 */
package org.verapdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class that encapsulates the release details of the veraPDF validation
 * library. The class controls instance creation so that only a single, static
 * and immutable instance is available.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
@XmlRootElement(name = "releaseDetails")
public final class ReleaseDetails {

    public static final String APPLICATION_PROPERTIES_ROOT = "org/verapdf/release/";
    public static final String PROPERTIES_EXT = "properties";
    public static final String LIBRARY_DETAILS_RESOURCE = APPLICATION_PROPERTIES_ROOT
            + "library." + PROPERTIES_EXT;

    private static final String RAW_DATE_FORMAT = "${maven.build.timestamp.format}";
    private static final String RIGHTS = "Developed and released by the veraPDF Consortium.\n"
            + "Funded by the PREFORMA project.\n"
            + "Released under the GNU General Public License v3\n"
            + "and the Mozilla Public License v2.\n";

    private static final ReleaseDetails DEFAULT = new ReleaseDetails();
    private static final Map<String, ReleaseDetails> DETAILS = new HashMap<>();
    static {
        ReleaseDetails details = fromResource(LIBRARY_DETAILS_RESOURCE);
        DETAILS.put(details.id, details);
    }

    @XmlAttribute
    private final String id;
    @XmlAttribute
    private final String version;
    @XmlAttribute
    private final Date buildDate;
    @XmlElement
    private final String rights = RIGHTS;

    private ReleaseDetails() {
        this("name", "version", new Date());
    }

    private ReleaseDetails(final String id, final String version,
            final Date buildDate) {
        this.id = id;
        this.version = version;
        this.buildDate = new Date(buildDate.getTime());
    }

    /**
     * @return the id of the release artifact
     */
    public String gitId() {
        return this.id;
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
    public String getRights() {
        return this.rights;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
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
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
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
        return "ReleaseDetails [id=" + this.id + ", version=" + this.version
                + ", buildDate=" + this.buildDate + "]";
    }

    public static ReleaseDetails defaultInstance() {
        return DEFAULT;
    }

    /**
     * @return the static immutable ReleaseDetails instance
     */
    public static ReleaseDetails getInstance() {
        if (DETAILS.isEmpty()) {
            return defaultInstance();
        }
        return DETAILS.values().toArray(new ReleaseDetails[DETAILS.size()])[0];
    }

    /**
     * @return the Set of ReleaseDetails ids as Strings
     */
    public static Set<String> getIds() {
        return DETAILS.keySet();
    }

    /**
     * Retrieve ReleaseDetails by id
     * 
     * @param id
     *            the id to lookup
     * @return the
     */
    public static ReleaseDetails byId(final String id) {
        if (DETAILS.containsKey(id)) {
            return DETAILS.get(id);
        }
        return defaultInstance();
    }

    /**
     * Will load a ReleaseDetails instance from the resource found with
     * resourceName. This should be a properties file with the appropriate
     * release details properties.
     * 
     * @param resourceName
     *            the name of the resource to load
     */
    public static void addDetailsFromResource(final String resourceName) {
        ReleaseDetails details = fromResource(resourceName);
        DETAILS.put(details.id, details);
    }

    static void toXml(final ReleaseDetails toConvert,
            final OutputStream stream, Boolean prettyXml) throws JAXBException {
        Marshaller varMarshaller = getMarshaller(prettyXml);
        varMarshaller.marshal(toConvert, stream);
    }

    static ReleaseDetails fromXml(final InputStream toConvert)
            throws JAXBException {
        Unmarshaller stringUnmarshaller = getUnmarshaller();
        return (ReleaseDetails) stringUnmarshaller.unmarshal(toConvert);
    }

    private static ReleaseDetails fromResource(final String resourceName) {
        try (InputStream is = ReleaseDetails.class.getClassLoader()
                .getResourceAsStream(resourceName)) {
            if (is == null) {
                return DEFAULT;
            }
            return fromPropertiesStream(is);
        } catch (IOException excep) {
            throw new IllegalStateException(
                    "Couldn't load ReleaseDetails resource:" + resourceName,
                    excep);
        }
    }

    private static ReleaseDetails fromPropertiesStream(final InputStream is)
            throws IOException {
        Properties props = new Properties();
        props.load(is);
        return fromProperties(props);
    }

    private static ReleaseDetails fromProperties(final Properties props) {
        String release = props.getProperty("verapdf.release.version");
        String dateFormat = props.getProperty("verapdf.date.format");
        String id = props.getProperty("verapdf.project.id");
        Date date = new Date();
        if (!dateFormat.equals(RAW_DATE_FORMAT)) {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            try {
                date = formatter.parse(props
                        .getProperty("verapdf.release.date"));
            } catch (ParseException e) {
                /**
                 * Safe to ignore this exception as release simply set to new
                 * date.
                 */
            }
        }
        return new ReleaseDetails(id, release, date);
    }

    private static Unmarshaller getUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ReleaseDetails.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller;
    }

    private static Marshaller getMarshaller(Boolean setPretty)
            throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ReleaseDetails.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, setPretty);
        return marshaller;
    }

}
