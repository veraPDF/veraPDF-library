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

import java.util.EnumSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.verapdf.features.FeatureExtractorConfig;
import org.verapdf.features.FeatureFactory;
import org.verapdf.metadata.fixer.FixerFactory;
import org.verapdf.metadata.fixer.MetadataFixerConfig;
import org.verapdf.pdfa.validation.profiles.Profiles;
import org.verapdf.pdfa.validation.profiles.ValidationProfile;
import org.verapdf.pdfa.validation.validators.ValidatorConfig;
import org.verapdf.pdfa.validation.validators.ValidatorFactory;

/**
 * @author Maksim Bezrukov
 */

@XmlRootElement(name = "processorConfig")
final class ProcessorConfigImpl implements ProcessorConfig {
	private static final String defaultMdFolder = ".";
	private static final ProcessorConfig defaultInstance = new ProcessorConfigImpl();
	@XmlElement
	private final EnumSet<TaskType> tasks;
	@XmlElement
	private final ValidatorConfig validatorConfig;
	@XmlElement
	private final FeatureExtractorConfig featureConfig;
	@XmlElement
	private final MetadataFixerConfig fixerConfig;
	@XmlElement
	private final ValidationProfile customProfile;
	@XmlAttribute
	private final String mdFolder;

	private ProcessorConfigImpl() {
		this(ValidatorFactory.defaultConfig(), FeatureFactory.defaultConfig(), FixerFactory.defaultConfig(),
				EnumSet.noneOf(TaskType.class));
	}

	private ProcessorConfigImpl(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final MetadataFixerConfig fixerConfig, final EnumSet<TaskType> tasks) {
		this(config, featureConfig, fixerConfig, tasks, Profiles.defaultProfile());
	}

	private ProcessorConfigImpl(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final MetadataFixerConfig fixerConfig, final EnumSet<TaskType> tasks, final String mdFolder) {
		this(config, featureConfig, fixerConfig, tasks, Profiles.defaultProfile(), mdFolder);
	}

	private ProcessorConfigImpl(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final MetadataFixerConfig fixerConfig, final EnumSet<TaskType> tasks,
			final ValidationProfile customProfile) {
		this(config, featureConfig, fixerConfig, tasks, customProfile, defaultMdFolder);
	}

	private ProcessorConfigImpl(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final MetadataFixerConfig fixerConfig, final EnumSet<TaskType> tasks, final ValidationProfile customProfile,
			final String mdFolder) {
		super();
		this.tasks = EnumSet.copyOf(tasks);
		this.validatorConfig = config;
		this.featureConfig = featureConfig;
		this.fixerConfig = fixerConfig;
		this.customProfile = customProfile;
		this.mdFolder = mdFolder;
	}

	public boolean isFixMetadata() {
		return (this.tasks.contains(TaskType.FIX_METADATA));
	}

	@Override
	public ValidatorConfig getValidatorConfig() {
		return this.validatorConfig;
	}

	@Override
	public FeatureExtractorConfig getFeatureConfig() {
		return this.featureConfig;
	}

	@Override
	public MetadataFixerConfig getFixerConfig() {
		return this.fixerConfig;
	}

	@Override
	public EnumSet<TaskType> getTasks() {
		return EnumSet.copyOf(this.tasks);
	}

	@Override
	public boolean hasTask(TaskType toCheck) {
		return this.tasks.contains(toCheck);
	}

	@Override
	public ValidationProfile getCustomProfile() {
		return this.customProfile;
	}

	@Override
	public boolean hasCustomProfile() {
		return this.customProfile.equals(Profiles.defaultProfile());
	}

	@Override
	public String getMetadataFolder() {
		return this.mdFolder;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.featureConfig == null) ? 0 : this.featureConfig.hashCode());
		result = prime * result + ((this.fixerConfig == null) ? 0 : this.fixerConfig.hashCode());
		result = prime * result + ((this.tasks == null) ? 0 : this.tasks.hashCode());
		result = prime * result + ((this.validatorConfig == null) ? 0 : this.validatorConfig.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ProcessorConfigImpl)) {
			return false;
		}
		ProcessorConfigImpl other = (ProcessorConfigImpl) obj;
		if (this.featureConfig == null) {
			if (other.featureConfig != null) {
				return false;
			}
		} else if (!this.featureConfig.equals(other.featureConfig)) {
			return false;
		}
		if (this.fixerConfig == null) {
			if (other.fixerConfig != null) {
				return false;
			}
		} else if (!this.fixerConfig.equals(other.fixerConfig)) {
			return false;
		}
		if (this.tasks == null) {
			if (other.tasks != null) {
				return false;
			}
		} else if (!this.tasks.equals(other.tasks)) {
			return false;
		}
		if (this.validatorConfig == null) {
			if (other.validatorConfig != null) {
				return false;
			}
		} else if (!this.validatorConfig.equals(other.validatorConfig)) {
			return false;
		}
		return true;
	}

	static ProcessorConfig defaultInstance() {
		return defaultInstance;
	}

	static ProcessorConfig fromValues(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final MetadataFixerConfig fixerConfig, final EnumSet<TaskType> tasks) {
		return new ProcessorConfigImpl(config, featureConfig, fixerConfig, tasks);
	}

	static ProcessorConfig fromValues(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final MetadataFixerConfig fixerConfig, final EnumSet<TaskType> tasks, final String mdFolder) {
		return new ProcessorConfigImpl(config, featureConfig, fixerConfig, tasks, mdFolder);
	}

	static ProcessorConfig fromValues(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final MetadataFixerConfig fixerConfig, final EnumSet<TaskType> tasks, final ValidationProfile profile) {
		return new ProcessorConfigImpl(config, featureConfig, fixerConfig, tasks, profile);
	}

	static ProcessorConfig fromValues(final ValidatorConfig config, final FeatureExtractorConfig featureConfig,
			final MetadataFixerConfig fixerConfig, final EnumSet<TaskType> tasks, final ValidationProfile profile,
			final String mdFolder) {
		return new ProcessorConfigImpl(config, featureConfig, fixerConfig, tasks, profile, mdFolder);
	}
}
