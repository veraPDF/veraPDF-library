/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2024, veraPDF Consortium <info@verapdf.org>
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
/**
 * 
 */
package org.verapdf.processor.app;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import javax.xml.bind.JAXBException;
import org.verapdf.features.FeatureExtractorConfig;
import org.verapdf.metadata.fixer.MetadataFixerConfig;
import org.verapdf.pdfa.validation.validators.ValidatorConfig;
import org.verapdf.processor.ProcessorConfig;
import org.verapdf.processor.TaskType;
import org.verapdf.processor.plugins.PluginsCollectionConfig;

/**
 * @author  <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *          <a href="https://github.com/carlwilson">carlwilson AT github</a>
 *
 * @version 0.1
 * 
 * Created 31 Oct 2016:09:12:43
 */

public interface ConfigManager {
	public ValidatorConfig getValidatorConfig();
	public FeatureExtractorConfig getFeaturesConfig();
	public PluginsCollectionConfig getPluginsCollectionConfig();
	public MetadataFixerConfig getFixerConfig();
	public ProcessorConfig createProcessorConfig();
	public ProcessorConfig createProcessorConfig(EnumSet<TaskType> tasks);
	public ProcessorConfig createProcessorConfig(EnumSet<TaskType> tasks, String mdFolder);
	public VeraAppConfig getApplicationConfig();
	public File getConfigDir();
	public void updateValidatorConfig(ValidatorConfig config) throws JAXBException, IOException;
	public void updateFeaturesConfig(FeatureExtractorConfig config) throws JAXBException, IOException;
	public void updateFixerConfig(MetadataFixerConfig config) throws JAXBException, IOException;
	public void updateAppConfig(VeraAppConfig config) throws JAXBException, IOException;
}
