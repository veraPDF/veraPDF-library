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
/**
 * 
 */
package org.verapdf.pdfa.validation.profiles;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * Convenience gathering of a set of properties that help identify and describe
 * a {@link ValidationProfile}.
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlJavaTypeAdapter(ProfileDetailsImpl.Adapter.class)
public interface ProfileDetails {

    /**
     * @return a human interpretable name for this ValidationProfile
     */
    public abstract String getName();

    /**
     * @return a brief textual description of this ValidationProfile
     */
    public abstract String getDescription();

    /**
     * @return a String that identifies the creator of this ValidationProfile
     */
    public abstract String getCreator();

    /**
     * @return the Date that this ValidationProfile was created
     */
    public abstract Date getDateCreated();

}