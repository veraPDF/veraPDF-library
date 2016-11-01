package org.verapdf.features;

import java.util.EnumSet;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(FeatureExtractorConfigImpl.Adapter.class)

public interface FeatureExtractorConfig {

	boolean isFeatureEnabled(FeatureObjectType type);

	boolean isAnyFeatureEnabled(EnumSet<FeatureObjectType> types);

	EnumSet<FeatureObjectType> getEnabledFeatures();

}