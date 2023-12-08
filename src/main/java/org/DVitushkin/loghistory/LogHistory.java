package org.DVitushkin.loghistory;

import org.DVitushkin.customexception.MachineException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LogHistory {
    private final Logger logger;

    public LogHistory() {
        this.logger = LogManager.getLogger();
    }

    public void sendErrResponse(Exception errMsg) {
        System.out.printf("Sorry but something was wrong:\n %s", errMsg.getMessage());
        this.logger.error(errMsg.getMessage());
    }

    public void saveLog(LogLvls lvl, String msg) {
        System.out.printf("[%s] %s\n", lvl, msg);
        switch (lvl) {
            case INFO:
                this.logger.info(msg);
                break;
            case DEBUG:
                this.logger.debug(msg);
                break;
            case WARN:
                this.logger.warn(msg);
                break;
            case ERR:
                this.logger.error(msg);
        }
    }

    private static ArrayList<String> parseLogRecord(String ptrn, String input) {
        String date_pattern = "(?<timestamp>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.\\d{3})";
        String level_pattern = "(?<level>\\[\\w+\\] INFO|ERROR|WARN|TRACE|DEBUG|FATAL)";
        String class_pattern = "(?<class>[^\\]]+)";
        String text_pattern = String.format("(?<text>%s.*)", ptrn);

        Pattern pattern = Pattern.compile(date_pattern + " " + level_pattern + "  " + class_pattern + " - " + text_pattern);

        Matcher matcher = pattern.matcher(input);
        var result = new ArrayList<String>();

        while (matcher.find()) {
            result.add(String.format("<%s> <%s>\n", matcher.group("timestamp"), matcher.group("text")));
        }
        return result;
    }

    private static ArrayList<String> readLogfileByString(String filePath) throws MachineException {
        File file = new File(filePath);
        Scanner sc;

        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new MachineException("Log file not found!");
        }

        var result = new ArrayList<String>();
        while (sc.hasNextLine()) {
            result.add(sc.nextLine());
        }
        return result;
    }

    public void getHistoryByKeyWord(String kw) throws MachineException {
        ArrayList<String> logFileContent;
        try {
            logFileContent = readLogfileByString("./logs/app.log");
        } catch (MachineException e) {
            throw new MachineException(e.getMessage());
        }

        for (String log : logFileContent) {
            ArrayList<String> parseResult = parseLogRecord(kw, log);
            parseResult.forEach(System.out::println);
        }
    }
}
