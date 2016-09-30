/**
 * 
 */
package org.verapdf.report;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 22 Sep 2016:07:02:09
 */
@XmlRootElement(name = "batchSummary")
public class BatchSummary {
	@XmlElement
	private final TaskDetails taskDetails;
	@XmlAttribute
	private final int itemsProcessed;
	@XmlAttribute
	private final int validItems;
	@XmlAttribute
	private final int invalidItems;

	private BatchSummary() {
		this(null, 0, 0, 0);
	}
	
	public BatchSummary(final TaskDetails taskDetails, final int itemsProcessed, final int validItems,
			final int invalidItems) {
		this.taskDetails = taskDetails;
		this.itemsProcessed = itemsProcessed;
		this.validItems = validItems;
		this.invalidItems = invalidItems;
	}

	public TaskDetails getTaskDetails() {
		return taskDetails;
	}

	public int getItemsProcessed() {
		return itemsProcessed;
	}

	public int getValidItems() {
		return validItems;
	}

	public int getInvalidItems() {
		return invalidItems;
	}
}
