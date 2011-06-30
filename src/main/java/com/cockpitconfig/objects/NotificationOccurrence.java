package com.cockpitconfig.objects;

import net.sf.json.JSONObject;

public class NotificationOccurrence {

	private int id;
	private String dateOccur;
	private String type;
	private String description;
	private String timeOccur;
	private int assertionConditionID;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDateOccur() {
		return dateOccur;
	}

	public void setDateOccur(String dateOccur) {
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

	public String getTimeOccur() {
		return timeOccur;
	}

	public void setTimeOccur(String timeOccur) {
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
		// DateFormat formatter;
		// formatter = new SimpleDateFormat("yyyy-MM-dd");
		// String s = formatter.format(this.dateOccur);
		json.put("Date", this.dateOccur);
		json.put("Type", this.type);
		json.put("Description", this.description);
		return json;
	}
}
