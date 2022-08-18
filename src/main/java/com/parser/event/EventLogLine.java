package com.parser.event;


import com.parser.utility.Logging;
import org.json.simple.JSONObject;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

/**
 * This class represents a line from the input log file
 */



public class EventLogLine {

	static Logging log=new Logging("EventLogLine");

    enum EventStatus{
    	STARTED,
		FINISHED,
		UNKNOWNSTATE;

		public static EventStatus getStatus(String status) {
			try {
				switch (status.toUpperCase()) {
					case "STARTED":
						return STARTED;
					case "FINISHED":
						return FINISHED;
					default:
						return UNKNOWNSTATE;
				}
			} catch (Exception e) {
				log.error("Invalid Event Status in log file as ->" + status);
				return UNKNOWNSTATE;
			}
		}
	}

	private String id;
    private EventStatus state;
	private long timestamp;
	private String type;
	private String host;
	
	
	public EventLogLine(JSONObject jsonObject) {
		try {
			this.id = (String) jsonObject.get("id");
			this.state = EventStatus.getStatus((String) jsonObject.get("state"));
			this.timestamp = (long) jsonObject.get("timestamp");
			this.type = (String) jsonObject.get("type");
			this.host = (String) jsonObject.get("host");
		}catch (Exception e)
		{
			log.error("Unable to map values to Pojo Fields, getting exception "+ e.getMessage());
		}
	}


	@Override
	public String toString() {
		return "EventLogLine{" +
				"id='" + id + '\'' +
				", state=" + state +
				", timestamp=" + timestamp +
				", type='" + type + '\'' +
				", host='" + host + '\'' +
				'}';
	}

	public String getId() {
		return id;
	}


	public EventStatus getState() {
		return state;
	}


	public long getTimestamp() {
		return timestamp;
	}


	public String getType() {
		return type;
	}


	public String getHost() {
		return host;
	}
}
