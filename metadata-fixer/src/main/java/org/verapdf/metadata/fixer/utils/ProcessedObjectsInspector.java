package org.verapdf.metadata.fixer.utils;

import org.verapdf.metadata.fixer.entity.ValidationStatus;
import org.verapdf.metadata.fixer.utils.model.ProcessedObjects;
import org.verapdf.metadata.fixer.utils.parser.ProcessedObjectsParser;
import org.verapdf.metadata.fixer.utils.parser.XMLProcessedObjectsParser;
import org.verapdf.validation.profile.model.ValidationProfile;
import org.verapdf.validation.report.model.Check;
import org.verapdf.validation.report.model.Rule;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Current class check status of validation according to failed rules,
 * validation profile and list of Metadata failed rule types.
 * <p>
 *     Metadata failed rule type is list of rules that belongs to metadata
 * 	   check rules. This rules can be obtain by {@link ProcessedObjectsParser}.
 * 	   Default implementation of this interface is {@link XMLProcessedObjectsParser}.
 * </p>
 *
 * @author Evgeniy Muravitskiy
 *
 * @see Rule
 * @see ValidationProfile
 * @see ValidationStatus
 */
public class ProcessedObjectsInspector {

	private ProcessedObjectsInspector() {
		// hide default constructor
	}

	/**
	 * Return validation status according to failed rules and validation profile.
	 * {@link ProcessedObjects} obtain from xml file.
	 *
	 * @param rules list of rules
	 * @param profile validation profile
	 *
	 * @return validation status
	 */
	public static ValidationStatus validationStatus(List<Rule> rules, ValidationProfile profile)
			throws URISyntaxException, IOException, ParserConfigurationException, SAXException {
		ProcessedObjectsParser parser = XMLProcessedObjectsParser.getInstance();
		return validationStatus(rules, profile, parser);
	}

	/**
	 * Return validation status according to failed rules and validation profile.
	 * {@link ProcessedObjects} obtain by {@link ProcessedObjectsParser}.
	 *
	 * @param rules list of rules
	 * @param profile validation profile
	 * @param parser parse some data to {@code ProcessedObjects}
	 *
	 * @return validation status
	 */
	public static ValidationStatus validationStatus(
			List<Rule> rules, ValidationProfile profile, ProcessedObjectsParser parser)
			throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
		return validationStatus(rules, profile, parser.getProcessedObjects());
	}

	/**
	 * Return validation status according to failed rules, validation profile
	 * and metadata failed rules type
	 *
	 * @param rules list of rules
	 * @param profile validation profile
	 * @param objects metadata failed rules type
	 *
	 * @return validation status
	 */
	public static ValidationStatus validationStatus(
			List<Rule> rules, ValidationProfile profile, ProcessedObjects objects)
			throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
		ValidationStatus status = ValidationStatus.VALID;

		for (Rule rule : rules) {
			if (rule.getStatus() == Check.Status.FAILED) {
				status = checkCurrentRule(profile, objects, rule, status);
			}
		}

		return status;
	}

	private static ValidationStatus checkCurrentRule(ValidationProfile profile, ProcessedObjects objects,
													 Rule rule, ValidationStatus status) {
		org.verapdf.validation.profile.model.Rule profileRule = profile.getRuleById(rule.getID());
		String objectType = profileRule.getAttrObject();
		if (objects.contains(objectType, profileRule.getTest())) {
			return ValidationStatus.INVALID_METADATA.getStatus(status);
		} else {
			return ValidationStatus.INVALID_STRUCTURE.getStatus(status);
		}
	}

}
