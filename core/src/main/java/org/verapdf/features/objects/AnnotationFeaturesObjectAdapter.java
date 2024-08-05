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
package org.verapdf.features.objects;

import java.util.Set;

/**
 * @author Maksim Bezrukov
 */
public interface AnnotationFeaturesObjectAdapter extends FeaturesObjectAdapter {
	String getId();
	String getPopupId();
	Set<String> getFormXObjectsResources();
	String getSubtype();
	double[] getRectangle();
	String getContents();
	String getAnnotationName();
	String getModifiedDate();
	double[] getColor();
	boolean isInvisible();
	boolean isHidden();
	boolean isPrinted();
	boolean isNoZoom();
	boolean isNoRotate();
	boolean isNoView();
	boolean isReadOnly();
	boolean isLocked();
	boolean isToggleNoView();
	boolean isLockedContents();
}
