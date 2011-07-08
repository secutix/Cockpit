<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ include file="../../libraries.html"%>
<title>Cockpit</title>
</head>

<body>
<script type="text/javascript">
	Ext.onReady(function() {
		Ext.QuickTips.init();

		// turn on validation errors besabide the field globally
		Ext.form.Field.prototype.msgTarget = 'side';

		var bd = Ext.getBody();

		var streamBoxes = new Array(); // array to store streams comboBox for different rules
		var timeFrameBoxes = new Array(); // array to store timeframe comboBox for different rules
		var notificationBoxes = new Array(); // array to store notification comboBox for different rules
		var isAreBoxes = new Array(); // array to store isAre comboBox for different rules
		var slopeBoxes = new Array(); // array to store slope comboBox for different rules
		var numberBoxes = new Array(); // array to store user supplied value
		var frequencyForms = new Array(); // array to create each row of a rule
		var ruleForms = new Array(); // array to create each row of a frequency

		var ruleCount = 0; // variable to count total number of rows in a rule
		var sourceSelected = false; // boolean variable to check whether is seleced or not
		var streamStore; // Simplestore to store stram names to display once selection of source is done
		var fromFields = new Array(); // array to store start time value when disable on is enabled
		var toFields = new Array(); // array to store end time value when disable on is enabled
		var frequencyFields = new Array(); // array to store row of "disabled on" option
		var frequencyCount = 0; // varaiable to count number of rows of time constraint
		var steps; // step size of graphite data

		// array to store all rule name
		var dropDownRules = new Array();
		var r = 0;

		// Using JSTL, list of existing rule name is passed to display ir to user
		<c:forEach items="${ruleNames}" var="ruleNames">
		dropDownRules[r] = new Array();
		dropDownRules[r][0] = r.toString();
		dropDownRules[r][1] = "<c:out escapeXml='false' value="${ruleNames}"/>";
		r ++;
		</c:forEach>

		var rules = new Ext.data.SimpleStore({
			fields : [ 'id', 'ruleNames' ],
			data : dropDownRules
		});

		// ComboBox which contains all the available rules as well as able to store new rule name.
		var RuleList = function() {
			nameField = new Ext.form.ComboBox({
				store : rules,
				mode : 'local',
				forceSelection : false,
				allowBlank : false,
				fieldLabel : 'Name',
				name : 'rules',
				anchor : '30%',
				displayField : 'ruleNames',
				emptyText : 'Enter Rule Name',
				valueField : 'id',
				listeners : {
					select : function(f, record, index) {
						showExistingScreen();
						Ext.getCmp('id-deleteRule').setDisabled(false);
					}
				}
			});

			return nameField;
		};

		var dropDownSources = new Array();
		var z = 0;

		// Using JSTL, list of existing sources name is passed to display ir to user
		<c:forEach items="${sources}" var="sources">
		dropDownSources[z] = new Array();
		dropDownSources[z][0] = z.toString();
		dropDownSources[z][1] = "<c:out escapeXml='false' value="${sources}"/>";
		z ++;
		</c:forEach>

		var sourceData = new Ext.data.SimpleStore({
			fields : [ 'id', 'sources' ],
			data : dropDownSources
		// multi-dimensional array
		});

		// ComboBox which contains all the available sources
		var SourceList = function() {
			sourceField = new Ext.form.ComboBox({
				store : sourceData,
				mode : 'local',
				forceSelection : true,
				allowBlank : false,
				fieldLabel : 'Sources',
				resizable : true,
				name : 'sources',
				anchor : '85%',
				displayField : 'sources',
				emptyText : 'Select a Source',
				valueField : 'id',
				listeners : {
					select : function(f, record, index) {
						showAvailableStreams();
					}
				}
			});

			return sourceField;
		};

		// Number Field to store information about the rule
		var numberField = function() {
			numberBoxes[ruleCount] = new Ext.form.NumberField({
				fieldLabel : '',
				name : 'number',
				emptyText : '',
				allowNegative : false,
				//fieldLabel	: '%',
				anchor : '80%',
				emptyText : 'to compare value',
				allowBlank : false,
				valueField : 'number'
			});

			return numberBoxes[ruleCount];
		};

		/**
		 * Function to load the existing screen when user selects existing rule
		 */
		var showExistingScreen = function() {
			Ext.Ajax.request({
				url : 'assertion.htm',
				method : 'POST',
				params : {
					existingRule : nameField.getRawValue()
				},
				scope : this,

				success : function(response) {
					var existingValues = Ext.util.JSON.decode(response.responseText); // parameters are passed from controller as a JSON Object

					for ( var i = ruleCount; i > 1; -- i) {
						ruleForms[i].ownerCt.remove(ruleForms[i]);
						ruleForms.splice(i, 1);
						ruleCount --;
					}

					sourceField.setValue(existingValues.existSource);
					showAvailableStreams();

					for ( var i = 0; i < existingValues.existingStreams.length; ++ i) {
						if (ruleCount < i + 1) {
							addRuleTemplate(null);
						}

						streamBoxes[i + 1].setValue(existingValues.existingStreams[i]);
						timeFrameBoxes[i + 1].setValue(existingValues.existingTimeFrame[i]);
						notificationBoxes[i + 1].setValue(existingValues.existingNotification[i]);

						if (existingValues.existingMinDelta[i] == null && existingValues.existingMaxDelta[i] == null) {
							isAreBoxes[i + 1].setValue(1);
						} else {
							isAreBoxes[i + 1].setValue(2);
						}

						if ((existingValues.existingMinDelta[i] != null || existingValues.existingMinVal[i] != null)
								&& (existingValues.existingMaxDelta[i] != null || existingValues.existingMaxVal[i] != null)) {
							slopeBoxes[i + 1].setValue(2);
						} else if (existingValues.existingMinDelta[i] != null || existingValues.existingMinVal[i] != null) {
							slopeBoxes[i + 1].setValue(3);
						} else {
							slopeBoxes[i + 1].setValue(1);
						}

						if (existingValues.existingMinDelta[i] != null) {
							numberBoxes[i + 1].setValue(existingValues.existingMinDelta[i]);
						} else if (existingValues.existingMaxDelta[i] != null) {
							numberBoxes[i + 1].setValue(existingValues.existingMaxDelta[i]);
						} else if (existingValues.existingMinVal[i] != null) {
							numberBoxes[i + 1].setValue(existingValues.existingMinVal[i]);
						} else {
							numberBoxes[i + 1].setValue(existingValues.existingMaxVal[i]);
						}
					}

					for ( var i = frequencyCount; i > 1; -- i) {
						frequencyForms[i].ownerCt.remove(frequencyForms[i]);
						frequencyForms.splice(i, 1);
						frequencyCount --;
					}

					for ( var i = 0; i < existingValues.monday.length; ++ i) {
						if (frequencyCount < i + 1) {
							addFrequencyTemplate(null);
						}

						if (existingValues.monday[i] == 1) {
							frequencyFields[i + 1].setValue({
								1 : true
							});
						}
						if (existingValues.tuesday[i] == 1) {
							frequencyFields[i + 1].setValue({
								2 : true
							});
						}
						if (existingValues.wednesday[i] == 1) {
							frequencyFields[i + 1].setValue({
								3 : true
							});
						}
						if (existingValues.thursday[i] == 1) {
							frequencyFields[i + 1].setValue({
								4 : true
							});
						}
						if (existingValues.friday[i] == 1) {
							frequencyFields[i + 1].setValue({
								5 : true
							});
						}
						if (existingValues.saturday[i] == 1) {
							frequencyFields[i + 1].setValue({
								6 : true
							});
						}
						if (existingValues.sunday[i] == 1) {
							frequencyFields[i + 1].setValue({
								7 : true
							});
						}

						var str1 = existingValues.startHour[i];
						var str2 = existingValues.startMin[i];
						str1 += ""; 			// convert int to string
						str2 += "";
						if (str1.length == 1) {
							str1 = "0" + str1;
						}
						if (str2.length == 1) {
							str2 = "0" + str2;
						}
						fromFields[i + 1].setValue(str1 + ":" + str2);

						str1 = existingValues.endHour[i];
						str2 = existingValues.endMin[i];
						str1 += "";
						str2 += "";
						if (str1.length == 1) {
							str1 = "0" + str1;
						}
						if (str2.length == 1) {
							str2 = "0" + str2;
						}
						toFields[i + 1].setValue(str1 + ":" + str2);

					}

					if (existingValues.communicationVia == 0) {
						var radioItems = radios.items.items;
						radioItems[0].setValue(true);
						radioItems[1].setValue(false);
						emailTextField.setValue(existingValues.recipent);
					} else {
						var radioItems = radios.items.items;
						radioItems[0].setValue(false);
						radioItems[1].setValue(true);
					}
					win.doLayout();
				}
			});
		};

		streamStore = new Ext.data.SimpleStore({
			fields : [ 'id', 'streams' ],
			data : []
		// multi-dimensional array
		});

		/**
		 * Function to load available streams for a given source
		 */
		var showAvailableStreams = function() {
			Ext.Ajax.request({
				url : 'assertion.htm',
				method : 'POST',
				params : {
					selectedSource : sourceField.getRawValue()
				},
				scope : this,

				success : function(response) {
					//streamBoxes[ruleCount].setDisabled(false);
					var availableStreams = Ext.util.JSON.decode(response.responseText);

					var dropDownStream = new Array(availableStreams.streamCount);
					for ( var i = 0; i < availableStreams.streamCount; ++ i) {
						dropDownStream[i] = new Array();
						dropDownStream[i][0] = i.toString();
						dropDownStream[i][1] = availableStreams.streams[i];
					}

					steps = new Array(availableStreams.streamCount);
					for ( var i = 0; i < availableStreams.streamCount; ++ i) {
						steps[i] = availableStreams.stepSizeList[i];
					}
					streamStore.removeAll();
					streamStore.loadData(dropDownStream);

					sourceSelected = true;

					for ( var i = 1; i <= ruleCount; ++ i) {
						streamBoxes[i].setDisabled(false);
					}
				}
			});
		};

		// When Email as a communication mode is selected this field will be displayed for recipents address
		var emailTextField = new Ext.form.TextField({
			border : false,
			fieldLabel : 'Email',
			vtype : 'email',
			name : 'email',
			id : 'id-email',
			emptyText : 'Enter Email id',
			anchor : '80%',
			allowBlank : false
		});

		/*
		 * Function which display warning message whenever user tries to delete a row from "disable on" option
		 */
		var deleteRuleWarning = function(btn) {
			Ext.MessageBox.confirm('Confirm', 'Are you sure you want to remove this rule?', removeRule);
		};


		/**
		 * Handler to remove rule
		 */
		var removeRule = function(btn) {
			if (btn == 'yes') {
				Ext.Ajax.request({
					url : 'assertion.htm',
					method : 'POST',
					params : {
						ruleToDelete : nameField.getRawValue()
					},
					scope : this
				});
			}
			win.close();
		};

		var deleteButton = function() {
			return new Ext.Button({
				id		: 'id-deleteRule',
				text    : 'Delete Rule',
				//ctCls	: 'red-btn',
				width	: 70,
				height	: 25,
				disabled : true,
				handler : deleteRuleWarning
			});
		};

		var nameTextFieldContainer = {
			xtype : 'fieldset',
			flex : 1,
			border : false,
			hideBorders : true,
			autoHeight : true,
			labelWidth : 100,
			height : 42,
			width : 1250,
			frame : true,
			items: [{
				items : [ {
					rowWidth : .5,
					layout   : 'column',
					hideBorders : true,
					bodyBorder: false,
					items : [{
						columnWidth : 0.92,
						layout : 'form',
						items : [RuleList()]
					}, {
						columnWidth : .08,
						layout : 'form',
						items : [deleteButton()]
					}]
				}]
			}, SourceList()]

		};

		var AvailableStreamList = function() {
			streamBoxes[ruleCount] = new Ext.form.ComboBox({
				store : streamStore,
				mode : 'local',
				forceSelection : true,
				allowBlank : false,
				disabled : !sourceSelected,
				//fieldLabel	: 'Generates',
				resizable : true,
				name : 'stream',
				emptyText : 'Select a source first',
				anchor : '90%',
				displayField : 'streams',
				valueField : 'id',
				listeners : {
					select : function(f, record, index) {
						var ruleIndex = streamBoxes.indexOf(f);
						timeFrameBoxes[ruleIndex].emptyText = "Step size is " + steps[index] + " secs";
						timeFrameBoxes[ruleIndex].reset();
					}
				}
			});

			return streamBoxes[ruleCount];
		};

		var dropDownTimeFrame = new Array();
		var j = 0;

		<c:forEach items="${frames}" var="frames">
		dropDownTimeFrame[j] = new Array();
		dropDownTimeFrame[j][0] = j.toString();
		dropDownTimeFrame[j][1] = "<c:out escapeXml='false' value="${frames}"/>";
		j ++;
		</c:forEach>

		var frames = new Ext.data.SimpleStore({
			fields : [ 'id', 'frame' ],
			data : dropDownTimeFrame
		// multi-dimensional array
		});

		/**
		 * Function to create comboBox for timeFrame
		 */
		var TimeFrameList = function() {
			timeFrameBoxes[ruleCount] = new Ext.form.ComboBox({
				store : frames,
				mode : 'local',
				forceSelection : true,
				allowBlank : false,
				//fieldLabel		: 'Frames',
				name : 'frames',
				emptyText : 'Select Time Frame',
				anchor : '70%',
				displayField : 'frame',
				valueField : 'id',
				listeners : {
					select : function(f, record, index) {
						TimeFrameListIndex = index;
						//doTimeFrameUpdate(TimeFrameListIndex);
					}
				}
			});

			return timeFrameBoxes[ruleCount];
		};

		var dropDownNotificationLevel = new Array();
		var k = 0;

		<c:forEach items="${levels}" var="levels">
		dropDownNotificationLevel[k] = new Array();
		dropDownNotificationLevel[k][0] = k.toString();
		dropDownNotificationLevel[k][1] = "<c:out escapeXml='false' value="${levels}"/>";
		k ++;
		</c:forEach>

		var levels = new Ext.data.SimpleStore({
			fields : [ 'id', 'level' ],
			data : dropDownNotificationLevel
		// multi-dimensional array
		});

		/**
		 * Function to create comboBox for notificationLevel
		 */
		var NotificationLevelList = function() {
			notificationBoxes[ruleCount] = new Ext.form.ComboBox({
				store : levels,
				mode : 'local',
				forceSelection : true,
				allowBlank : false,
				//fieldLabel		: 'Generates',
				name : 'levels',
				emptyText : 'Generates',
				anchor : '70%',
				displayField : 'level',
				valueField : 'id',
				listeners : {
					select : function(f, record, index) {
						NotificationLevelListIndex = index;
						//doNotificationUpdate(NotificationLevelListIndex);
					}
				}
			});

			return notificationBoxes[ruleCount];
		};

		var isAre = new Ext.data.SimpleStore({
			fields : [ 'id', 'value' ],
			data : [ [ '1', 'is/are' ], [ '2', 'has slope' ] ]
		});

		var isAreCombo = function() {
			isAreBoxes[ruleCount] = new Ext.form.ComboBox({
				store : isAre,
				mode : 'local',
				forceSelection : true,
				allowBlank : false,
				//fieldLabel		: 'Frames',
				name : 'isAre',
				anchor : '70%',
				displayField : 'value',
				valueField : 'id'
			});

			return isAreBoxes[ruleCount];
		};

		var param = new Ext.data.SimpleStore({
			fields : [ 'id', 'value' ],
			data : [ [ '1', 'less than' ], [ '2', 'equal to' ], [ '3', 'greater than' ] ]
		});

		var slopeCombo = function() {
			slopeBoxes[ruleCount] = new Ext.form.ComboBox({
				store : param,
				mode : 'local',
				forceSelection : true,
				allowBlank : false,
				//fieldLabel	: 'Frames',
				name : 'slope',
				anchor : '70%',
				displayField : 'value',
				valueField : 'id',
				listeners : {
					select : function(f, record, index) {
					}
				}
			});

			return slopeBoxes[ruleCount];
		};

		/**
		 * Handler to add new rule template when user clicks on '+' button
		 */
		var addRuleTemplate = function(btn) {
			var index = ruleForms[ruleCount].ownerCt.items.indexOf(ruleForms[ruleCount]);
			ruleForms[ruleCount].ownerCt.insert(index + 1, getNewRuleForm());
			if (btn != null) {
				win.doLayout();
			}
		};

		var indexToRemove;
		/**
		 * Handler to display warning message to user when user tries to delete a rule
		 */
		var removeWarning = function(btn) {
			indexToRemove = ruleForms.indexOf(btn.ownerCt.ownerCt.ownerCt.ownerCt);
			Ext.MessageBox.confirm('Confirm', 'Are you sure you want to delete this rule?', removeRuleTemplate);
		};

		/**
		 * Function to delete new rule template when user confirms to delete a rule
		 */
		var removeRuleTemplate = function(btn) {
			if (btn == 'yes') {
				ruleCount --;
				if (ruleCount == 0) {
					ruleCount ++;
					Ext.MessageBox.show({
						title : 'Warning!',
						msg : 'Can not delete this rule! There must be atleast one constraint!',
						width : 300,
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.WARNING
					});
					return;
				} else {
					var index = indexToRemove;
					ruleForms[index].ownerCt.remove(ruleForms[index]);
					ruleForms.splice(index, 1);
					streamBoxes.splice(index, 1);
					timeFrameBoxes.splice(index, 1);
					notificationBoxes.splice(index, 1);
					isAreBoxes.splice(index, 1);
					slopeBoxes.splice(index, 1);
					numberBoxes.splice(index, 1);

					win.doLayout();
				}
			}
		};

		/**
		 * Function to add new rule template when user clicks on '+' button
		 */
		var plusButton = function() {
			return new Ext.Button({
				text : '+',
				width : 20,
				height : 20,
				handler : addRuleTemplate
			});
		};

		var minusButton = function() {
			return new Ext.Button({
				text : '-',
				width : 20,
				height : 20,
				handler : removeWarning
			});
		};

		/**
		 * Function responsible for adding new rule template
		 */
		var getNewRuleForm = function() {
			ruleCount ++;
			var ruleForm = new Ext.FormPanel({
				border : false,
				labelAlign : 'top',
				frame : true,
				items : [ {
					items : [ {
						rowWidth : .5,
						layout : 'column',
						hideBorders : true,
						bodyBorder: false,
						items : [ {
							columnWidth : .30,
							layout : 'form',
							items : [ AvailableStreamList() ]
						}, {
							columnWidth : .12,
							layout : 'form',
							items : [ isAreCombo() ]
						}, {
							columnWidth : .14,
							layout : 'form',
							items : [ slopeCombo() ]
						}, {
							columnWidth : .09,
							layout : 'form',
							items : [ numberField() ]
						}, {
							columnWidth : .15,
							layout : 'form',
							items : [ TimeFrameList() ]
						}, {
							columnWidth : .10,
							layout : 'form',
							items : [ NotificationLevelList() ]
						}, {
							columnWidth : .05,
							layout : 'form',
							items : [ plusButton() ]
						}, {
							columnWidth : .05,
							layout : 'form',
							items : [ minusButton() ]
						} ]
					} ]
				} ]
			});

			ruleForms[ruleCount] = ruleForm;
			return ruleForm;
		};

		/**
		 * Function to create checkBoxes for days
		 */
		var frequencyGroup = function() {
			frequencyFields[frequencyCount] = new Ext.form.CheckboxGroup({
				xtype : 'checkboxgroup',
				itemCls : 'x-check-group-alt',
				columns : 8,
				bodyStyle : 'padding-bottom:20px;',
				items : [ {
					boxLabel : 'MO',
					name : '1'
				}, {
					boxLabel : 'TU',
					name : '2'
				}, {
					boxLabel : 'WE',
					name : '3'
				}, {
					boxLabel : 'TH',
					name : '4'
				}, {
					boxLabel : 'FR',
					name : '5'
				}, {
					boxLabel : 'SA',
					name : '6'
				}, {
					boxLabel : 'SU',
					name : '7'
				} ],
				listeners : {
					change : function(ct, val) {
						if (ct.getValue().length > 0) {
							fromFields[frequencyFields.indexOf(ct)].enable(true);
							fromFields[frequencyFields.indexOf(ct)].allowBlank = 0;
							toFields[frequencyFields.indexOf(ct)].enable(true);
							toFields[frequencyFields.indexOf(ct)].allowBlank = 0;
						} else {
							fromFields[frequencyFields.indexOf(ct)].disable(true);
							fromFields[frequencyFields.indexOf(ct)].allowBlank = 1;
							toFields[frequencyFields.indexOf(ct)].disable(true);
							toFields[frequencyFields.indexOf(ct)].allowBlank = 1;
						}
					}
				}
			});

			return frequencyFields[frequencyCount];
		};

		/**
		 * Function to create textField to store information about start timing when user disable a rule for particular days
		 */
		var fromTimeField = function() {
			fromFields[frequencyCount] = new Ext.form.TextField({
				border : false,
				fieldLabel : 'From',
				regex : /^([0-1][0-9]|[2][0-3]):([0-5][0-9])$/,
				regexText : 'Invalid format. Enter time in HH:MM',
				disabled : true,
				emptyText : 'HH:MM | 24 HOUR FORMAT',
				allowBlank : false
			});

			return fromFields[frequencyCount];
		};

		/**
		 * Function to create textField to store information about end timing when user disable a rule for particular days
		 */
		var toTimeField = function() {
			toFields[frequencyCount] = new Ext.form.TextField({
				border : false,
				fieldLabel : 'To',
				regex : /^([0-1][0-9]|[2][0-3]):([0-5][0-9])$/,
				regexText : 'Invalid format. Enter time in HH:MM',
				disabled : true,
				emptyText : 'HH:MM | 24 HOUR FORMAT',
				allowBlank : false
			});

			return toFields[frequencyCount];
		};

		/**
		 * Handler to add template for "disable on" option
		 */
		var addFrequencyTemplate = function(btn1) {
			var index = frequencyForms[frequencyCount].ownerCt.items.indexOf(frequencyForms[frequencyCount]);
			frequencyForms[frequencyCount].ownerCt.insert(index + 1, getNewFrequencyForm());
			win.doLayout();
		};

		/**
		 * Function which displays '+' button in "disable on" option
		 */
		var plusFrequencyButton = function() {
			return new Ext.Button({
				text : '+',
				width : 20,
				height : 20,
				handler : addFrequencyTemplate
			});
		};

		var indexToRemoveFrequency;
		/*
		 * Function which display warning message whenever user tries to delete a row from "disable on" option
		 */
		var removeFrequencyWarning = function(btn) {
			indexToRemoveFrequency = frequencyForms.indexOf(btn.ownerCt.ownerCt.ownerCt.ownerCt);
			Ext.MessageBox.confirm('Confirm', 'Are you sure you want to remove this time constraint?', removeFrequencyTemplate);
		};

		/**
		 * Handler to remove template for "disable on" option
		 */
		var removeFrequencyTemplate = function(btn) {
			if (btn == 'yes') {
				frequencyCount --;
				if (frequencyCount == 0) {
					frequencyCount ++;
					Ext.MessageBox.show({
						title : 'Warning!',
						msg : 'Can not delete this time constraint! There must be atleast one constraint.',
						width : 300,
						buttons : Ext.MessageBox.OK,
						icon : Ext.MessageBox.WARNING
					});
					return;
				} else {
					var index = indexToRemoveFrequency;
					frequencyForms[index].ownerCt.remove(frequencyForms[index]);
					frequencyForms.splice(index, 1);
					fromFields.splice(index, 1);
					toFields.splice(index, 1);
					frequencyFields.splice(index, 1);

					win.doLayout();
				}
			}
		};

		/**
		 * Function which displays '-' button in "disable on" option
		 */
		var minusFrequencyButton = function() {
			return new Ext.Button({
				text : '-',
				width : 20,
				height : 20,
				handler : removeFrequencyWarning
			});
		};

		/*
		 * Function which adds a new row in "disable on" when user clicks on '+' button
		 */
		var getNewFrequencyForm = function() {
			frequencyCount ++;
			var frequencyForm = new Ext.FormPanel({
				border : false,
				labelAlign : 'top',
				frame : true,
				height : 55,
				bodyStyle : 'padding-left:10px;',
				items : [ {
					items : [ {
						rowWidth : .5,
						layout : 'column',
						hideBorders : true,
						bodyBorder: false,
						items : [ {
							columnWidth : 0.50,
							layout : 'form',
							items : [ frequencyGroup() ]
						}, {
							columnWidth : .20,
							layout : 'form',
							items : [ fromTimeField() ]
						}, {
							columnWidth : .20,
							layout : 'form',
							items : [ toTimeField() ]
						}, {
							columnWidth : .05,
							layout : 'form',
							items : [ plusFrequencyButton() ]
						}, {
							columnWidth : .05,
							layout : 'form',
							items : [ minusFrequencyButton() ]
						} ]
					} ]
				} ]
			});

			frequencyForms[frequencyCount] = frequencyForm;
			return frequencyForm;
		};

		var flagCommunication = -1;

		var radios = new Ext.form.RadioGroup({
			columns : 8,
			items : [ {
				boxLabel : 'E-Mail',
				name : 'communication',
				inputValue : 1
			}, {
				boxLabel : 'Nagios',
				name : 'communication',
				inputValue : 2
			} ],
			listeners : {
				change : function(ct, val) {
					var radiosValue = radios.getValue();

					if (ct.getValue().getRawValue() == 1) {
						emailTextField.setVisible(true);
						flagCommunication = 0;
					} else {
						flagCommunication = 1;
						emailTextField.setVisible(false);
					}
				}
			}

		});

		emailTextField.setVisible(false);

		var communicationForm = new Ext.FormPanel({
			border : false,
			labelAlign : 'top',
			frame : true,
			height : 55,
			//id			: 'id-frequency',
			bodyStyle : 'padding-left:10px;',
			items : [ {
				items : [ {
					rowWidth : .5,
					layout : 'column',
					items : [ {
						columnWidth : 0.50,
						layout : 'form',
						items : [ radios ]
					}, {
						columnWidth : .20,
						layout : 'form',
						items : [ emailTextField ]
					} ]
				} ]
			} ]
		});

		/**
		 * Function to save rules in DB
		 */
		function saveRules(btn) {

			if (!nameField.validate()) {
				Ext.Msg.alert('Missing Field', 'Please specify Rule Name');
				return;
			}

			if (!sourceField.validate()) {
				Ext.Msg.alert('Missing Field', 'Please specify a source to load streams');
				return;
			}

			for ( var i = 1; i <= ruleCount; ++ i) {
				if ((!streamBoxes[i].validate()) || (!timeFrameBoxes[i].validate()) || (!notificationBoxes[i].validate())
						|| (!isAreBoxes[i].validate()) || (!slopeBoxes[i].validate()) || (!numberBoxes[i].validate())) {
					Ext.Msg.alert('Missing Field', 'Please specify parameter for Rule');
					return;
				}
			}

			if (flagCommunication == -1) {
				Ext.Msg.alert('Missing Field', 'Please specify communication medium');
				return;
			}

			if (flagCommunication == 0) {
				if ((!emailTextField.validate())) {
					Ext.Msg.alert('Missing Field', 'Please specify at least one recipient');
					return;
				}
			}

			for ( var i = 1; i <= frequencyCount; ++ i) {
				if (frequencyFields[i].getValue().length != 0) {
					if ((!fromFields[i].validate()) || (!toFields[i].validate())) {
						Ext.Msg.alert('Missing Field', 'Please specify time constraints');
						return;
					}
				}
			}

			var selectedDays = new Array();

			selectedDays = frequencyFields[1].getValue();
			var checkedDays = new Array();
			for (i = 0; i < selectedDays.length; ++ i) {
				checkedDays[i] = selectedDays[i].getName();
			}

			if (selectedDays.length == 0) {
				checkedDays = null;
			}

			if (flagCommunication == 0) {
				var emailField = Ext.getCmp('id-email');
				Ext.Ajax.request({
					url : 'assertion.htm',
					method : 'POST',
					params : {
						TimeFrameIndex : timeFrameBoxes[1].getValue(), // TimeFrameIndex is the selected option of the TimeFrameaList
						NotificationIndex : notificationBoxes[1].getValue(), // NotificationIndex is the selected option of the NotificationList
						isAreIndex : isAreBoxes[1].getValue(),
						slopeIndex : slopeBoxes[1].getValue(),
						totalRule : 1,
						numberValue : numberBoxes[1].getValue(),
						ruleName : nameField.getRawValue(),
						source : sourceField.getRawValue(),
						stream : streamBoxes[1].getRawValue(),
						selectedDays : checkedDays,
						startHour : fromFields[1].getValue(),
						endHour : toFields[1].getValue(),
						communicationVia : flagCommunication,
						recipents : emailField.getValue()
					},
					scope : this,
					callback : saveOtherRules
				});
			} else {
				Ext.Ajax.request({
					url : 'assertion.htm',
					method : 'POST',
					params : {
						TimeFrameIndex : timeFrameBoxes[1].getValue(), // TimeFrameIndex is the selected option of the TimeFrameaList
						NotificationIndex : notificationBoxes[1].getValue(), // NotificationIndex is the selected option of the NotificationList
						isAreIndex : isAreBoxes[1].getValue(),
						slopeIndex : slopeBoxes[1].getValue(),
						totalRule : 1,
						numberValue : numberBoxes[1].getValue(),
						ruleName : nameField.getRawValue(),
						source : sourceField.getRawValue(),
						stream : streamBoxes[1].getRawValue(),
						selectedDays : checkedDays,
						startHour : fromFields[1].getValue(),
						endHour : toFields[1].getValue(),
						communicationMedium : flagCommunication
					},
					scope : this,
					callback : saveOtherRules
				});
			}
		}
		;

		function saveOtherRules() {
			for ( var i = 2; i <= ruleCount; ++ i) {
				Ext.Ajax.request({
					url : 'assertion.htm',
					method : 'POST',
					params : {
						TimeFrameIndex : timeFrameBoxes[i].getValue(), // TimeFrameIndex is the selected option of the TimeFrameaList
						NotificationIndex : notificationBoxes[i].getValue(), // NotificationIndex is the selected option of the NotificationList
						isAreIndex : isAreBoxes[i].getValue(),
						stream : streamBoxes[i].getRawValue(),
						slopeIndex : slopeBoxes[i].getValue(),
						totalRule : i,
						numberValue : numberBoxes[i].getValue()
					//ruleName			: nameField.getValue()
					},
					scope : this
				});
			}

			for ( var i = 2; i <= frequencyCount; ++ i) {
				var otherSelectedDays = new Array();
				otherSelectedDays = frequencyFields[i].getValue();

				var checkedOtherDays = new Array();
				for (j = 0; j < otherSelectedDays.length; ++ j) {
					checkedOtherDays[j] = otherSelectedDays[j].getName();
				}

				Ext.Ajax.request({
					url : 'assertion.htm',
					method : 'POST',
					params : {
						selectedDays : checkedOtherDays,
						startHour : fromFields[i].getValue(),
						endHour : toFields[i].getValue()
					},
					scope : this
				});
			}

			Ext.MessageBox.alert('Status', 'Screen saved successfully.');
		}
		;

		function getNewLabel(text) {
			return {
				xtype : 'box',
				autoEl : {
					cn : text
				}
			};
		}

		function closeWindow(btn) {

			win.close();
		}

		var win = new Ext.Window({
			title : 'Cockpit',
			width : 1250,
			border : 'false',
			height : 770,
			id : 'win',
			name : 'win',
			resizable   : false,
			//style				: 'margin:0 auto;margin-top:100;',
			bodyStyle : 'background-color:#fff;padding: 10px',
			autoScroll : true,
			items : [ {
				items : [ nameTextFieldContainer, getNewRuleForm(), getNewLabel('<br/><b><font size="3"> Disabled on</font><font size="2"> &nbsp;(24 hour format)</font></b>'),
							getNewFrequencyForm(), getNewLabel('<br/><b><font size="3"> Communication Via</font></b>'), communicationForm ]
			} ],
			buttonAlign : 'right', // buttons aligned to the right
			buttons : [ {
				text 	: 'Save',
				iconCls : 'save',
				handler : saveRules
			}, {
				text 	: 'Cancel',
				iconCls : 'cancel',
				handler : closeWindow
			} ]
		// buttons of the form
		});
		win.show();
	});
</script>
</body>
</html>