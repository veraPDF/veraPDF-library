package org.verapdf.metadata.fixer.schemas;

/**
 * Current interface represent Dublin Core schema
 *
 * @author Evgeniy Muravitskiy
 */
public interface DublinCore extends BasicSchema {

	/**
	 * Return Title entry. For information dictionary
	 * represented by {@code Title} entry, in metadata -
	 * {@code title} entry
	 *
	 * @return Title entry
	 */
	String getTitle();

	/**
	 * Set Title entry. For information dictionary
	 * represented by {@code Title} entry, in metadata -
	 * {@code title} entry
	 *
	 * @param title new Title value
	 */
	void setTitle(String title);

	/**
	 * Return Subject entry. For information dictionary
	 * represented by {@code Subject} entry, in metadata -
	 * {@code description} entry
	 *
	 * @return Subject entry
	 */
	String getSubject();

	/**
	 * Set Subject entry. For information dictionary
	 * represented by {@code Subject} entry, in metadata -
	 * {@code description} entry
	 *
	 * @param description new Subject value
	 */
	void setSubject(String description);

	/**
	 * Return Author entry. For information dictionary
	 * represented by {@code Author} entry, in metadata -
	 * {@code creator} entry
	 *
	 * @return Author entry
	 */
	String getAuthor();

	/**
	 * Set Author entry. For information dictionary
	 * represented by {@code Author} entry, in metadata -
	 * {@code creator} entry
	 *
	 * @param creator new Author value
	 */
	void setAuthor(String creator);

}
