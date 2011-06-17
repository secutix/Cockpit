package com.cockpitconfig.objects;

public class AssertionCondition {

	private int id;
	private Integer minVal;
	private Integer maxVal;
	private Integer minDelta;
	private Integer maxDelta;
	private int timeFrameID;
	private int notificationID;
	private int assertionGroupID;
	private int assertionIndex;
	private String stream;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTimeFrameID() {
		return timeFrameID;
	}

	public void setTimeFrameID(int timeFrameID) {
		this.timeFrameID = timeFrameID;
	}

	public int getNotificationID() {
		return notificationID;
	}

	public void setNotificationID(int notificationID) {
		this.notificationID = notificationID;
	}

	public int getAssertionGroupID() {
		return assertionGroupID;
	}

	public void setAssertionGroupID(int assertionGroupID) {
		this.assertionGroupID = assertionGroupID;
	}

	public int getAssertionIndex() {
		return assertionIndex;
	}

	public void setAssertionIndex(int assertionIndex) {
		this.assertionIndex = assertionIndex;
	}

	public Integer getMinVal() {
		return minVal;
	}

	public void setMinVal(Integer minVal) {
		this.minVal = minVal;
	}

	public Integer getMaxVal() {
		return maxVal;
	}

	public void setMaxVal(Integer maxVal) {
		this.maxVal = maxVal;
	}

	public Integer getMinDelta() {
		return minDelta;
	}

	public void setMinDelta(Integer minDelta) {
		this.minDelta = minDelta;
	}

	public Integer getMaxDelta() {
		return maxDelta;
	}

	public void setMaxDelta(Integer maxDelta) {
		this.maxDelta = maxDelta;
	}

	public String getStream() {
		return stream;
	}

	public void setStream(String stream) {
		this.stream = stream;
	}
}
