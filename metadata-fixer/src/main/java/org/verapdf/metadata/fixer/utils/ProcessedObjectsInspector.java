package org.verapdf.metadata.fixer.utils;

import org.verapdf.metadata.fixer.entity.FixReport;
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
 * @author Evgeniy Muravitskiy
 */
public class ProcessedObjectsInspector {

	private ProcessedObjectsInspector() {
		// hide default constructor
	}

	public static FixReport validationStatus(List<Rule> rules, ValidationProfile profile)
			throws URISyntaxException, IOException, ParserConfigurationException, SAXException {
		ProcessedObjectsParser parser = XMLProcessedObjectsParser.getInstance();
		return validationStatus(rules, profile, parser);
	}

	public static FixReport validationStatus(List<Rule> rules, ValidationProfile profile, ProcessedObjectsParser parser)
			throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
		return validationStatus(rules, profile, parser.getProcessedObjects());
	}

	public static FixReport validationStatus(List<Rule> rules, ValidationProfile profile, ProcessedObjects objects)
			throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
		FixReport entity = new FixReport();

		for (Rule rule : rules) {
			if (rule.getStatus() == Check.Status.FAILED) {
				checkCurrentRule(profile, objects, rule, entity);
			}
		}

		return entity;
	}

	private static void checkCurrentRule(ValidationProfile profile, ProcessedObjects objects,
										 Rule rule, FixReport entity) {
		org.verapdf.validation.profile.model.Rule profileRule = profile.getRuleById(rule.getID());
		String objectType = profileRule.getAttrObject();
		if (objects.contains(objectType, profileRule.getTest())) {
			entity.setStatus(ValidationStatus.INVALID_METADATA.getStatus(entity.getStatus()));
		} else {
			entity.setStatus(ValidationStatus.INVALID_STRUCTURE.getStatus(entity.getStatus()));
		}
	}

}
