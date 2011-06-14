package com.cockpitconfig.objects;

import java.util.Calendar;
import java.util.Date;

import net.sf.json.JSONObject;

public class NotificationOccurrence {

	private int id;
	private Date dateOccur;
	private String type;
	private String description;
	private Calendar timeOccur;
	private int assertionConditionID;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateOccur() {
		return dateOccur;
	}

	public void setDateOccur(Date dateOccur) {
		this.dateOccur = dateOccur;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Calendar getTimeOccur() {
		return timeOccur;
	}

	public void setTimeOccur(Calendar timeOccur) {
		this.timeOccur = timeOccur;
	}

	public int getAssertionConditionID() {
		return assertionConditionID;
	}

	public void setAssertionConditionID(int assertionConditionID) {
		this.assertionConditionID = assertionConditionID;
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("Date", this.dateOccur.toString());
		json.put("Type", this.type);
		json.put("Description", this.description);
		return json;
	}

}
