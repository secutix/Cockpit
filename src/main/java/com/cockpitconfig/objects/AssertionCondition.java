package com.cockpitconfig.objects;

public class AssertionCondition {

	private int id;
	private int criteriaID;
	private Integer minVal;
	private Integer maxVal;
	private Integer minDelta;
	private Integer maxDelta;
	private int timeFrameID;
	private int notificationID;
	private int assertionGroupID;
	private int assertionIndex;

	/*final int MAX_INTIALIZE = Integer.MAX_VALUE;
	final int MIN_INTIALIZE = Integer.MIN_VALUE;

	public AssertionCondition() {
		this.maxDelta = MAX_INTIALIZE;
		this.maxVal = MAX_INTIALIZE;
		this.minDelta = MIN_INTIALIZE;
		this.minVal = MIN_INTIALIZE;
	}*/

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCriteriaID() {
		return criteriaID;
	}

	public void setCriteriaID(int criteriaID) {
		this.criteriaID = criteriaID;
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
}
