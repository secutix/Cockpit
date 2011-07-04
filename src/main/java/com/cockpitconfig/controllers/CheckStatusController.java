package com.cockpitconfig.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cockpitconfig.db.AssertionConditionDAO;
import com.cockpitconfig.db.AssertionGroupDAO;
import com.cockpitconfig.db.NotificationOccurrencesDAO;
import com.cockpitconfig.db.SourcesDAO;
import com.cockpitconfig.db.TimeConstraintsDAO;
import com.cockpitconfig.objects.AssertionCondition;
import com.cockpitconfig.objects.AssertionGroup;
import com.cockpitconfig.objects.NotificationOccurrence;
import com.cockpitconfig.objects.TimeConstraints;

@Controller
@RequestMapping("/checkStatus")
public class CheckStatusController {

	public enum TimeFrameEnum {
		PER_STEP, PER_5_STEP, PER_10_STEP, PER_25_STEP, PER_50_STEP
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView CheckStatus() throws Exception {

		ModelAndView model = new ModelAndView("checkStatus");

		SqlSessionFactory sf = MyBatisSqlSessionFactory.getSqlSessionFactory();
		AssertionGroupDAO agDao = new AssertionGroupDAO(sf);
		// HashMap<Integer, AssertionGroup> allPKs = agDao.getAllPKandSource();
		ArrayList<AssertionGroup> allRules = agDao.getAllrules();

		for (int i = 0; i < allRules.size(); ++i) {
			AssertionGroup tempObj = allRules.get(i);
			checkRule(tempObj.getId(), tempObj.getSource(), tempObj.getConstraintName(), tempObj.getCommunicationID(), sf);
		}

		return model;
	}

