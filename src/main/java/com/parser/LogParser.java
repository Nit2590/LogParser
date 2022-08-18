package com.parser;

import com.parser.event.EventLog;
import com.parser.event.EventLogLine;
import com.parser.event.EventOpr;
import com.parser.utility.Logging;
import com.parser.utility.PropertyReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* Parser Intitiate class, which calls other bussiness functions
 */
public class LogParser {

    public static Logging log = new Logging("LogParser");


    private static final String DEFAULT_FILE_PATH =PropertyReader.getFieldValue("DEFAULT_FILE_PATH") ;

    private String EventFilePath;

    public LogParser(String inputPath) {
        this.EventFilePath = inputPath;
    }



    public static void main(String[] args) {
        EventOpr eventOpr=new EventOpr();
        Map<String, List<EventLogLine>> eventMap = new HashMap<>();
        List<EventLog> logEvent=new ArrayList<>();

        //Read input file from cmd
        if (args.length ==1 ) {
            log.info("Calling log parser operation on input file "+ args[0]);
            eventMap=eventOpr.parseEventsFromFile(args[0]);
        } else //read data from default file
        {
            log.info( "Calling log parser operation on default file "+DEFAULT_FILE_PATH);
           eventMap=eventOpr.parseEventsFromFile(LogParser.DEFAULT_FILE_PATH);
        }

        if (eventMap.size()>0)
        {
            logEvent=eventOpr.filterEvents(eventMap);

            if(logEvent.size()>0)
            {
                eventOpr.addEventsToDB(logEvent);
            }else{
                log.info("No events to add into DB");
            }
        }else{
            log.info("No Events to read from specified file, please check the file/path");
        }


    }

}
