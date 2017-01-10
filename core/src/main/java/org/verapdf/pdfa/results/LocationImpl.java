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
package org.verapdf.pdfa.results;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author <a href="mailto:carl@openpreservation.org">Carl Wilson</a>
 *
 */
@XmlRootElement(name = "location")
final class LocationImpl implements Location {
    private static final LocationImpl DEFAULT = new LocationImpl();
    private static final String DEREF_REGEX = "\\([0-9]{1,} ([a-zA-Z]{4})";
    private static final String DEREF_REPL = "\\(";
    private static final Pattern DEREF_PATTERN = Pattern.compile(DEREF_REGEX);
    @XmlElement
    private final String level;
    @XmlElement
    private final String context;

    private LocationImpl() {
        this("level", "context");
    }

    /**
     * @param level
     */
    private LocationImpl(final String level, final String context) {
        super();
        this.level = level;
        this.context = context;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getLevel() {
        return this.level;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public String getContext() {
        return this.context;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.context == null) ? 0 : this.context.hashCode());
        result = prime * result
                + ((this.level == null) ? 0 : this.level.hashCode());
        return result;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public final boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Location))
            return false;
        Location other = (Location) obj;
        if (this.context == null) {
            if (other.getContext() != null)
                return false;
        } else if (!this.context.equals(other.getContext()))
            return false;
        if (this.level == null) {
            if (other.getLevel() != null)
                return false;
        } else if (!this.level.equals(other.getLevel()))
            return false;
        return true;
    }

    /**
     * { @inheritDoc }
     */
    @Override
    public final String toString() {
        return "Location [level=" + this.level + ", context=" + this.context
                + "]";
    }

    static LocationImpl defaultInstance() {
        return DEFAULT;
    }

    static LocationImpl fromValues(final String level, final String context) {
        String deRefdContext = context;
        Matcher matcher = DEREF_PATTERN.matcher(context);
        if (matcher.find())
            deRefdContext = matcher.replaceAll(DEREF_REPL + matcher.group(1));
        return new LocationImpl(level, deRefdContext);
    }

    static class Adapter extends XmlAdapter<LocationImpl, Location> {
        @Override
        public Location unmarshal(LocationImpl locationImpl) {
            return locationImpl;
        }

        @Override
        public LocationImpl marshal(Location location) {
            return (LocationImpl) location;
        }
    }

}