	/**
	 * Function which monitor each rule
	 * 
	 * @param PK
	 *            Primary Key of assertion group table
	 * @param sourcePK
	 *            SourcePK stored in Assertion Group table
	 * @param sf
	 * @throws Exception
	 */
	private void checkRule(int PK, int sourcePK, String constraintName, int communicationID, SqlSessionFactory sf) throws Exception {

		AssertionConditionDAO acDao = new AssertionConditionDAO(sf);
		SourcesDAO sourcesDao = new SourcesDAO(sf);
		String sourceUrl = sourcesDao.getSourceUrlForGivenPK(sourcePK);

		URL urlToMonitor = new URL(sourceUrl);
		BufferedReader availableStreams = new BufferedReader(new InputStreamReader(urlToMonitor.openStream()));
		String inputStream;

		HashMap<String, String> streamValueMap = new HashMap<String, String>();

		while ((inputStream = availableStreams.readLine()) != null) {
			String key = inputStream.substring(0, inputStream.indexOf(','));
			String value = inputStream.substring(inputStream.indexOf(',') + 1);
			streamValueMap.put(key, value);
		}

		availableStreams.close();

		ArrayList<AssertionCondition> rulesToMonitor = acDao.getRuleRow(PK);

		TimeConstraintsDAO tcDao = new TimeConstraintsDAO(sf);
		ArrayList<TimeConstraints> constrainedTime = tcDao.getfrequencyRow(PK);

		for (int i = 0; i < rulesToMonitor.size(); ++i) {
			AssertionCondition temp = rulesToMonitor.get(i);
			String tempStream = temp.getStream();
			int timeFrameIndex = temp.getTimeFrameID();
			int assertionConditionID = temp.getId();
			int notificationLevelID = temp.getNotificationID();
			int assertionIndex = temp.getAssertionIndex();
			String values = streamValueMap.get(tempStream);

			String header = values.substring(0, values.indexOf('|'));
			String dataValue = values.substring(values.indexOf('|') + 1);
			String[] valueList = dataValue.split(",");

			// Condition is to check whether "is/are" selected or "has slope"
			if (temp.getMaxDelta() == null && temp.getMinDelta() == null) {
				// This condition is to check whether "equal to" is selected
				if (temp.getMinVal() != null && temp.getMaxVal() != null) {
					if (timeFrameIndex == TimeFrameEnum.PER_STEP.ordinal()) {
						int increamentSize = 1;
						checkAgainstIsAreEqualTo(increamentSize, valueList, temp.getMaxVal(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_5_STEP.ordinal()) {
						int increamentSize = 5;
						checkAgainstIsAreEqualTo(increamentSize, valueList, temp.getMaxVal(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_10_STEP.ordinal()) {
						int increamentSize = 10;
						checkAgainstIsAreEqualTo(increamentSize, valueList, temp.getMaxVal(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_25_STEP.ordinal()) {
						int increamentSize = 25;
						checkAgainstIsAreEqualTo(increamentSize, valueList, temp.getMaxVal(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_50_STEP.ordinal()) {
						int increamentSize = 50;
						checkAgainstIsAreEqualTo(increamentSize, valueList, temp.getMaxVal(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					}
				} else if (temp.getMinVal() != null) {
					if (timeFrameIndex == TimeFrameEnum.PER_STEP.ordinal()) {
						int increamentSize = 1;
						checkAgainstIsAreGreaterThan(increamentSize, valueList, temp.getMinVal(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_5_STEP.ordinal()) {
						int increamentSize = 5;
						checkAgainstIsAreGreaterThan(increamentSize, valueList, temp.getMinVal(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_10_STEP.ordinal()) {
						int increamentSize = 10;
						checkAgainstIsAreGreaterThan(increamentSize, valueList, temp.getMinVal(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_25_STEP.ordinal()) {
						int increamentSize = 25;
						checkAgainstIsAreGreaterThan(increamentSize, valueList, temp.getMinVal(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_50_STEP.ordinal()) {
						int increamentSize = 50;
						checkAgainstIsAreGreaterThan(increamentSize, valueList, temp.getMinVal(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					}
				} else {
					if (timeFrameIndex == TimeFrameEnum.PER_STEP.ordinal()) {
						int increamentSize = 1;
						checkAgainstIsAreLessThan(increamentSize, valueList, temp.getMaxVal(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_5_STEP.ordinal()) {
						int increamentSize = 5;
						checkAgainstIsAreLessThan(increamentSize, valueList, temp.getMaxVal(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_10_STEP.ordinal()) {
						int increamentSize = 10;
						checkAgainstIsAreLessThan(increamentSize, valueList, temp.getMaxVal(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_25_STEP.ordinal()) {
						int increamentSize = 25;
						checkAgainstIsAreLessThan(increamentSize, valueList, temp.getMaxVal(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_50_STEP.ordinal()) {
						int increamentSize = 50;
						checkAgainstIsAreLessThan(increamentSize, valueList, temp.getMaxVal(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					}
				}
			} else {
				if (temp.getMinDelta() != null && temp.getMaxDelta() != null) {
					if (timeFrameIndex == TimeFrameEnum.PER_STEP.ordinal()) {
						int increamentSize = 1;
						checkAgainstHasSlopeIsEqualTo(increamentSize, valueList, temp.getMaxDelta(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_5_STEP.ordinal()) {
						int increamentSize = 5;
						checkAgainstHasSlopeIsEqualTo(increamentSize, valueList, temp.getMaxDelta(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_10_STEP.ordinal()) {
						int increamentSize = 10;
						checkAgainstHasSlopeIsEqualTo(increamentSize, valueList, temp.getMaxDelta(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_25_STEP.ordinal()) {
						int increamentSize = 25;
						checkAgainstHasSlopeIsEqualTo(increamentSize, valueList, temp.getMaxDelta(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_50_STEP.ordinal()) {
						int increamentSize = 50;
						checkAgainstHasSlopeIsEqualTo(increamentSize, valueList, temp.getMaxDelta(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					}
				} else if (temp.getMinDelta() != null) {
					if (timeFrameIndex == TimeFrameEnum.PER_STEP.ordinal()) {
						int increamentSize = 1;
						checkAgainstHasSlopeGreaterThan(increamentSize, valueList, temp.getMinDelta(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_5_STEP.ordinal()) {
						int increamentSize = 5;
						checkAgainstHasSlopeGreaterThan(increamentSize, valueList, temp.getMinDelta(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_10_STEP.ordinal()) {
						int increamentSize = 10;
						checkAgainstHasSlopeGreaterThan(increamentSize, valueList, temp.getMinDelta(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_25_STEP.ordinal()) {
						int increamentSize = 25;
						checkAgainstHasSlopeGreaterThan(increamentSize, valueList, temp.getMinDelta(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_50_STEP.ordinal()) {
						int increamentSize = 50;
						checkAgainstHasSlopeGreaterThan(increamentSize, valueList, temp.getMinDelta(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					}
				} else {
					if (timeFrameIndex == TimeFrameEnum.PER_STEP.ordinal()) {
						int increamentSize = 1;
						checkAgainstHasSlopeLessThan(increamentSize, valueList, temp.getMaxDelta(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_5_STEP.ordinal()) {
						int increamentSize = 5;
						checkAgainstHasSlopeLessThan(increamentSize, valueList, temp.getMaxDelta(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_10_STEP.ordinal()) {
						int increamentSize = 10;
						checkAgainstHasSlopeLessThan(increamentSize, valueList, temp.getMaxDelta(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_25_STEP.ordinal()) {
						int increamentSize = 25;
						checkAgainstHasSlopeLessThan(increamentSize, valueList, temp.getMaxDelta(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					} else if (timeFrameIndex == TimeFrameEnum.PER_50_STEP.ordinal()) {
						int increamentSize = 50;
						checkAgainstHasSlopeLessThan(increamentSize, valueList, temp.getMaxDelta(), assertionConditionID, notificationLevelID,
								constraintName, communicationID, assertionIndex, header, constrainedTime, sf);
					}
				}
			}
		}
	}

	/**
	 * Function which returns current data or time
	 * 
	 * @param dateFormat
	 * @return String containing current date or time
	 */
	public String now(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());
	}

	/**
	 * Checks a row of a rule against current value when rule contains "is/are" and "equal to". Upon violation, insert a row in NotificationOccurrence table
	 * 
	 * @param increamentSize
	 *            describes steps to check against
	 * @param valueList
	 *            contains the current value
	 * @param maxVal
	 *            value of user supplied data same as minVal in this case
	 * @param assertionConditionID
	 *            PK of row in assertionCondition table
	 * @param notificationLevelID
	 *            PK of notificationLevel in NotificationLevel table
	 */

	private void checkAgainstIsAreEqualTo(int increamentSize, String[] valueList, BigInteger maxValue, int assertionConditionID,
			int notificationLevelID, String constraintName, int communicationID, int assertionIndex, String header,
			ArrayList<TimeConstraints> constrainedTime, SqlSessionFactory sf) {

		for (int j = 0; j < valueList.length; j = j + increamentSize) {
			if (!isDisabled(header, constrainedTime, j) && valueList[j] != null) {
				String s = valueList[j].substring(0, valueList[j].indexOf("."));
				BigInteger dataValue = new BigInteger(s);
				int checkCondition = maxValue.compareTo(dataValue);
				if (checkCondition == 0) {
					String curentTime = now("H:mm:ss");
					String currentDate = now("yy/MM/dd");
					String alertType = new String();
					switch (notificationLevelID) {
					case 0:
						alertType = "INFO";
						break;
					case 1:
						alertType = "WARNING";
						break;
					case 2:
						alertType = "ALERT";
						break;
					default:
						break;
					}

					NotificationOccurrence notiOccurrence = new NotificationOccurrence();
					notiOccurrence.setAssertionConditionID(assertionConditionID);
					notiOccurrence.setTimeOccur(curentTime);
					notiOccurrence.setDateOccur(currentDate);
					notiOccurrence.setType(alertType);
					notiOccurrence.setDescription("[" + constraintName + "] | Stream index " + assertionIndex + " ("
							+ (int) Float.parseFloat(valueList[j]) + ") != " + maxValue);
					NotificationOccurrencesDAO notiOccDao = new NotificationOccurrencesDAO(sf);
					notiOccDao.setRow(notiOccurrence);
				}
			}
		}
	}

	/**
	 * Checks a row of a rule against current value when rule contains "is/are" and "greater than". Upon violation, insert a row in NotificationOccurrence table
	 * 
	 * @param increamentSize
	 *            describes steps to check against
	 * @param valueList
	 *            contains the current value
	 * @param minVal
	 *            value of user supplied data
	 * @param assertionConditionID
	 *            PK of row in assertionCondition table
	 * @param notificationLevelID
	 *            PK of notificationLevel in NotificationLevel table
	 */

	private void checkAgainstIsAreGreaterThan(int increamentSize, String[] valueList, BigInteger minValue, int assertionConditionID,
			int notificationLevelID, String constraintName, int communicationID, int assertionIndex, String header,
			ArrayList<TimeConstraints> constrainedTime, SqlSessionFactory sf) {

		for (int j = 0; j < valueList.length; j = j + increamentSize) {
			if (!isDisabled(header, constrainedTime, j) && valueList[j] != null) {
				String s = valueList[j].substring(0, valueList[j].indexOf("."));
				BigInteger dataValue = new BigInteger(s);
				int checkCondition = dataValue.compareTo(minValue);
				if (checkCondition > 0) {
					String curentTime = now("H:mm:ss");
					String currentDate = now("yy/MM/dd");
					String alertType = new String();
					switch (notificationLevelID) {
					case 0:
						alertType = "INFO";
						break;
					case 1:
						alertType = "WARNING";
						break;
					case 2:
						alertType = "ALERT";
						break;
					default:
						break;
					}

					NotificationOccurrence notiOccurrence = new NotificationOccurrence();
					notiOccurrence.setAssertionConditionID(assertionConditionID);
					notiOccurrence.setTimeOccur(curentTime);
					notiOccurrence.setDateOccur(currentDate);
					notiOccurrence.setType(alertType);
					notiOccurrence.setDescription("[" + constraintName + "] | Stream index " + assertionIndex + " ("
							+ (int) Float.parseFloat(valueList[j]) + ") > " + minValue);
					NotificationOccurrencesDAO notiOccDao = new NotificationOccurrencesDAO(sf);
					notiOccDao.setRow(notiOccurrence);
				}
			}
		}
	}

	/**
	 * Checks a row of a rule against current value when rule contains "is/are" and "less than". Upon violation, insert a row in NotificationOccurrence table
	 * 
	 * @param increamentSize
	 *            describes steps to check against
	 * @param valueList
	 *            contains the current value
	 * @param maxVal
	 *            value of user supplied data
	 * @param assertionConditionID
	 *            PK of row in assertionCondition table
	 * @param notificationLevelID
	 *            PK of notificationLevel in NotificationLevel table
	 */

	private void checkAgainstIsAreLessThan(int increamentSize, String[] valueList, BigInteger maxValue, int assertionConditionID,
			int notificationLevelID, String constraintName, int communicationID, int assertionIndex, String header,
			ArrayList<TimeConstraints> constrainedTime, SqlSessionFactory sf) {

		for (int j = 0; j < valueList.length; j = j + increamentSize) {
			if (!isDisabled(header, constrainedTime, j) && valueList[j] != null) {
				String s = valueList[j].substring(0, valueList[j].indexOf("."));
				BigInteger dataValue = new BigInteger(s);
				int checkCondition = maxValue.compareTo(dataValue);
				if (checkCondition > 0) {
					String curentTime = now("H:mm:ss");
					String currentDate = now("yy/MM/dd");
					String alertType = new String();
					switch (notificationLevelID) {
					case 0:
						alertType = "INFO";
						break;
					case 1:
						alertType = "WARNING";
						break;
					case 2:
						alertType = "ALERT";
						break;
					default:
						break;
					}

					NotificationOccurrence notiOccurrence = new NotificationOccurrence();
					notiOccurrence.setAssertionConditionID(assertionConditionID);
					notiOccurrence.setTimeOccur(curentTime);
					notiOccurrence.setDateOccur(currentDate);
					notiOccurrence.setType(alertType);
					notiOccurrence.setDescription("[" + constraintName + "] | Stream index " + assertionIndex + " ("
							+ (int) Float.parseFloat(valueList[j]) + ") < " + maxValue);
					NotificationOccurrencesDAO notiOccDao = new NotificationOccurrencesDAO(sf);
					notiOccDao.setRow(notiOccurrence);
				}
			}
		}
	}

	private void checkAgainstHasSlopeIsEqualTo(int increamentSize, String[] valueList, BigInteger maxValue, int assertionConditionID,
			int notificationLevelID, String constraintName, int communicationID, int assertionIndex, String header,
			ArrayList<TimeConstraints> constrainedTime, SqlSessionFactory sf) {

		for (int j = 0; j < valueList.length - increamentSize; j = j + increamentSize) {
			if (valueList[j + increamentSize] != null && valueList[j] != null) {
				String s1 = valueList[j + increamentSize].substring(0, valueList[j + increamentSize].indexOf("."));
				String s0 = valueList[j].substring(0, valueList[j].indexOf("."));
				BigInteger dataValue1 = new BigInteger(s1);
				BigInteger dataValue0 = new BigInteger(s0);

				Integer increamentStep = new Integer(increamentSize);
				String in = increamentStep.toString();
				BigInteger parsedStep = new BigInteger(in);

				BigInteger LHS = dataValue0.multiply(dataValue1);
				BigInteger RHS = maxValue.multiply(parsedStep);

				if (LHS.equals(RHS)) {
					String curentTime = now("H:mm:ss");
					String currentDate = now("yy/MM/dd");
					String alertType = new String();
					switch (notificationLevelID) {
					case 0:
						alertType = "INFO";
						break;
					case 1:
						alertType = "WARNING";
						break;
					case 2:
						alertType = "ALERT";
						break;
					default:
						break;
					}

					NotificationOccurrence notiOccurrence = new NotificationOccurrence();
					notiOccurrence.setAssertionConditionID(assertionConditionID);
					notiOccurrence.setTimeOccur(curentTime);
					notiOccurrence.setDateOccur(currentDate);
					notiOccurrence.setType(alertType);
					notiOccurrence.setDescription("[" + constraintName + "] | Stream index " + assertionIndex + " ("
							+ (int) Float.parseFloat(valueList[j]) + ") != " + maxValue);
					NotificationOccurrencesDAO notiOccDao = new NotificationOccurrencesDAO(sf);
					notiOccDao.setRow(notiOccurrence);
				}
			}
		}
	}

	private void checkAgainstHasSlopeGreaterThan(int increamentSize, String[] valueList, BigInteger minValue, int assertionConditionID,
			int notificationLevelID, String constraintName, int communicationID, int assertionIndex, String header,
			ArrayList<TimeConstraints> constrainedTime, SqlSessionFactory sf) {

		for (int j = 0; j < valueList.length - increamentSize; j = j + increamentSize) {
			if (valueList[j + increamentSize] != null && valueList[j] != null) {
				String s1 = valueList[j + increamentSize].substring(0, valueList[j + increamentSize].indexOf("."));
				String s0 = valueList[j].substring(0, valueList[j].indexOf("."));
				BigInteger dataValue1 = new BigInteger(s1);
				BigInteger dataValue0 = new BigInteger(s0);

				Integer increamentStep = new Integer(increamentSize);
				String in = increamentStep.toString();
				BigInteger parsedStep = new BigInteger(in);

				BigInteger LHS = dataValue0.multiply(dataValue1);
				BigInteger RHS = minValue.multiply(parsedStep);

				if (LHS.compareTo(RHS) > 0) {
					String curentTime = now("H:mm:ss");
					String currentDate = now("yy/MM/dd");
					String alertType = new String();
					switch (notificationLevelID) {
					case 0:
						alertType = "INFO";
						break;
					case 1:
						alertType = "WARNING";
						break;
					case 2:
						alertType = "ALERT";
						break;
					default:
						break;
					}

					NotificationOccurrence notiOccurrence = new NotificationOccurrence();
					notiOccurrence.setAssertionConditionID(assertionConditionID);
					notiOccurrence.setTimeOccur(curentTime);
					notiOccurrence.setDateOccur(currentDate);
					notiOccurrence.setType(alertType);
					notiOccurrence.setDescription("[" + constraintName + "] | Stream index " + assertionIndex + " ("
							+ (int) Float.parseFloat(valueList[j]) + ") > " + minValue);
					NotificationOccurrencesDAO notiOccDao = new NotificationOccurrencesDAO(sf);
					notiOccDao.setRow(notiOccurrence);
				}
			}
		}
	}

	private void checkAgainstHasSlopeLessThan(int increamentSize, String[] valueList, BigInteger maxValue, int assertionConditionID,
			int notificationLevelID, String constraintName, int communicationID, int assertionIndex, String header,
			ArrayList<TimeConstraints> constrainedTime, SqlSessionFactory sf) {

		for (int j = 0; j < valueList.length - increamentSize; j = j + increamentSize) {
			if (valueList[j + increamentSize] != null && valueList[j] != null) {
				String s1 = valueList[j + increamentSize].substring(0, valueList[j + increamentSize].indexOf("."));
				String s0 = valueList[j].substring(0, valueList[j].indexOf("."));
				BigInteger dataValue1 = new BigInteger(s1);
				BigInteger dataValue0 = new BigInteger(s0);

				Integer increamentStep = new Integer(increamentSize);
				String in = increamentStep.toString();
				BigInteger parsedStep = new BigInteger(in);

				BigInteger LHS = dataValue0.multiply(dataValue1);
				BigInteger RHS = maxValue.multiply(parsedStep);

				if (RHS.compareTo(LHS) > 0) {
					String curentTime = now("H:mm:ss");
					String currentDate = now("yy/MM/dd");
					String alertType = new String();
					switch (notificationLevelID) {
					case 0:
						alertType = "INFO";
						break;
					case 1:
						alertType = "WARNING";
						break;
					case 2:
						alertType = "ALERT";
						break;
					default:
						break;
					}

					NotificationOccurrence notiOccurrence = new NotificationOccurrence();
					notiOccurrence.setAssertionConditionID(assertionConditionID);
					notiOccurrence.setTimeOccur(curentTime);
					notiOccurrence.setDateOccur(currentDate);
					notiOccurrence.setType(alertType);
					notiOccurrence.setDescription("[" + constraintName + "] | Stream index " + assertionIndex + " ("
							+ (int) Float.parseFloat(valueList[j]) + ") < " + maxValue);
					NotificationOccurrencesDAO notiOccDao = new NotificationOccurrencesDAO(sf);
					notiOccDao.setRow(notiOccurrence);
				}
			}
		}
	}

	/**
	 * Function to check rules against time constraint specified by users.
	 * 
	 * @param header
	 * @param constrainedTime
	 * @param index
	 * @return
	 */
	private boolean isDisabled(String header, ArrayList<TimeConstraints> constrainedTime, int index) {
		String[] headerList = header.split(",");
		String startTime = headerList[0];
		// String endTime = headerList[1];
		String stepSize = headerList[2];

		Date constrinedDate = new Date(Long.parseLong(startTime + index * Integer.parseInt(stepSize)) * 1000);
		Calendar cal = Calendar.getInstance();
		cal.setTime(constrinedDate);

		int day = cal.get(Calendar.DAY_OF_WEEK);

		ArrayList<String> constraindDay = getDayTimes(constrainedTime, day);

		String HH = new SimpleDateFormat("HH").format(constrinedDate);
		String MM = new SimpleDateFormat("mm").format(constrinedDate);

		for (int i = 0; i < constraindDay.size(); i = i + 2) {
			if (constraindDay.get(i) != null) {
				if (checkForTime(HH, MM, constraindDay.get(i), constraindDay.get(i + 1)) == true) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean checkForTime(String epochStartTimes, String epochEndTimes, String constrainedStartTimes, String constrainedEndTimes) {
		String[] constrainedStart = constrainedStartTimes.split(":");
		String[] constrainedEnd = constrainedEndTimes.split(":");

		int epochTimeHH = Integer.parseInt(epochStartTimes);
		int epochTimeMM = Integer.parseInt(epochEndTimes);
		int constrainedStartTimeHH = Integer.parseInt(constrainedStart[0]);
		int constrainedStartTimeMM = Integer.parseInt(constrainedStart[1]);
		int constrainedEndTimeHH = Integer.parseInt(constrainedEnd[0]);
		int constrainedEndTimeMM = Integer.parseInt(constrainedEnd[1]);

		if (epochTimeHH < constrainedStartTimeHH) {
			return false;
		} else if (epochTimeHH > constrainedEndTimeHH) {
			return false;
		} else {
			if (epochTimeHH == constrainedStartTimeHH && epochTimeMM < constrainedStartTimeMM) {
				return false;
			} else if (epochTimeHH == constrainedEndTimeHH && epochTimeMM > constrainedEndTimeMM) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * Function to check whether rule is enabled or disabled on a particular day
	 * 
	 * @param constraintTime
	 *            timeConstraint list
	 * @param day
	 *            Day as calculated by epoch time. graphite day
	 * @return list of string containing 2 entries for everyday if day is enable then first entry represents start timing and second entry represents end timing
	 *         else null in both entries
	 */
	private ArrayList<String> getDayTimes(ArrayList<TimeConstraints> constraintTime, int day) {
		ArrayList<String> dayTime = new ArrayList<String>();

		switch (day) {
		case 0:
			for (int i = 0; i < constraintTime.size(); ++i) {
				TimeConstraints temp = constraintTime.get(i);
				if (temp.getSunday() == 1) {
					int hhStart = temp.getStartHour();
					int mmStart = temp.getStartMin();
					int hhEnd = temp.getEndHour();
					int mmEnd = temp.getEndMin();
					dayTime.add(hhStart + ":" + mmStart);
					dayTime.add(hhEnd + ":" + mmEnd);
				} else {
					dayTime.add(null);
					dayTime.add(null);
				}
			}
			break;
		case 1:
			for (int i = 0; i < constraintTime.size(); ++i) {
				TimeConstraints temp = constraintTime.get(i);
				if (temp.getMonday() == 1) {
					int hhStart = temp.getStartHour();
					int mmStart = temp.getStartMin();
					int hhEnd = temp.getEndHour();
					int mmEnd = temp.getEndMin();
					dayTime.add(hhStart + ":" + mmStart);
					dayTime.add(hhEnd + ":" + mmEnd);
				} else {
					dayTime.add(null);
					dayTime.add(null);
				}
			}
			break;
		case 2:
			for (int i = 0; i < constraintTime.size(); ++i) {
				TimeConstraints temp = constraintTime.get(i);
				if (temp.getTuesday() == 1) {
					int hhStart = temp.getStartHour();
					int mmStart = temp.getStartMin();
					int hhEnd = temp.getEndHour();
					int mmEnd = temp.getEndMin();
					dayTime.add(hhStart + ":" + mmStart);
					dayTime.add(hhEnd + ":" + mmEnd);
				} else {
					dayTime.add(null);
					dayTime.add(null);
				}
			}
			break;
		case 3:
			for (int i = 0; i < constraintTime.size(); ++i) {
				TimeConstraints temp = constraintTime.get(i);
				if (temp.getWednesday() == 1) {
					int hhStart = temp.getStartHour();
					int mmStart = temp.getStartMin();
					int hhEnd = temp.getEndHour();
					int mmEnd = temp.getEndMin();
					dayTime.add(hhStart + ":" + mmStart);
					dayTime.add(hhEnd + ":" + mmEnd);
				} else {
					dayTime.add(null);
					dayTime.add(null);
				}
			}
			break;
		case 4:
			for (int i = 0; i < constraintTime.size(); ++i) {
				TimeConstraints temp = constraintTime.get(i);
				if (temp.getThursday() == 1) {
					int hhStart = temp.getStartHour();
					int mmStart = temp.getStartMin();
					int hhEnd = temp.getEndHour();
					int mmEnd = temp.getEndMin();
					dayTime.add(hhStart + ":" + mmStart);
					dayTime.add(hhEnd + ":" + mmEnd);
				} else {
					dayTime.add(null);
					dayTime.add(null);
				}
			}
			break;
		case 5:
			for (int i = 0; i < constraintTime.size(); ++i) {
				TimeConstraints temp = constraintTime.get(i);
				if (temp.getFriday() == 1) {
					int hhStart = temp.getStartHour();
					int mmStart = temp.getStartMin();
					int hhEnd = temp.getEndHour();
					int mmEnd = temp.getEndMin();
					dayTime.add(hhStart + ":" + mmStart);
					dayTime.add(hhEnd + ":" + mmEnd);
				} else {
					dayTime.add(null);
					dayTime.add(null);
				}
			}
			break;
		case 6:
			for (int i = 0; i < constraintTime.size(); ++i) {
				TimeConstraints temp = constraintTime.get(i);
				if (temp.getSaturday() == 1) {
					int hhStart = temp.getStartHour();
					int mmStart = temp.getStartMin();
					int hhEnd = temp.getEndHour();
					int mmEnd = temp.getEndMin();
					dayTime.add(hhStart + ":" + mmStart);
					dayTime.add(hhEnd + ":" + mmEnd);
				} else {
					dayTime.add(null);
					dayTime.add(null);
				}
			}
			break;

		default:
			break;

		}

		return dayTime;
	}

}
