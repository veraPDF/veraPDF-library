package org.verapdf.validation.report.model;

import org.verapdf.validation.profile.model.ValidationProfile;

/**
 * Structure of the profile info in validation info.
 *
 * @author Maksim Bezrukov
 */
public class Profile {

	private final String name;
	private final String hash;
	private final ValidationProfile validationProfile;

	/**
	 * Creates model of an information about used profile
	 *
	 * @param name name of the validation profile
	 * @param hash hash code of the validation profile
	 */
	public Profile(final String name, final String hash, ValidationProfile validationProfile) {
		this.name = name;
		this.hash = hash;
		this.validationProfile = validationProfile;
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

	/**
	 * @return representation of validation profile
	 */
	public ValidationProfile getValidationProfile() {
		return validationProfile;
	}

}
