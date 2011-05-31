package com.cockpitconfig.objects;

import java.util.Calendar;
import java.util.Date;

public class NotificationOccurrence {

	private int id;
	private Date dateOccur;
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

}
