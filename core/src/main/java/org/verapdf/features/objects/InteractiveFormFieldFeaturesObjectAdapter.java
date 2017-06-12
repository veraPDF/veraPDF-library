package org.verapdf.features.objects;

import java.util.List;

/**
 * @author Maksim Bezrukov
 */
public interface InteractiveFormFieldFeaturesObjectAdapter extends FeaturesObjectAdapter {
	String getFullyQualifiedName();
	String getValue();
	List<InteractiveFormFieldFeaturesObjectAdapter> getChildren();
}
