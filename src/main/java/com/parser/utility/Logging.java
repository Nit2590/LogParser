package com.parser.utility;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Logging {

    private static Logger log;

    public Logging(String logName) {
        if (log!=null)
        {
            //return old log object
        }
        else
        {
          //  PropertyConfigurator.configure("src/main/resources/log4j2.xml");
            log = LogManager.getLogger(logName);
        }
    }



    public void info(String message) {
        log.info(message);
    }

    public void info(Exception e) {
        log.info(e);
    }

    public void warn(String message) {
        log.warn(message);
    }

    public void warn(Exception e) {
        log.warn(e);
    }

    public void error(String message) {
        log.error(message);
    }

    public void error(Exception e) {
        log.error(e);
    }

    public void fatal(String message) {
        log.fatal(message);
    }

    public void fatal(Exception e) {
        log.fatal(e);
    }

    public void debug(String message) {
        log.debug(message);
    }

    public void debug(Exception e) {
        log.debug(e);
    }

    public void trace(String message) {
        log.trace(message);
    }

    public void trace(Exception e) {
        log.trace(e);
    }

    public static Logger getLog() {
        return log;
    }
}
