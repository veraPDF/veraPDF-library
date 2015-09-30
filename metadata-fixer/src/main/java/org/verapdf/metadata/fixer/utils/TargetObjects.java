package org.verapdf.metadata.fixer.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.verapdf.metadata.fixer.entity.FixEntity;
import org.verapdf.metadata.fixer.entity.ValidationStatus;
import org.verapdf.metadata.fixer.utils.json.AppliedObjects;
import org.verapdf.validation.profile.model.ValidationProfile;
import org.verapdf.validation.report.model.Check;
import org.verapdf.validation.report.model.Rule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

/**
 * @author Evgeniy Muravitskiy
 */
public class TargetObjects {

	public final static String APPLIED_OBJECTS_PROPERTIES_PATH = "/applied-objects.properties";
	public static final String APPLIED_OBJECTS_PATH = "applied.objects.path";

	private final static ObjectMapper MAPPER = new ObjectMapper();

	public static FixEntity validationStatus(List<Rule> rules, ValidationProfile profile)
			throws URISyntaxException, IOException {
		AppliedObjects objects = getAppliedObjects();
		FixEntity entity = new FixEntity();

		for (Rule rule : rules) {
			if (rule.getStatus() == Check.Status.FAILED) {
				checkCurrentRule(profile, objects, rule, entity);
			}
		}

		return entity;
	}

	public static AppliedObjects getAppliedObjects() throws IOException, URISyntaxException {
		Properties prop = new Properties();
		InputStream inputStream = ClassLoader.class.getResourceAsStream(APPLIED_OBJECTS_PROPERTIES_PATH);
		prop.load(inputStream);
		String appliedObjectsPath = prop.getProperty(APPLIED_OBJECTS_PATH);
		File json = new File(getSystemIndependentPath(appliedObjectsPath));
		return MAPPER.readValue(json, AppliedObjects.class);
	}

	private static void checkCurrentRule(ValidationProfile profile, AppliedObjects objects,
										 Rule rule, FixEntity entity) {
		org.verapdf.validation.profile.model.Rule profileRule = profile.getRuleById(rule.getID());
		String objectType = profileRule.getAttrObject();
		if (objects.contains(objectType, profileRule.getTest())) {
			entity.setStatus(ValidationStatus.INVALID_METADATA.getStatus(entity.getStatus()));
		} else {
			entity.setStatus(ValidationStatus.INVALID_STRUCTURE.getStatus(entity.getStatus()));
		}
	}

	private static String getSystemIndependentPath(String path) throws URISyntaxException {
		URL resourceUrl = ClassLoader.class.getResource(path);
		Path resourcePath = Paths.get(resourceUrl.toURI());
		return resourcePath.toString();
	}

}
