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
package org.verapdf.core.utils;

import java.lang.management.ManagementFactory;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogsFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        String format = "[log]%n%2$s %3$s in process %1$d%n%4$s: %5$s%n";
        long pid = Long.parseLong(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
        return String.format(format,
                pid,
                record.getSourceClassName(),
                record.getSourceMethodName(),
                record.getLevel().getName(),
                record.getMessage());
    }
}
