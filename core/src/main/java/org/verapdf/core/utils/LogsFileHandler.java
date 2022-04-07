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

    public static void configLogs() throws IOException {
        InputStream loggersConfig = BatchFileProcessor.class.getClassLoader().getResourceAsStream("org/verapdf/processor/logging.properties");
        LogManager.getLogManager().readConfiguration(loggersConfig);
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
