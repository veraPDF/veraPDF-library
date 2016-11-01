package org.verapdf.features;

import java.util.EnumSet;

public interface FeatureExtractorConfig {

	boolean isFeatureEnabled(FeatureObjectType type);

	boolean isAnyFeatureEnabled(EnumSet<FeatureObjectType> types);

	EnumSet<FeatureObjectType> getEnabledFeatures();

}