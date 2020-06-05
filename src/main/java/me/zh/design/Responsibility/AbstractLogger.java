package me.zh.design.Responsibility;

//enum LoggerLevel {
//    INFO = 1,
//    DEBUG,
//    ERROR
//};


public abstract class AbstractLogger {
    public static int INFO = 1;
    public static int DEBUG = 2;
    public static int ERROR = 3;

    abstract protected void write(String message);

    protected AbstractLogger nextLogger;
    protected int level;

    public void setNextLogger(AbstractLogger nextLogger) {
        this.nextLogger = nextLogger;
    }

    public void logMessage(int level, String message) {
        if (this.level <= level) {
            write(message);
        }
        if (nextLogger != null) {
            nextLogger.logMessage(level, message);
        }
    }
}
