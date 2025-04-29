/**
 * This file is part of veraPDF Library core, a module of the veraPDF project.
 * Copyright (c) 2015-2025, veraPDF Consortium <info@verapdf.org>
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

	/**
	 * Gets annotation adapter id.
	 *
	 * @return an annotation adapter id
	 */
	String getId();

	/**
	 * Gets annotation adapter popup id.
	 *
	 * @return an annotation adapter popup id
	 */
	String getPopupId();

	/**
	 * Gets form XObject from annotation.
	 *
	 * @return a form XObject
	 */
	Set<String> getFormXObjectsResources();

	/**
	 * Gets annotation subtype.
	 *
	 * @return an annotation subtype
	 */
	String getSubtype();

	/**
	 * Gets annotation rectangle.
	 *
	 * @return an annotation rectangle
	 */
	double[] getRectangle();

	/**
	 * Gets annotation contents.
	 *
	 * @return an annotation contents
	 */
	String getContents();

	/**
	 * Gets annotation name.
	 *
	 * @return an annotation name
	 */
	String getAnnotationName();

	/**
	 * Gets annotation modified date.
	 *
	 * @return an annotation modified date
	 */
	String getModifiedDate();

	/**
	 * Gets annotation color.
	 *
	 * @return an annotation color
	 */
	double[] getColor();

	/**
	 * Tells whether annotation is invisible.
	 *
	 * @return true if annotation is invisible
	 */
	boolean isInvisible();

	/**
	 * Tells whether annotation is hidden.
	 *
	 * @return true if annotation is hidden
	 */
	boolean isHidden();

	/**
	 * Tells whether annotation is printed.
	 *
	 * @return true if annotation is printed
	 */
	boolean isPrinted();

	/**
	 * Tells whether annotation is no zoom.
	 *
	 * @return true if annotation is no zoom
	 */
	boolean isNoZoom();

	/**
	 * Tells whether annotation is no rotate.
	 *
	 * @return true if annotation is no rotate
	 */
	boolean isNoRotate();

	/**
	 * Tells whether annotation is no view.
	 *
	 * @return true if annotation is no view
	 */
	boolean isNoView();

	/**
	 * Tells whether annotation is read only.
	 *
	 * @return true if annotation is read only
	 */
	boolean isReadOnly();

	/**
	 * Tells whether annotation is locked.
	 *
	 * @return true if annotation is locked
	 */
	boolean isLocked();

	/**
	 * Tells whether annotation is toggle no view.
	 *
	 * @return true if annotation is toggle no view
	 */
	boolean isToggleNoView();

	/**
	 * Tells whether annotation is locked content.
	 *
	 * @return true if annotation is locked content
	 */
	boolean isLockedContents();
}
