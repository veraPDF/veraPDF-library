package org.verapdf.component;

import java.net.URI;

public interface ComponentDetails {
	/**
	 * @return the id
	 */
	URI getId();

	/**
	 * @return the name
	 */
	String getName();

	/**
	 * @return the version
	 */
	String getVersion();

	/**
	 * @return the provider
	 */
	String getProvider();

	/**
	 * @return the description
	 */
	String getDescription();

}