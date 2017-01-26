/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * veraPDF Library core is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with veraPDF Library core as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * veraPDF Library core as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
package org.verapdf.processor;

import org.verapdf.features.FeatureExtractorConfig;
import org.verapdf.metadata.fixer.MetadataFixerConfig;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;
import org.verapdf.pdfa.validation.validators.ValidatorConfig;
import org.verapdf.processor.plugins.PluginsCollectionConfig;

import java.util.EnumSet;

/**
 * Configuration for a veraPDF {@link Processor} or {@link BatchProcessor}.
 * 
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

	public PluginsCollectionConfig getPluginsCollectionConfig();

	/**
	 * @return the {@link MetadataFixerConfig} assigned to this configuration or
	 *         {@link FixerFactory#defaultConfig()} if no MetadataFixer task has
	 *         been assigned.
	 */
	public MetadataFixerConfig getFixerConfig();

	/**
	 * @return the folder that files fixed by the metadata fixer should be
	 *         written to.
	 */
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