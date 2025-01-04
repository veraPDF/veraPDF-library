/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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
package org.verapdf;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAttribute;
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
	private static final Logger LOGGER = Logger.getLogger(ReleaseDetails.class.getCanonicalName());
	public static final String APPLICATION_PROPERTIES_ROOT = "org/verapdf/release/"; //$NON-NLS-1$
	public static final String PROPERTIES_EXT = "properties"; //$NON-NLS-1$
	public static final String LIBRARY_DETAILS_RESOURCE = APPLICATION_PROPERTIES_ROOT + "library." + PROPERTIES_EXT; //$NON-NLS-1$

	private static final String RAW_DATE_FORMAT = "${maven.build.timestamp.format}"; //$NON-NLS-1$
	private static final String RIGHTS = "Developed and released by the veraPDF Consortium.\n" //$NON-NLS-1$
			+ "Funded by the PREFORMA project.\n" + "Released under the GNU General Public License v3\n"  //$NON-NLS-1$//$NON-NLS-2$
			+ "and the Mozilla Public License v2 or later.\n"; //$NON-NLS-1$

	private static final ReleaseDetails DEFAULT = new ReleaseDetails();
	private static final Map<String, ReleaseDetails> DETAILS = initDetailsMap();

	@XmlAttribute
	private final String id;
	@XmlAttribute
	private final String version;
	@XmlAttribute
	private final Date buildDate;

	private ReleaseDetails() {
		this("name", "version", new Date()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private ReleaseDetails(final String id, final String version, final Date buildDate) {
		this.id = id;
		this.version = version;
		this.buildDate = new Date(buildDate.getTime());
	}

	/**
	 * @return the id of the release artifact
	 */
	public String getId() {
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
	 * { @inheritDoc }
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result + ((this.buildDate == null) ? 0 : this.buildDate.hashCode());
		result = prime * result + ((this.version == null) ? 0 : this.version.hashCode());
		return result;
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ReleaseDetails other = (ReleaseDetails) obj;
		if (!Objects.equals(this.buildDate, other.buildDate)) {
			return false;
		}
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return Objects.equals(this.version, other.version);
	}

	/**
	 * { @inheritDoc }
	 */
	@Override
	public String toString() {
		return "ReleaseDetails [id=" + this.id + ", version=" + this.version + ", buildDate=" + this.buildDate + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	public static ReleaseDetails defaultInstance() {
		return DEFAULT;
	}

	/**
	 * @return the rights statement for the veraPDF software
	 */
	public static String rightsStatement() {
		return RIGHTS;
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

	public static Collection<ReleaseDetails> getDetails() {
		return DETAILS.values();
	}

	/**
	 * Will load a ReleaseDetails instance from the resource found with
	 * resourceName. This should be a properties file with the appropriate
	 * release details properties.
	 * 
	 * @param resourceName
	 *            the name of the resource to load
	 */
	public static ReleaseDetails addDetailsFromResource(final String resourceName) {
		ReleaseDetails details = fromResource(resourceName);
		DETAILS.put(details.id, details);
		return details;
	}

	private static ReleaseDetails fromResource(final String resourceName) {
		try (InputStream is = ReleaseDetails.class.getClassLoader().getResourceAsStream(resourceName)) {
			if (is == null) {
				return DEFAULT;
			}
			return fromPropertiesStream(is);
		} catch (IOException excep) {
			throw new IllegalStateException("Couldn't load ReleaseDetails resource:" + resourceName, excep); //$NON-NLS-1$
		}
	}

	private static ReleaseDetails fromPropertiesStream(final InputStream is) throws IOException {
		Properties props = new Properties();
		props.load(is);
		return fromProperties(props);
	}

	private static ReleaseDetails fromProperties(final Properties props) {
		String release = props.getProperty("verapdf.release.version"); //$NON-NLS-1$
		String dateFormat = props.getProperty("verapdf.date.format"); //$NON-NLS-1$
		String id = props.getProperty("verapdf.project.id"); //$NON-NLS-1$
		Date date = new Date();
		if (!dateFormat.equals(RAW_DATE_FORMAT)) {
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
			try {
				date = formatter.parse(props.getProperty("verapdf.release.date")); //$NON-NLS-1$
			} catch (ParseException e) {
				/**
				 * Safe to ignore this exception as release simply set to new
				 * date.
				 */
				LOGGER.log(Level.FINEST, "No parsable release date found, setting release date to:" + date, e); //$NON-NLS-1$
			}
		}
		return new ReleaseDetails(id, release, date);
	}

	private static Map<String, ReleaseDetails> initDetailsMap() {
		Map<String, ReleaseDetails> details = new HashMap<>();
		ReleaseDetails libDetails = fromResource(LIBRARY_DETAILS_RESOURCE);
		details.put(libDetails.id, libDetails);
		return details;
	}

}
