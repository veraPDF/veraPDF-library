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
package org.verapdf.core.utils;

import org.verapdf.processor.BatchFileProcessor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

public class LogsFileHandler extends FileHandler {
    private static final Logger LOGGER = Logger.getLogger(LogsFileHandler.class.getCanonicalName());

    public static final String SUFFIX = LogManager.getLogManager().getProperty(LogsFileHandler.class.getName() + ".pattern") == null ?
            ".log" : LogManager.getLogManager().getProperty(LogsFileHandler.class.getName() + ".pattern");
    private static final List<String> logFilePaths = new ArrayList<>();

    public LogsFileHandler() throws IOException, SecurityException {
        this(SUFFIX);
    }

    public LogsFileHandler(String pattern) throws IOException, SecurityException {
        super(getLogFile());
    }

    public LogsFileHandler(String pattern, boolean append) throws IOException, SecurityException {
        super(getLogFile(), append);
    }

    public LogsFileHandler(String pattern, int limit, int count) throws IOException, SecurityException {
        super(getLogFile(), limit, count);
    }

    public LogsFileHandler(String pattern, int limit, int count, boolean append) throws IOException, SecurityException {
        super(getLogFile(), limit, count, append);
    }

    public static long getPID() {
        return Long.parseLong(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
    }

    public static String getLogFile() throws IOException {
        long pid = getPID();
        for (String filePath : logFilePaths) {
            if (filePath.contains("logs" + pid)) {
                return filePath;
            }
        }
        logFilePaths.add(Files.createTempFile("logs" + getPID(), SUFFIX).toString());
        return logFilePaths.get(logFilePaths.size() - 1);
    }

    public static void configLogs() {
        InputStream loggersConfig = BatchFileProcessor.class.getClassLoader().getResourceAsStream("org/verapdf/processor/logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(loggersConfig);
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "Logging is not configured (console output only)");
        }
    }

    public static void createNewLogFile() throws IOException {
        File logs = new File(getLogFile());
        if (!logs.exists()) {
            logs.createNewFile();
            logs.deleteOnExit();
        } else {
            //clear existing file
            try (PrintWriter writer = new PrintWriter(logs)) {
                writer.print("");
            }
        }
    }

    public static void setLoggingLevel(Level level) {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        rootLogger.setLevel(level);
        for (Handler handler : handlers) {
            handler.setLevel(level);
        }
    }
}
