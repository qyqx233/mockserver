package me.zh.design.Responsibility;

import java.io.File;
import java.nio.file.Paths;

public class Main {
    private static AbstractLogger getChainOfLoggers() {
        AbstractLogger errorLogger = new ErrorLogger(AbstractLogger.ERROR);
        AbstractLogger fileLogger = new FileLogger(AbstractLogger.DEBUG);
        AbstractLogger consoleLogger = new ConsoleLogger(AbstractLogger.INFO);
        errorLogger.setNextLogger(fileLogger);
        fileLogger.setNextLogger(consoleLogger);
        return errorLogger;
    }

    public static void main(String[] args) {
        // 责任链遍历，判断是否满足条件
        File f = Paths.get("d:/", "a", "b").toFile();
        System.out.println(f.mkdirs());
        System.out.println(f.toString());

        AbstractLogger loggerChain = getChainOfLoggers();
        loggerChain.logMessage(AbstractLogger.INFO, "this is an information");
        loggerChain.logMessage(AbstractLogger.DEBUG, "this is an debug");
    }
}
