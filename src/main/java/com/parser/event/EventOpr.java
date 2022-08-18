package com.parser.event;

import com.parser.utility.DBOperations;
import com.parser.utility.LogFileReader;
import com.parser.utility.Logging;
import com.parser.utility.PropertyReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventOpr {
    static Logging log=new Logging("EventOpr");
    private static final int EVENT_THRESHOLD_MS = Integer.valueOf(PropertyReader.getFieldValue("EVENT_THRESHOLD_MS"));


   /*
    * Function to read data from file and add into Map
    */
    public  Map<String,List<EventLogLine>> parseEventsFromFile(String filePath) {
        Map<String, List<EventLogLine>> eventMap = new HashMap<>();
        LogFileReader reader = new LogFileReader();
        eventMap = reader.readEventsFromFile(filePath);

        if (eventMap.size() <= 0) {
            log.error("Failed to read data from file, Stopping Execution");
        }

        return eventMap;
    }


    /*
    *  Function to add events into DB table
     */

    public void addEventsToDB(List<EventLog> eventList){

        List<EventLog> listevents =eventList;

        DBOperations dbmanager = new DBOperations();
        //dropping table every time new input files is traversed
        dbmanager.dropHSQLDBLTable();

        // Creating the table
        dbmanager.createHSQLDBTable();
        dbmanager.insertEvents(eventList);
        log.info("Events in the table :\n"+dbmanager.readEvents());
        dbmanager.stopHSQLDB();

    }

    /**
     * Function to filter the events and add them into a list
     */
    public  List<EventLog> filterEvents(Map<String, List<EventLogLine>> eventMap) {

        List<EventLog> logEvent=new ArrayList<>();
        EventLog eventLog;
        //Browse the map and compute the duration between finished and started if possible:
        for (List<EventLogLine> eventLogList : eventMap.values()) {
            if (eventLogList.size() >= 2) {  //checking the existence of atleast one start-finish event

                EventLogLine startEvent = null;
                EventLogLine finishEvent = null;

                for (EventLogLine event : eventLogList) {
                    if (event.getState() == EventLogLine.EventStatus.STARTED) {
                        startEvent = event;
                    } else if (event.getState() == EventLogLine.EventStatus.FINISHED) {
                        finishEvent = event;
                    }
                }

                //In case both 'Started' and 'Finished' Events are present
                if (startEvent != null && finishEvent != null) {
                    if(finishEvent.getTimestamp()==0 || startEvent.getTimestamp()==0)
                    {
                        log.warn("Either the finish/start timestamp is 0/missing for event "+ startEvent.getId());
                        continue;
                    }
                    long duration = finishEvent.getTimestamp() - startEvent.getTimestamp();
                    if (duration >= EVENT_THRESHOLD_MS) {
                         eventLog = new EventLog(finishEvent.getId(), duration, finishEvent.getType(), finishEvent.getHost(),true);
                    }
                    else
                    {
                         eventLog = new EventLog(finishEvent.getId(), duration, finishEvent.getType(), finishEvent.getHost(),false);
                    }
                    logEvent.add(eventLog);
                }
            }
        }
        return logEvent;
    }

}


