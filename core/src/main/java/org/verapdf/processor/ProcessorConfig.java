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

	/**
	 * @return the custom {@link ValidationProfile} or a
	 *         {@link Profiles#defaultProfile()} instance if no custom profile
	 *         is assigned.
	 */
	public ValidationProfile getCustomProfile();

	/**
	 * @return true if this configuration has been assigned a custom
	 *         {@link ValidationProfile}
	 */
	public boolean hasCustomProfile();

	/**
	 * @return the {@link ValidatorConfig} assigned to this configuration, or
	 *         {@link ValidatorFactory#defaultConfig()} if no validation task
	 *         has been assigned.
	 */
	public ValidatorConfig getValidatorConfig();

	/**
	 * @return the {@link FeatureExtractorConfig} assigned to this configuration
	 *         or {@link FeatureFactory#defaultConfig()} if no feature
	 *         extraction task has been assigned.
	 */
	public FeatureExtractorConfig getFeatureConfig();

	/**
	 * @return the {@link MetadataFixerConfig} assigned to this configuration or
	 *         {@link FixerFactory#defaultConfig()} if no MetadataFixer task has
	 *         been assigned.
	 */
	public MetadataFixerConfig getFixerConfig();

	public String getMetadataFolder();

	/**
	 * @return the full {@link EnumSet} of {@link TaskType}s assigned in this
	 *         configuration.
	 */
	public EnumSet<TaskType> getTasks();

	/**
	 * @param toCheck
	 *            the {@link TaskType} to check
	 * @return true if this configuration has been assigned a task of
	 *         {@link TaskType} toCheck.
	 */
	public boolean hasTask(TaskType toCheck);

}