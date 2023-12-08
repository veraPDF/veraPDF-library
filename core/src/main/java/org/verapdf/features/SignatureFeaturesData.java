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

import java.io.InputStream;
import java.util.Calendar;

/**
 * @author Maksim Bezrukov
 */
public class SignatureFeaturesData extends FeaturesData {

    private final String filter;
    private final String subFilter;
    private final String name;
    private final Calendar signDate;
    private final String location;
    private final String reason;
    private final String contactInfo;

    public SignatureFeaturesData(InputStream stream, String filter, String subFilter, String name, Calendar signDate, String location, String reason, String contactInfo) {
        super(stream);
        this.filter = filter;
        this.subFilter = subFilter;
        this.name = name;
        this.signDate = signDate;
        this.location = location;
        this.reason = reason;
        this.contactInfo = contactInfo;
    }

    public static SignatureFeaturesData newInstance(InputStream stream, String filter, String subFilter, String name, Calendar signDate, String location, String reason, String contactInfo) {
        return new SignatureFeaturesData(stream, filter, subFilter, name, signDate, location, reason, contactInfo);
    }

    public String getFilter() {
        return filter;
    }

    public String getSubFilter() {
        return subFilter;
    }

    public String getName() {
        return name;
    }

    public Calendar getSignDate() {
        return signDate;
    }

    public String getLocation() {
        return location;
    }

    public String getReason() {
        return reason;
    }

    public String getContactInfo() {
        return contactInfo;
    }
}
