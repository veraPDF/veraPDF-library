package org.verapdf.processor;

import java.util.EnumSet;

import org.verapdf.features.FeatureExtractorConfig;
import org.verapdf.metadata.fixer.MetadataFixerConfig;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;
import org.verapdf.pdfa.validation.validators.ValidatorConfig;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 30 Oct 2016:22:13:34
 */
public interface ProcessorConfig {
	public ValidationProfile getCustomProfile();
	/**
	 * @return
	 */
	public ValidatorConfig getValidatorConfig();

	/**
	 * @return
	 */
	public FeatureExtractorConfig getFeatureConfig();

	/**
	 * @return
	 */
	public MetadataFixerConfig getFixerConfig();

	/**
	 * @return
	 */
	public EnumSet<TaskType> getTasks();

	/**
	 * @param toCheck
	 * @return
	 */
	public boolean hasTask(TaskType toCheck);

}