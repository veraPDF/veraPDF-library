package org.verapdf.component;

import org.verapdf.core.utils.LogsFileHandler;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
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
