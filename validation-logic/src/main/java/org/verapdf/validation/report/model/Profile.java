package org.verapdf.validation.report.model;

/**
 * Structure of the profile info in validation info.
 *
 * @author Maksim Bezrukov
 */
public class Profile {
	private final String name;
	private final String hash;

	/**
	 * Creates model of an information about used profile
	 *
	 * @param name name of the validation profile
	 * @param hash hash code of the validation profile
	 */
	public Profile(final String name, final String hash) {
		this.name = name;
		this.hash = hash;
	}

	/**
	 * @return Name of the validation profile.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Hash of the validation profile.
	 */
	public String getHash() {
		return hash;
	}
}
