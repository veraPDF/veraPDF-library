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
package org.verapdf.pdfa.validation.profiles;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.verapdf.pdfa.flavours.PDFAFlavour;

/**
 * A ProfileDirectory provides access to a set of {@link ValidationProfile}s
 * that can be retrieved by String id or {@link PDFAFlavour}.
 * <p>
 * This interface provides a simple directory of {@link ValidationProfile}s that is intentionally restricted by the enum type {@link PDFAFlavour}.
 * </p>
 * 
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 */
public interface ProfileDirectory {
    /**
     * @return the Set of ValidationProfile String identifiers for the profiles
     *         held in the directory.
     */
    public Set<String> getValidationProfileIds();

    /**
     * @return the Set of {@link PDFAFlavour} enum instances that identify the
     *         profiles held in the directory.
     */
    public Set<PDFAFlavour> getPDFAFlavours();

    /**
     * @param profileID
     *            a two character String that uniquely identifies a particular
     *            {@link PDFAFlavour}, e.g. 1a, 1b, 2a, etc.
     * @return the {@link ValidationProfile} associated with the profileId
     * @throws NoSuchElementException
     *             when there is no profile associated with the profileID string
     *             IllegalArgumentException if the profileID parameter is null
     * @throws IllegalArgumentException
     *             if profileID is null
     */
    public ValidationProfile getValidationProfileById(String profileID);

    /**
     * @param flavour
     *            a {@link PDFAFlavour} instance that identifies a
     *            {@link ValidationProfile}
     * @return the {@link ValidationProfile} associated with the flavour
     * @throws NoSuchElementException
     *             when there is no profile associated with the flavour
     *             IllegalArgumentException if the flavour parameter is null
     * @throws IllegalArgumentException
     *             if flavour is null
     */
    public ValidationProfile getValidationProfileByFlavour(PDFAFlavour flavour);
    
    public List<ValidationProfile> getValidationProfilesByFlavours(List<PDFAFlavour> flavours);

    /**
     * @return the full set of {@link ValidationProfile}s held in the directory.
     */
    public Set<ValidationProfile> getValidationProfiles();
}
