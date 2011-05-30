package com.cockpitconfig.objects;

public class AssertionGroup {

	private int id;
	private String constraintName;
	private int communicationID;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getConstraintName() {
		return constraintName;
	}

	public void setConstraintName(String constraintName) {
		this.constraintName = constraintName;
	}

	public int getCommunicationID() {
		return communicationID;
	}

	public void setCommunicationID(int communicationID) {
		this.communicationID = communicationID;
	}

}
