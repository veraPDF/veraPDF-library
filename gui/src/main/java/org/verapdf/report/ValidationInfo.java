package org.verapdf.report;

import org.verapdf.pdfa.MetadataFixerResult;
import org.verapdf.pdfa.results.ValidationResult;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Maksim Bezrukov
 */
@XmlRootElement(name = "validationInfo")
public class ValidationInfo {

	@XmlElement
	private final String profile;
	@XmlElement
	private final Result result;


	ValidationInfo() {
		this("", new Result());
	}

	private ValidationInfo(String profile, Result result) {
		this.profile = profile;
		this.result = result;
	}

	static ValidationInfo fromValues(ValidationResult result, MetadataFixerResult fixerResult) {
		String profile = result.getPDFAFlavour().getLevel().getName();
		Result resultType = Result.fromValues(result, fixerResult);
		return new ValidationInfo(profile, resultType);
	}
}
