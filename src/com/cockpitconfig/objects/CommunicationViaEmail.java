package com.cockpitconfig.objects;

public class CommunicationViaEmail {

	private int id;
	private int assertionGroupID;
	private String recipents;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAssertionGroupID() {
		return assertionGroupID;
	}

	public void setAssertionGroupID(int assertionGroupID) {
		this.assertionGroupID = assertionGroupID;
	}

	public String getRecipents() {
		return recipents;
	}

	public void setRecipents(String recipents) {
		this.recipents = recipents;
	}
}
