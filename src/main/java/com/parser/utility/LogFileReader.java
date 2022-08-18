package com.parser.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.parser.event.EventLogLine;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public final class LogFileReader {

	Logging log=new Logging("LogFileReader");

	 // Reads a log event files and return a map of events grouped by id

	public Map<String, List<EventLogLine>> readEventsFromFile(String filePath) {
		Map<String, List<EventLogLine>> eventMap = new HashMap<>();
		JSONParser parser = new JSONParser();
		try  {
			File file=new File(filePath);
			Reader is = new FileReader(file);
	        BufferedReader bufferedReader = new BufferedReader(is);
	        
	        String currentLine;
	        while((currentLine=bufferedReader.readLine()) != null) {
	            JSONObject logLine = (JSONObject) parser.parse(currentLine);
	            EventLogLine event = new EventLogLine(logLine);
	            String id = event.getId();
	            if (eventMap.containsKey(id)) {
	            	eventMap.get(id).add(event);
	            } else {
	            	List<EventLogLine> eventList = new ArrayList<>();
	            	eventList.add(event);
	            	eventMap.put(id, eventList);
	            }
	        }
	        log.info("Successfully readed "+eventMap.size() +" distinct events from the file ");
		} catch (IOException | ParseException e) {
			log.fatal(e.getMessage());
			log.error("Unable to read file data, either the path or file is not correct");
		} 

		return eventMap;
	}
}
