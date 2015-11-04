package org.verapdf.validation.profile.model;

/**
 * Structure of the fix in a rule.
 *
 * @author Maksim Bezrukov
 */
public class Fix {
	private final String ID;
	private final String description;
	private final String info;
	private final String error;

	/**
	 * Creates fix model
	 *
	 * @param ID          id of the fix
	 * @param description description of the fix
	 * @param info        info of the fix
	 * @param error       error of the fix
	 */
	public Fix(final String ID, final String description, final String info, final String error) {
		this.ID = ID;
		this.description = description;
		this.info = info;
		this.error = error;
	}

	/**
	 * @return Text provided by attribute "id".
	 */
	public String getID() {
		return this.ID;
	}

	/**
	 * @return Text in tag "description".
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return Class which represents an info in this fix.
	 */
	public String getInfo() {
		return this.info;
	}

	/**
	 * @return Class which represents an error in this fix.
	 */
	public String getError() {
		return this.error;
	}
}
