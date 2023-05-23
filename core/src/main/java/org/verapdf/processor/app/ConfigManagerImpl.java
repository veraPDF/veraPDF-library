/**
 * This file is part of VeraPDF Library GUI, a module of the veraPDF project.
 * Copyright (c) 2015, veraPDF Consortium <info@verapdf.org>
 * All rights reserved.
 *
 * VeraPDF Library GUI is free software: you can redistribute it and/or modify
 * it under the terms of either:
 *
 * The GNU General public license GPLv3+.
 * You should have received a copy of the GNU General Public License
 * along with VeraPDF Library GUI as the LICENSE.GPL file in the root of the source
 * tree.  If not, see http://www.gnu.org/licenses/ or
 * https://www.gnu.org/licenses/gpl-3.0.en.html.
 *
 * The Mozilla Public License MPLv2+.
 * You should have received a copy of the Mozilla Public License along with
 * VeraPDF Library GUI as the LICENSE.MPL file in the root of the source tree.
 * If a copy of the MPL was not distributed with this file, you can obtain one at
 * http://mozilla.org/MPL/2.0/.
 */
/**
 * 
 */
package org.verapdf.processor.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.verapdf.features.FeatureExtractorConfig;
import org.verapdf.features.FeatureFactory;
import org.verapdf.metadata.fixer.FixerFactory;
import org.verapdf.metadata.fixer.MetadataFixerConfig;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.validation.validators.ValidatorConfig;
import org.verapdf.pdfa.validation.validators.ValidatorFactory;
import org.verapdf.processor.ProcessorConfig;
import org.verapdf.processor.ProcessorFactory;
import org.verapdf.processor.TaskType;
import org.verapdf.processor.plugins.PluginsCollectionConfig;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *         <a href="https://github.com/carlwilson">carlwilson AT github</a>
 * @version 0.1 Created 31 Oct 2016:09:20:14
 */

public final class ConfigManagerImpl implements ConfigManager {

	private static final Logger LOGGER = Logger.getLogger(ConfigManagerImpl.class.getCanonicalName());

	private static final String nullArgMessage = "Arg tasks can not be null";
	private static final String defaultConfExt = ".xml"; //$NON-NLS-1$
	private static final String defaultValidName = "validator" + defaultConfExt; //$NON-NLS-1$
	private static final String defaultFixerName = "fixer" + defaultConfExt; //$NON-NLS-1$
	private static final String defaultFeaturesName = "features" + defaultConfExt; //$NON-NLS-1$
	private static final String defaultPluginsName = "plugins" + defaultConfExt; //$NON-NLS-1$
	private static final String defaultAppName = "app" + defaultConfExt; //$NON-NLS-1$
	private final File root;
	private final File validatorFile;
	private final File fixerFile;
	private final File featuresFile;
	private final File pluginsFile;
	private final File appFile;

	private ConfigManagerImpl(final File root) {
		this.root = root;
		this.validatorFile = getConfigFile(defaultValidName);
		this.fixerFile = getConfigFile(defaultFixerName);
		this.featuresFile = getConfigFile(defaultFeaturesName);
		this.pluginsFile = getConfigFile(defaultPluginsName);
		this.appFile = getConfigFile(defaultAppName);
		this.initialise();
	}

	/**
	 * @see ConfigManager#getValidatorConfig()
	 */
	@Override
	public ValidatorConfig getValidatorConfig() {
		try (InputStream fis = new FileInputStream(this.validatorFile)) {
			ValidatorConfig config = ValidatorFactory.createConfig(fis);
			if (config.getFlavour() == null) {
				config.setFlavour(PDFAFlavour.NO_FLAVOUR);
			}
			if (config.getDefaultFlavour() == null) {
				config.setDefaultFlavour(PDFAFlavour.NO_FLAVOUR);
			}
			return config;
		} catch (IOException | JAXBException excep) {
			LOGGER.log(Level.WARNING, "The validator config file " + validatorFile.getAbsolutePath() + " is missing or damaged");
			return ValidatorFactory.defaultConfig();
		}
	}

	/**
	 * @see ConfigManager#getFeaturesConfig()
	 */
	@Override
	public FeatureExtractorConfig getFeaturesConfig() {
		try (InputStream fis = new FileInputStream(this.featuresFile)) {
			return FeatureFactory.configFromXml(fis);
		} catch (IOException | JAXBException excep) {
			LOGGER.log(Level.WARNING, "The features config file " + featuresFile.getAbsolutePath() + " is missing or damaged");
			return FeatureFactory.defaultConfig();
		}
	}

	@Override
	public PluginsCollectionConfig getPluginsCollectionConfig() {
		try (InputStream fis = new FileInputStream(this.pluginsFile)) {
			return PluginsCollectionConfig.create(fis);
		} catch (IOException | JAXBException excep) {
			LOGGER.log(Level.WARNING, "The plugins config file " + pluginsFile.getAbsolutePath() + " is missing or damaged");
			return PluginsCollectionConfig.defaultConfig();
		}
	}

	/**
	 * @see ConfigManager#getFixerConfig()
	 */
	@Override
	public MetadataFixerConfig getFixerConfig() {
		try (InputStream fis = new FileInputStream(this.fixerFile)) {
			return FixerFactory.configFromXml(fis);
		} catch (IOException | JAXBException excep) {
			LOGGER.log(Level.WARNING, "The fixer config file " + fixerFile.getAbsolutePath() + " is missing or damaged");
			return FixerFactory.defaultConfig();
		}
	}

