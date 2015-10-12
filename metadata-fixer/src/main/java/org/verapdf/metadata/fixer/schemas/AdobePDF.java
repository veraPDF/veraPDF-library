package org.verapdf.metadata.fixer.schemas;

/**
 * @author Evgeniy Muravitskiy
 */
public interface AdobePDF extends BasicSchema {

	String getProducer();

	void setProducer(String producer);

	String getKeywords();

	void setKeywords(String keywords);
}
