/**
 * 
 */
package org.verapdf.report;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.ValidationResult;
import org.verapdf.pdfa.results.ValidationResults;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 21 Sep 2016:23:41:31
 */
@XmlRootElement(name="validationSummary")
public class ValidationSummary {
	static final ValidationSummary DEFAULT = new ValidationSummary();

	@XmlElement
	private final ItemDetails item;
	@XmlElement
	private final TaskDetails taskDetails;
	private final PDFAFlavour flavour;
	private final boolean isCompliant;
	private ValidationDetails details;

	private ValidationSummary() {
		this(ItemDetails.DEFAULT, ValidationResults.defaultResult(), TaskDetails.DEFAULT);
	}

	public ValidationSummary(final ItemDetails item, final ValidationResult result, final TaskDetails taskDetails) {
		this.item = item;
		this.flavour = result.getPDFAFlavour();
		this.isCompliant = result.isCompliant();
		this.details = ValidationDetails.fromValues(result, false, 1);
		this.taskDetails = taskDetails;
	}
	
	@XmlAttribute(name="itemSize")
	public long getItemSize() {
		return this.item.getSize();
	}
	
	@XmlAttribute(name="flavour")
	public String getFlavour() {
		return this.flavour.getId();
	}
	
	@XmlAttribute(name="validity")
	public String getValidity() {
		return this.isCompliant ? "valid" : "invalid";
	}

	@XmlAttribute(name="checksPassed")
	public int getPassedChecks() {
		return this.details.getPassedChecks();
	}
	
	@XmlAttribute(name="checksFailed")
	public int getFailedChecks() {
		return this.details.getFailedChecks();
	}
	
	public boolean isValid() {
		return this.isCompliant;
	}
}
