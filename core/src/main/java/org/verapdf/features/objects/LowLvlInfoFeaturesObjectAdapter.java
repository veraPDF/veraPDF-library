package org.verapdf.features.objects;

import java.util.Set;

/**
 * @author Maksim Bezrukov
 */
public interface LowLvlInfoFeaturesObjectAdapter extends FeaturesObjectAdapter {

	int getIndirectObjectsNumber();

	String getCreationId();

	String getModificationId();

	Set<String> getFilters();
}
