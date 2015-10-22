package org.verapdf.metadata.fixer.schemas;

/**
 * Current interface represent XMP Basic schema
 *
 * @author Evgeniy Muravitskiy
 */
public interface XMPBasic extends BasicSchema {

	/**
	 * Return Creator entry. For information dictionary
	 * represented by {@code Creator} entry, in metadata -
	 * {@code CreatorTool} entry
	 *
	 * @return Creator entry
	 */
	String getCreator();

	/**
	 * Set Creator entry. For information dictionary
	 * represented by {@code Creator} entry, in metadata -
	 * {@code CreatorTool} entry
	 *
	 * @param creatorTool new CreatorTool value
	 */
	void setCreator(String creatorTool);

	/**
	 * Return Creation Date entry. For information dictionary
	 * represented by {@code CreationDate} entry, in metadata -
	 * {@code CreationDate} entry
	 *
	 * @return Creation Date entry
	 */
	String getCreationDate();

	/**
	 * Set Creation Date entry. For information dictionary
	 * represented by {@code CreationDate} entry, in metadata -
	 * {@code CreationDate} entry
	 *
	 * @param creationDate new Creation Date value
	 */
	void setCreationDate(String creationDate);

	/**
	 * Return Modification Date entry. For information dictionary
	 * represented by {@code ModDate} entry, in metadata -
	 * {@code ModifyDate} entry
	 *
	 * @return Modification Date entry
	 */
	String getModificationDate();

	/**
	 * Set Modification Date entry. For information dictionary
	 * represented by {@code ModDate} entry, in metadata -
	 * {@code ModifyDate} entry
	 *
	 * @param modificationDate new Modification Date value
	 */
	void setModificationDate(String modificationDate);
}
