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
package org.verapdf.component;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;

final class LogImpl implements Log {
    @XmlAttribute
    private final int occurrences;
    @XmlAttribute
    private final String level;
    @XmlValue
    private final String message;

    private LogImpl(final int occurrences, String level, final String message) {
        this.occurrences = occurrences;
        this.level = level;
        this.message = message;
    }

    @Override
    public int getOccurrences() {
        return occurrences;
    }

    @Override
    public String getLevel() {
        return level;
    }

    @Override
    public String getMessage() {
        return message;
    }

    static class Adapter extends XmlAdapter<LogImpl, Log> {
        @Override
        public Log unmarshal(LogImpl log) {
            return log;
        }

        @Override
        public LogImpl marshal(Log log) {
            return (LogImpl) log;
        }
    }

    static Log fromValues(final int occurrences, final String level, final String message) {
        if (occurrences <= 0) {
            throw new IllegalArgumentException("Argument occurrences can not be negative");
        }
        if (level == null) {
            throw new IllegalArgumentException("Argument level can not be null");
        }
        if (message == null) {
            throw new IllegalArgumentException("Argument message can not be null");
        }
        return new LogImpl(occurrences, level, message);
    }

}