	/**
	 * @see ConfigManager#createProcessorConfig()
	 */
	@Override
	public ProcessorConfig createProcessorConfig() {
		VeraAppConfig applicationConfig = this.getApplicationConfig();
		return createProcessorConfig(applicationConfig.getProcessType().getTasks(), applicationConfig.getFixesFolder());
	}

	/**
	 * @see ConfigManager#createProcessorConfig(EnumSet< TaskType >)
	 */
	@Override
	public ProcessorConfig createProcessorConfig(EnumSet<TaskType> tasks) {
		if (tasks == null) throw new NullPointerException(nullArgMessage);
		return ProcessorFactory.fromValues(getValidatorConfig(), getFeaturesConfig(), getPluginsCollectionConfig(), getFixerConfig(), tasks);
	}

	/**
	 * @see ConfigManager#createProcessorConfig(EnumSet<TaskType>)
	 */
	@Override
	public ProcessorConfig createProcessorConfig(EnumSet<TaskType> tasks, String mdFolder) {
		if (tasks == null) throw new NullPointerException(nullArgMessage);
		return ProcessorFactory.fromValues(getValidatorConfig(), getFeaturesConfig(), getPluginsCollectionConfig(), getFixerConfig(), tasks, mdFolder);
	}

	/**
	 * @see ConfigManager#getApplicationConfig()
	 */
	@Override
	public VeraAppConfig getApplicationConfig() {
		try (InputStream fis = new FileInputStream(this.appFile)) {
			return VeraAppConfigImpl.fromXml(fis);
		} catch (IOException | JAXBException excep) {
			LOGGER.log(Level.WARNING, "The application config file " + appFile.getAbsolutePath() + " is missing or damaged");
			return VeraAppConfigImpl.defaultInstance();
		}
	}

	/**
	 * @see ConfigManager#getConfigDir()
	 */
	@Override
	public File getConfigDir() {
		return this.root;
	}

	/**
	 * @see ConfigManager#updateValidatorConfig(ValidatorConfig)
	 */
	@Override
	public void updateValidatorConfig(ValidatorConfig config) throws JAXBException, IOException {
		try (FileOutputStream fos = new FileOutputStream(this.validatorFile, false)) {
			ValidatorFactory.configToXml(config, fos);
		}
	}

	/**
	 * @see ConfigManager#updateFeaturesConfig(FeatureExtractorConfig)
	 */
	@Override
	public void updateFeaturesConfig(FeatureExtractorConfig config) throws JAXBException, IOException {
		try (FileOutputStream fos = new FileOutputStream(this.featuresFile, false)) {
			FeatureFactory.configToXml(config, fos);
		}
	}

	/**
	 * @see ConfigManager#updateFixerConfig(MetadataFixerConfig)
	 */
	@Override
	public void updateFixerConfig(MetadataFixerConfig config) throws JAXBException, IOException {
		try (FileOutputStream fos = new FileOutputStream(this.fixerFile, false)) {
			FixerFactory.configToXml(config, fos);
		}
	}

	@Override
	public void updateAppConfig(VeraAppConfig config) throws JAXBException, IOException {
		try (FileOutputStream fos = new FileOutputStream(this.appFile, false)) {
			VeraAppConfigImpl.toXml(config, fos, Boolean.TRUE);
		}
	}

	public static ConfigManagerImpl create(final File root) {
		return new ConfigManagerImpl(root);
	}

	private void initialise() {
		try {
			if ((!this.validatorFile.exists() && this.validatorFile.createNewFile())
					|| this.validatorFile.length() == 0) {
				try (OutputStream fos = new FileOutputStream(this.validatorFile, false)) {
					ValidatorFactory.configToXml(ValidatorFactory.defaultConfig(), fos);
				}
			}
			if ((!this.featuresFile.exists() && this.featuresFile.createNewFile()) || this.featuresFile.length() == 0) {
				try (OutputStream fos = new FileOutputStream(this.featuresFile, false)) {
					FeatureFactory.configToXml(FeatureFactory.defaultConfig(), fos);
				}
			}
			if ((!this.fixerFile.exists() && this.fixerFile.createNewFile()) || this.fixerFile.length() == 0) {
				try (OutputStream fos = new FileOutputStream(this.fixerFile, false)) {
					FixerFactory.configToXml(FixerFactory.defaultConfig(), fos);
				}
			}
			if ((!this.appFile.exists() && this.appFile.createNewFile()) || this.appFile.length() == 0) {
				try (OutputStream fos = new FileOutputStream(this.appFile, false)) {
					VeraAppConfigImpl.toXml(VeraAppConfigImpl.defaultInstance(), fos, Boolean.TRUE);
				}
			}
			if ((!this.pluginsFile.exists() && this.pluginsFile.createNewFile()) || this.pluginsFile.length() == 0) {
				try (OutputStream fos = new FileOutputStream(this.pluginsFile, false)) {
					PluginsCollectionConfig.configToXml(PluginsCollectionConfig.defaultConfig(), fos);
				}
			}
		} catch (IOException | JAXBException excep) {
			LOGGER.log(Level.WARNING, "One or several of config files are missing or damaged");
			throw new IllegalStateException("Couldn't setup config", excep);
		}
	}

	private File getConfigFile(final String name) {
		File config = new File(this.root, name);
		try {
			if (config.isDirectory() || !config.exists() && !config.createNewFile()) {
				throw new IllegalArgumentException(config.getAbsolutePath() + " must be a creatable or readable file");
			}
		} catch (IOException excep) {
			throw new IllegalArgumentException("IOException when creating: " + config.getAbsolutePath(), excep);
		}
		return config;
	}
}
