package org.verapdf.metadata.fixer.schemas;

/**
 * @author Evgeniy Muravitskiy
 */
public interface XMPBasic extends BasicSchema {

	// CreatorTool in metadata
	String getCreator();

	void setCreator(String creatorTool);

	String getCreationDate();

	void setCreationDate(String creationDate);

	String getModificationDate();

	void setModificationDate(String modificationDate);
}
