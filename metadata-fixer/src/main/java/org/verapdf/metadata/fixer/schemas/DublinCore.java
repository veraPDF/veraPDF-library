package org.verapdf.metadata.fixer.schemas;

/**
 * @author Evgeniy Muravitskiy
 */
public interface DublinCore extends BasicSchema {

	String getTitle();

	void setTitle(String title);

	// description in metadata
	String getSubject();

	void setSubject(String description);

	// creator in metadata
	String getAuthor();

	void setAuthor(String creator);

}
