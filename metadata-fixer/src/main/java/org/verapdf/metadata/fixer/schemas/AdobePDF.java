package org.verapdf.metadata.fixer.schemas;

/**
 * Interface represent Adobe PDF schema
 *
 * @author Evgeniy Muravitskiy
 */
public interface AdobePDF extends BasicSchema {

	/**
	 * Return Producer entry. For information dictionary and
	 * metadata Adobe PDF schema represented by {@code Producer} entry
	 *
	 * @return Producer entry
	 */
	String getProducer();

	/**
	 * Set Producer entry. For information dictionary and
	 * metadata Adobe PDF schema represented by {@code Producer} entry
	 *
	 * @param producer new value for producer
	 */
	void setProducer(String producer);

	/**
	 * Return Keywords entry. For information dictionary and
	 * metadata Adobe PDF schema represented by {@code Keywords} entry
	 *
	 * @return Keywords entry
	 */
	String getKeywords();

	/**
	 * Set Keywords entry. For information dictionary and
	 * metadata Adobe PDF schema represented by {@code Keywords} entry
	 *
	 * @param keywords new value for keywords
	 */
	void setKeywords(String keywords);
}
