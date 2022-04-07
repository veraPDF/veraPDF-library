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
