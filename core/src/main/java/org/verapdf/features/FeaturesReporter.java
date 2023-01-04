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
package org.verapdf.features;

import org.verapdf.core.FeatureParsingException;
import org.verapdf.features.objects.FeaturesObject;
import org.verapdf.features.tools.FeatureTreeNode;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Features reporter
 *
 * @author Maksim Bezrukov
 */
public class FeaturesReporter {

	private static final Logger LOGGER = Logger.getLogger(FeaturesReporter.class.getCanonicalName());

	public static final String CUSTOM_FEATURES_NAME = "customFeatures";
	public static final String PLUGIN_FEATURES_NAME = "pluginFeatures";

	private final Map<FeatureObjectType, List<AbstractFeaturesExtractor>> featuresExtractors = new HashMap<>();

	private final FeatureExtractionResult collection;
	private final FeatureExtractorConfig config;

	/**
	 * Creates new FeaturesReporter
	 */
	public FeaturesReporter(FeatureExtractorConfig config, List<AbstractFeaturesExtractor> extractors) {
		if (extractors == null) {
			throw new IllegalArgumentException(nullMessage("extractors"));
		}
		if (config == null) {
			throw new IllegalArgumentException(nullMessage("config"));
		}
		this.config = config;
		this.collection = new FeatureExtractionResult();
		for (AbstractFeaturesExtractor extractor : extractors) {
			registerFeaturesExtractor(extractor);
		}
	}

	public FeaturesReporter(FeatureExtractorConfig config) {
		this(config, Collections.<AbstractFeaturesExtractor>emptyList());
	}

	/**
	 * Registers feature extractor for features object type
	 *
	 * @param extractor object for extract custom features
	 */
	private void registerFeaturesExtractor(AbstractFeaturesExtractor extractor) {
		if (featuresExtractors.get(extractor.getType()) == null) {
			featuresExtractors.put(extractor.getType(), new ArrayList<AbstractFeaturesExtractor>());
		}

		featuresExtractors.get(extractor.getType()).add(extractor);
	}

	/**
	 * Reports feature object for feature report
	 *
	 * @param obj object for reporting
	 */
	public void report(FeaturesObject obj) {
		if (!config.isFeatureEnabled(obj.getType()))
			return;

		try {
			FeatureTreeNode root = obj.reportFeatures(this.collection);
			if (root != null && featuresExtractors.containsKey(obj.getType())) {
				FeatureTreeNode custom = root.addChild(CUSTOM_FEATURES_NAME);
				for (AbstractFeaturesExtractor ext : featuresExtractors.get(obj.getType())) {
					try {
						FeaturesData objData = obj.getData();
						if (objData != null) {
							List<FeatureTreeNode> cust = ext.getFeatures(objData);
							if (cust != null && !cust.isEmpty()) {
								FeatureTreeNode custRoot = custom.addChild(PLUGIN_FEATURES_NAME);
								AbstractFeaturesExtractor.ExtractorDetails details = ext.getDetails();
								String detailsName = details.getName();
								if (detailsName != null && !detailsName.isEmpty()) {
									custRoot.setAttribute("name", detailsName);
								}
								String detailsVersion = details.getVersion();
								if (detailsVersion != null && !detailsVersion.isEmpty()) {
									custRoot.setAttribute("version", detailsVersion);
								}
								String detailsDescription = details.getDescription();
								if (detailsDescription != null && !detailsDescription.isEmpty()) {
									custRoot.setAttribute("description", detailsDescription);
								}
								for (FeatureTreeNode ftn : cust) {
									if (ftn != null) {
										custRoot.addChild(ftn);
									}
								}
							}
							objData.close();
						}
					} catch (IOException e) {
						LOGGER.log(Level.FINE, "Exception in features data", e);
					}
				}
			}

		} catch (FeatureParsingException ignore) {
			// The method logic should ensure this never happens, so if it does
			// it's catastrophic. We'll throw an IllegalStateException with this
			// as a cause. The only time it's ignored is when the unthinkable
			// happens
			throw new IllegalStateException(
					"FeaturesReporter.report() illegal state.", ignore);
			// This exception occurs when wrong node creates for feature tree.
			// The logic of the method guarantees this doesn't occur.
		}
	}

	/**
	 * @return collection of featurereport
	 */
	public FeatureExtractionResult getCollection() {
		return this.collection;
	}

	private static String nullMessage(final String name) {
		return String.format("Argument %s cannot be null.", name);
	}

}
