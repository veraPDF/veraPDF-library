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
package org.verapdf.component;

import org.verapdf.core.utils.LogsFileHandler;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@XmlRootElement(name = "logs")
public final class LogsSummaryImpl implements LogsSummary {
    private static final Logger logger = Logger.getLogger(LogsSummaryImpl.class.getCanonicalName());
    private static final String logBeginning = "[log]";
    @XmlAttribute
    private final int logsCount;
    @XmlElement(name = "logMessage")
    private final Set<Log> logs;

    private LogsSummaryImpl() {
        this(0, Collections.emptySet());
    }

    private LogsSummaryImpl(int logsCount, Set<Log> logs) {
        this.logsCount = logsCount;
        this.logs = logs;
    }

    @Override
    public int getLogsCount() {
        return logsCount;
    }

    @Override
    public Set<Log> getLogs() {
        return logs;
    }

    static class Adapter extends XmlAdapter<LogsSummaryImpl, LogsSummary> {
        @Override
        public LogsSummary unmarshal(LogsSummaryImpl logs) {
            return logs;
        }

        @Override
        public LogsSummaryImpl marshal(LogsSummary logs) {
            return (LogsSummaryImpl) logs;
        }
    }

    public static LogsSummary getSummary() {
        List<String> logs = new ArrayList<>();
        try {
            List<String> loggerMessages = Files.readAllLines(Paths.get(LogsFileHandler.getLogFile()));
            int size = loggerMessages.size() - 1;
            for (int i = 0; i < size; ++i) {
                if (loggerMessages.get(i).contains(logBeginning)) {
                    ++i; //skip source class and method names
                    // get message till next log or EOF
                    StringBuilder log = new StringBuilder(loggerMessages.get(++i));
                    while (i < size && !loggerMessages.get(i + 1).contains(logBeginning)) {
                        log.append('\n').append(loggerMessages.get(++i));
                    }
                    logs.add(log.toString());
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to get logger messages from file. Warnings report will be empty");
        }
        int logsCount = logs.size();
        if (logsCount == 0) {
            return new LogsSummaryImpl();
        }
        Map<String, Long> occurrencesMap = logs.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Set<Log> logsSet = new HashSet<>();
        for (Map.Entry<String, Long> log : occurrencesMap.entrySet()) {
            String[] message = log.getKey().split(": ", 2);
            logsSet.add(LogImpl.fromValues(log.getValue().intValue(), message[0], message[1]));
        }
        return new LogsSummaryImpl(logsCount, logsSet);
    }
}
