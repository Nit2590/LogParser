package com.parser.event;

/**
 * This class represents an event, a phase of  "started" and "finished"
 *
 */
public class EventLog {

	private String id;
	private long duration;
	private String type="";
	private String host;
	private boolean alert;



	public EventLog(String id, long duration, String type, String host, boolean alert) {
		this.id = id;
		this.duration = duration;
		this.type = type;
		this.host = host;
		this.alert=alert;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean getAlert() {
		return alert;
	}

	public void setAlert(boolean alert) {
		this.alert = alert;
	}
}
