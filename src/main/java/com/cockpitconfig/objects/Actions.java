package com.cockpitconfig.objects;

public class Actions {

	private int id;
	private int assertionConditionID;
	private String email;
	private String nagios;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAssertionConditionID() {
		return assertionConditionID;
	}

	public void setAssertionConditionID(int assertionConditionID) {
		this.assertionConditionID = assertionConditionID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNagios() {
		return nagios;
	}

	public void setNagios(String nagios) {
		this.nagios = nagios;
	}
}
