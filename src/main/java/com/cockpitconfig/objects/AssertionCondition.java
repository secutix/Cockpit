package com.cockpitconfig.objects;

import java.math.BigInteger;

public class AssertionCondition {

	private int id;
	private BigInteger minVal;
	private BigInteger maxVal;
	private BigInteger minDelta;
	private BigInteger maxDelta;
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

	public String getStream() {
		return stream;
	}

	public void setStream(String stream) {
		this.stream = stream;
	}

	public BigInteger getMinVal() {
		return minVal;
	}

	public void setMinVal(BigInteger minVal) {
		this.minVal = minVal;
	}

	public BigInteger getMaxVal() {
		return maxVal;
	}

	public void setMaxVal(BigInteger maxVal) {
		this.maxVal = maxVal;
	}

	public BigInteger getMinDelta() {
		return minDelta;
	}

	public void setMinDelta(BigInteger minDelta) {
		this.minDelta = minDelta;
	}

	public BigInteger getMaxDelta() {
		return maxDelta;
	}

	public void setMaxDelta(BigInteger maxDelta) {
		this.maxDelta = maxDelta;
	}
}
