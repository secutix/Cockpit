<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <%@ include file="../../libraries.html" %>
  <title>Cockpit</title>
</head>

<script type="text/javascript">

Ext.onReady(function(){

    Ext.QuickTips.init();

    // turn on validation errors besabide the field globally
    Ext.form.Field.prototype.msgTarget = 'side';

    var bd = Ext.getBody();

    /*
     * ================  Simple form  =======================
     */
    // bd.createChild({tag: 'h2', html: 'Form 1 - V kaery Simple'});

    var criteriaBoxes = new Array();		// array to store criteria comboBox for different rules
    var timeFrameBoxes = new Array();		// array to store timeframe comboBox for different rules
    var notificationBoxes = new Array();	// array to store notification comboBox for different rules
    var isAreBoxes = new Array();			// array to store isAre comboBox for different rules
    var slopeBoxes = new Array();			// array to store slope comboBox for different rules
    var numberBoxes = new Array();
    var frequencyForms = new Array();
    var ruleForms = new Array();

    var ruleCount = 0;

    var fromFields = new Array();
    var toFields = new Array();
    var frequencyFields = new Array();

    var frequencyCount = 0;

    /*var nameTextField = new Ext.form.TextField({
      border			: false,
      fieldLabel		: 'Name',
        name			: 'first',
        id				: 'id-name',
        emptyText		: 'Enter Rule Name',
        allowBlank		:  false
    });*/

    var dropDownRules = new Array();
    var r = 0;

    // Using JSTL, list of existing rule name is passed to display ir to user
    <c:forEach items="${ruleNames}" var="ruleNames">
      dropDownRules[r] = new Array();
      dropDownRules[r][0] = r.toString();
      dropDownRules[r][1] = "<c:out value="${ruleNames}"/>";
      r ++;
   </c:forEach>

    var rules = new Ext.data.SimpleStore({
                    fields	: ['id', 'ruleNames'],
                    data	: dropDownRules 									// multi-dimensional array
    });

    // ComboBox which contains all the available rules as well as able to store new rule name.
    var RuleList = function()
    {
      nameField = new Ext.form.ComboBox({
                        store			: rules,
                        mode			: 'local',
                        forceSelection 	: false,
                        allowBlank 		: false,
                        fieldLabel		: 'Name',
                        name			: 'rules',
                        anchor			: '85%',
                        displayField	: 'ruleNames',
                        emptyText		: 'Enter Rule Name',
                        valueField		: 'id',
                        listeners: {
                          select: function(f,record,index){
                            showExistingScreen();
                           }
                       }
                    });

      return nameField;
    };

    // Number Field to store information about the rule
    var numberField = function()
    {
      numberBoxes[ruleCount] = new Ext.form.NumberField({
                      fieldLabel		: '',
                      name				: 'number',
                      emptyText			: '',
                      allowNegative		: false,
                      //fieldLabel		: '%',
                      anchor			: '50%',
                      allowBlank		: false,
                      valueField 		: 'number'
                  });

      return numberBoxes[ruleCount];
    };

    /**
	 * Function to load the existing screen when user selects existing rule
	 */
    var showExistingScreen = function () {

	    Ext.Ajax.request({
	        url						: 'assertion.htm',
	        method					: 'POST',
	        params: {
	           existingRule			: nameField.getRawValue()
	            },
	        scope					: this,

	        success: function (response) {
	          var existingValues = Ext.util.JSON.decode(response.responseText);		// parameters are passed from controller as a JSON Object

	          for (var i = ruleCount; i > 1; --i){
	            ruleForms[i].ownerCt.remove(ruleForms[i]);
	                ruleForms.splice(i,1);
	                ruleCount--;
	          }

	          for (var i = 0; i < existingValues.existingCriteria.length; ++i) {
	        	  if (ruleCount < i+1) {
	        		  addRuleTemplate(null);
        		  }

	        	  criteriaBoxes[i+1].setValue(existingValues.existingCriteria[i]);
	        	  timeFrameBoxes[i+1].setValue(existingValues.existingTimeFrame[i]);
	        	  notificationBoxes[i+1].setValue(existingValues.existingNotification[i]);

	        	  if (existingValues.existingMinDelta[i] == null && existingValues.existingMaxDelta[i] == null) {
	        		  isAreBoxes[i+1].setValue(1);
        		  } else {
        			  isAreBoxes[i+1].setValue(2);
       			  }

	        	  if ((existingValues.existingMinDelta[i] != null || existingValues.existingMinVal[i] != null) &&
	        			  (existingValues.existingMaxDelta[i] != null || existingValues.existingMaxVal[i] != null)) {
	        		  slopeBoxes[i+1].setValue(2);
        		  } else if (existingValues.existingMinDelta[i] != null || existingValues.existingMinVal[i] != null) {
        			  slopeBoxes[i+1].setValue(3);
       			  } else {
       				  slopeBoxes[i+1].setValue(1);
   				  }

	        	  if (existingValues.existingMinDelta[i] != null) {
	        		  numberBoxes[i+1].setValue(existingValues.existingMinDelta[i]);
        		  } else if (existingValues.existingMaxDelta[i] != null) {
        			  numberBoxes[i+1].setValue(existingValues.existingMaxDelta[i]);
       			  } else if (existingValues.existingMinVal[i] != null) {
      				  numberBoxes[i+1].setValue(existingValues.existingMinVal[i]);
   				  } else {
   					  numberBoxes[i+1].setValue(existingValues.existingMaxVal[i]);
				  }
          	   }

	          for (var i = frequencyCount; i > 1; --i){
		            frequencyForms[i].ownerCt.remove(frequencyForms[i]);
		            frequencyForms.splice(i,1);
		            frequencyCount--;
	          }

	          for (var i = 0; i < existingValues.monday.length; ++i) {
	        	  if (frequencyCount < i+1) {
	        		  addFrequencyTemplate(null);
        		  }

	        	  if(existingValues.monday[i] == 1) {
	        		  frequencyFields[i+1].setValue({
						1 : true
	        		  });
	        	  }
	        	  if(existingValues.tuesday[i] == 1) {
	        		  frequencyFields[i+1].setValue({
						2 : true
	        		  });
	        	  }
	        	  if(existingValues.wednesday[i] == 1) {
	        		  frequencyFields[i+1].setValue({
						3 : true
	        		  });
	        	  }
	        	  if(existingValues.thursday[i] == 1) {
	        		  frequencyFields[i+1].setValue({
						4 : true
	        		  });
	        	  }
	        	  if(existingValues.friday[i] == 1) {
	        		  frequencyFields[i+1].setValue({
						5 : true
	        		  });
	        	  }
	        	  if(existingValues.saturday[i] == 1) {
	        		  frequencyFields[i+1].setValue({
						6 : true
	        		  });
	        	  }
	        	  if(existingValues.sunday[i] == 1) {
	        		  frequencyFields[i+1].setValue({
						7 : true
	        		  });
	        	  }

	        	  var str1 = existingValues.startHour[i];
	        	  str1 += "";					// convert int to string
	        	  var str2 = existingValues.startMin[i];
	        	  str2 += "";
	        	  fromFields[i+1].setValue(str1.concat(":", str2));
	        	  str1 = existingValues.endHour[i];
	        	  str1 += "";
	        	  str2 = existingValues.endMin[i];
	        	  str2 += "";
	        	  toFields[i+1].setValue(str1.concat(":", str2));
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

	// When Email as a communication mode is selected this field will be displayed for recipents address
    var emailTextField = new Ext.form.TextField({
    	border			: false,
    	fieldLabel		: 'Email',
    	vtype			: 'email',
    	name			: 'email',
    	id				: 'id-email',
        emptyText		: 'Enter Email id',
        anchor	     	: '80%',
        allowBlank		: false
    });

    var nameTextFieldContainer = {
    		xtype			: 'fieldset',
            flex        	: 1,
            border      	: false,
            hideBorders 	: false,
            autoHeight 	    : true,
            labelWidth  	: 50,
            height  	  	: 42,
            defaultType 	: 'field',
            defaults    	: {
	            anchor 	    	: '20%',
	            allowBlank 		: false,
	            border			: 'false'
            },
            items : [
          RuleList()
            ]
       };

    var dropDownCriteria = new Array();
    var i = 0;

    // Existing criteria values are passed after fetching their values from database
   <c:forEach items="${criterias}" var="criterias">
   	dropDownCriteria[i] = new Array();
    dropDownCriteria[i][0] = i.toString();
    dropDownCriteria[i][1] = "<c:out value="${criterias}"/>";
    i ++;
   </c:forEach>

    var criterias = new Ext.data.SimpleStore({
                    fields	: ['id', 'criteria'],
                    data	: dropDownCriteria 									// multi-dimensional array
    });

    /**
     * Function to create comboBox for criteria
     */
    var CriteriaList = function()
    {
      criteriaBoxes[ruleCount] = new Ext.form.ComboBox({
    	  store			: criterias,
    	  mode			: 'local',
    	  forceSelection: true,
    	  allowBlank 	: false,
    	  //fieldLabel	: 'Criterias',
    	  name			: 'criterias',
    	  anchor		: '85%',
    	  emptyText		: 'Select Your Criteria',
    	  displayField	: 'criteria',
    	  valueField	: 'id',
    	  listeners: {
    		  select: function(f,record,index){
    			  //CriteriaListIndex = index;
    			  }
      		}
      });

      return criteriaBoxes[ruleCount];
    };

    var dropDownTimeFrame = new Array();
    var j = 0;

   <c:forEach items="${frames}" var="frames">
      dropDownTimeFrame[j] = new Array();
      dropDownTimeFrame[j][0] = j.toString();
      dropDownTimeFrame[j][1] = "<c:out value="${frames}"/>";
      j ++;
   </c:forEach>

    var frames = new Ext.data.SimpleStore({
    	fields: ['id', 'frame'],
    	data: dropDownTimeFrame		// multi-dimensional array
    });

    /**
     * Function to create comboBox for timeFrame
     */
    var TimeFrameList = function()
    {
      timeFrameBoxes[ruleCount] = new Ext.form.ComboBox({
    	  store				: frames,
    	  mode				: 'local',
    	  forceSelection 	: true,
    	  allowBlank 		: false,
    	  //fieldLabel		: 'Frames',
    	  name				: 'frames',
    	  emptyText			: 'Select Time Frame',
    	  anchor			: '70%',
    	  displayField		: 'frame',
    	  valueField		: 'id',
    	  listeners: {
    		  select: function(f,record,index){
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
      dropDownNotificationLevel[k][1] = "<c:out value="${levels}"/>";
      k ++;
    </c:forEach>

    var levels = new Ext.data.SimpleStore({
    	fields: ['id', 'level'],
    	data: dropDownNotificationLevel		// multi-dimensional array
    });

    /**
     * Function to create comboBox for notificationLevel
     */
    var NotificationLevelList = function()
    {
      notificationBoxes[ruleCount] = new Ext.form.ComboBox({
    	  store				: levels,
    	  mode				: 'local',
    	  forceSelection 	: true,
    	  allowBlank 		: false,
    	  //fieldLabel		: 'Generates',
    	  name				: 'levels',
    	  emptyText			: 'Generates',
    	  anchor			: '70%',
    	  displayField		: 'level',
    	  valueField		: 'id',
    	  listeners: {
    		  select: function(f,record,index){
    			  NotificationLevelListIndex = index;
    			  //doNotificationUpdate(NotificationLevelListIndex);
    			  }
    	  }
       });

      return notificationBoxes[ruleCount];
    };

    var isAre = new Ext.data.SimpleStore({
        fields: ['id', 'value'],
        data : [['1','is/are'],['2','has slope']]
    });

    var isAreCombo = function()
    {
      isAreBoxes[ruleCount] = new Ext.form.ComboBox({
    	  store				: isAre,
    	  mode				: 'local',
    	  forceSelection 	: true,
    	  allowBlank 		: false,
    	  //fieldLabel		: 'Frames',
    	  name				: 'isAre',
    	  anchor			: '70%',
    	  displayField		: 'value',
    	  valueField		: 'id',
    	  listeners: {
    		  select: function(f,record,index){
    			  //Ext.Msg.alert('Title',i);
    			  }
	      }
      });

      return isAreBoxes[ruleCount];
    };

    var param = new Ext.data.SimpleStore({
        fields: ['id', 'value'],
        data : [['1','less than'],['2','equal to'],['3', 'greater than']]
    });


    var slopeCombo = function()
    {
      slopeBoxes[ruleCount] = new Ext.form.ComboBox({
    	  store			: param,
    	  mode			: 'local',
    	  forceSelection: true,
    	  allowBlank 	: false,
    	  //fieldLabel	: 'Frames',
    	  name			: 'slope',
    	  anchor		: '70%',
    	  displayField	: 'value',
    	  valueField	: 'id',
    	  listeners: {
    		  select: function(f,record,index){
    			  //Ext.Msg.alert('Title',i);
    			  }
       	  }
      });

      return slopeBoxes[ruleCount];
    };

    var param1 = new Ext.data.SimpleStore({
        fields: ['id', 'value'],
        data : [['1','stream 1'],['2','stream 2'],['3', 'stream 3']]
    });


    var streamCombo = function()
    {
      return new Ext.form.ComboBox({
    	  store			: param1,
    	  mode			: 'local',
    	  forceSelection: true,
    	  allowBlank 	: false,
    	  //fieldLabel	: 'Frames',
    	  name			: 'stream',
    	  emptyText		: 'Select Stream',
    	  anchor		: '70%',
    	  displayField	: 'value',
    	  valueField	: 'id',
    	  listeners: {
    		  select: function(f,record,index){
    			  //Ext.Msg.alert('Title',i);
    			  }
       	  }
      });
    };
   /**
    * Handler to add new rule template when user clicks on '+' button
    */
   var addRuleTemplate = function (btn) {
	   var index = ruleForms[ruleCount].ownerCt.items.indexOf(ruleForms[ruleCount]);
	   ruleForms[ruleCount].ownerCt.insert(index+1, getNewRuleForm());
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
	   /*Ext.Msg.show({
		   title			: 'Delete Rule',
		   msg				: 'Are you sure you want to delete this rule?',
		   buttons			: Ext.Msg.YESNO,
		   fn				: removeRuleTemplate
     });*/
   };

   /**
    * Function to delete new rule template when user confirms to delete a rule
    */
   var removeRuleTemplate = function (btn) {
	   if(btn == 'yes') {
		   var index = indexToRemove;
		   ruleForms[index].ownerCt.remove(ruleForms[index]);
		   ruleForms.splice(index,1);
		   ruleCount--;
		   win.doLayout();
	   }
   };

   /**
    * Function to add new rule template when user clicks on '+' button
    */
  var plusButton = function() {
	  return new Ext.Button ({
		  text		: '+',
		  width		: 20,
		  height	: 20,
		  handler 	: addRuleTemplate
	  });
   };

   var minusButton = function() {
	   return new Ext.Button ({
		   text		: '-',
		   width	: 20,
		   height	: 20,
		   handler 	: removeWarning
	   });
   };

   /**
    * Function responsible for adding new rule template
    */
   var getNewRuleForm = function() {
	   ruleCount ++;
	   var ruleForm = new Ext.FormPanel({
		   border		: false,
		   labelAlign	: 'top',
           //collapsible: true,
           frame		: true,
           //style		: {borderColor:'#000000', borderStyle:'solid', borderWidth:'0px'},
           //id			: 'id-ruleform',
           //bodyStyle	:'padding: 0.005px',
           //title: 'Assertion Screen',
           items: [{
             items: [{
               rowWidth: .5,
               layout:'column',
                   items:[{
                	   columnWidth		: .14,
                       layout			: 'form',
                       items			: [streamCombo()]
                   },{
                	   columnWidth		: .25,
                       layout			: 'form',
                       items			: [CriteriaList()]
                   },{
                	   columnWidth		: .10,
                       layout			: 'form',
                       items			: [isAreCombo()]
                   },{
                	   columnWidth		: .10,
                       layout			: 'form',
                       items			: [slopeCombo()]
                   },{
                	   columnWidth		: .06,
                       layout			: 'form',
                       items			: [numberField()]
                   },{
                	   columnWidth		: .15,
                       layout			: 'form',
                       items			: [TimeFrameList()]
                   },{
                	   columnWidth		: .10,
                       layout			: 'form',
                       items			: [NotificationLevelList()]
                   },{
                	   columnWidth		: .05,
                       layout			: 'form',
                       items			: [plusButton()]
                   },{
                	   columnWidth		: .05,
                       layout			: 'form',
                       items			: [minusButton()]
                   }]
             }]
           }]
       });

     ruleForms[ruleCount] = ruleForm;
     return ruleForm;
   };

   /**
    * Function to create checkBoxes for days
    */
   var frequencyGroup = function() {
	   frequencyFields[frequencyCount] = new Ext.form.CheckboxGroup({
		   //id			: 'myGroup',
		   xtype		: 'checkboxgroup',
		   //fieldLabel	: 'Single Column',
		   itemCls		: 'x-check-group-alt',
		   columns		: 8,
		   bodyStyle	: 'padding-bottom:20px;',
		   items: [
		           {boxLabel: 'MO', name: '1'},
		           {boxLabel: 'TU', name: '2'},
		           {boxLabel: 'WE', name: '3'},
		           {boxLabel: 'TH', name: '4'},
		           {boxLabel: 'FR', name: '5'},
		           {boxLabel: 'SA', name: '6'},
		           {boxLabel: 'SU', name: '7'}
		           ],
		           listeners:  {
		        	   change: function (ct, val) {
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
  var fromTimeField = function()
  {
    fromFields[frequencyCount] = new Ext.form.TextField ({
    	border			: false,
    	fieldLabel		: 'From',
    	regex			: /^([0-1][0-9]|[2][0-3]):([0-5][0-9])$/,
    	regexText		: 'Invalid format. Enter time in HH:MM',
    	disabled		: true,
    	emptyText		: 'HH:MM',
    	allowBlank		: false
    });

    return fromFields[frequencyCount];
  };

  /**
   * Function to create textField to store information about end timing when user disable a rule for particular days
   */
  var toTimeField = function()
  {
    toFields[frequencyCount] = new Ext.form.TextField ({
    	border			: false,
    	fieldLabel		: 'To',
    	regex			: /^([0-1][0-9]|[2][0-3]):([0-5][0-9])$/,
    	regexText		: 'Invalid format. Enter time in HH:MM',
    	disabled		: true,
    	emptyText		: 'HH:MM',
    	allowBlank		: false
   	});

    return toFields[frequencyCount];
  };

  /**
   * Handler to add template for "disable on" option
   */
  var addFrequencyTemplate = function (btn1) {
	  var index = frequencyForms[frequencyCount].ownerCt.items.indexOf(frequencyForms[frequencyCount]);
	  frequencyForms[frequencyCount].ownerCt.insert(index+1, getNewFrequencyForm());
	  win.doLayout();
   };

  /**
   * Function which displays '+' button in "disable on" option
   */
  var plusFrequencyButton = function()
   {
     return new Ext.Button ({
    	 text		: '+',
    	 width		: 20,
    	 height		: 20,
    	 handler 	: addFrequencyTemplate
     });
   };

   var indexToRemoveFrequency;
   /*
    * Function which display warning message whenever user tries to delete a row from "disable on" option
    */
   var removeFrequencyWarning = function(btn)
   {
     indexToRemoveFrequency = frequencyForms.indexOf(btn.ownerCt.ownerCt.ownerCt.ownerCt);
     Ext.MessageBox.confirm('Confirm', 'Are you sure you want to remove this time constraint?', removeFrequencyTemplate);
     /*Ext.Msg.show({
          title		: 'Delete Time Constraint',
          msg		: 'Are you sure you want to delete this row?',
          buttons	: Ext.Msg.YESNO,
          fn		: removeFrequencyTemplate
     });*/
   };

   /**
    * Handler to remove template for "disable on" option
    */
   var removeFrequencyTemplate = function (btn)
   {
     if(btn == 'yes') {
    	 var index = indexToRemoveFrequency;
    	 frequencyForms[index].ownerCt.remove(frequencyForms[index]);
    	 frequencyForms.splice(index,1);
    	 ruleCount--;
    	 win.doLayout();
     }
   };

   /**
    * Function which displays '-' button in "disable on" option
    */
   var minusFrequencyButton = function()
   {
     return new Ext.Button ({
       text		: '-',
       width	: 20,
       height	: 20,
       handler 	: removeFrequencyWarning
     });
   };

  /*
   * Function which adds a new row in "disable on" when user clicks on '+' button
   */
  var getNewFrequencyForm = function() {
    frequencyCount ++;
    var frequencyForm = new Ext.FormPanel({
      border		: false,
      labelAlign	: 'top',
      frame			: true,
      height 		: 55,
      //id			: 'id-frequency',
      bodyStyle		: 'padding-left:10px;',
      items: [{
        items: [{
          rowWidth		: .5,
          layout		: 'column',
          items	:[{
            //bodyStyle	: 'padding-top:-50px;',
            columnWidth	: 0.50,
            layout		: 'form',
            items		: [frequencyGroup()]
          },{
            //bodyStyle	: 'padding-bottom:50px;',
            columnWidth	: .20,
            layout		: 'form',
            items		: [fromTimeField()]
          },{
            //sbodyStyle	: 'padding-left:50px;',
            columnWidth	: .20,
            layout		: 'form',
            items		: [toTimeField()]
          },{
        	  columnWidth	: .05,
        	  layout		: 'form',
        	  items		 	: [plusFrequencyButton()]
          },{
        	  columnWidth	: .05,
        	  layout		: 'form',
        	  items			: [minusFrequencyButton()]
          }]
        }]
      }]
    });

    frequencyForms[frequencyCount] = frequencyForm;
    return frequencyForm;
  };

  var flagCommunication = -1;

  var radios = new Ext.form.RadioGroup({
     columns		: 8,
       items: [
             {boxLabel: 'E-Mail', name: 'communication', inputValue: 1},
             {boxLabel: 'Nagios', name: 'communication', inputValue: 2}
        ],
        listeners:  {
                 change: function (ct, val) {
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
    border		: false,
    labelAlign	: 'top',
    frame		: true,
    height 		: 55,
    //id			: 'id-frequency',
    bodyStyle	: 'padding-left:10px;',
    items: [{
      items: [{
        rowWidth		: .5,
        layout			: 'column',
        items	:[{
          //bodyStyle	: 'padding-bottom:50px;',
          columnWidth	: 0.50,
          layout		: 'form',
          items			: [radios]
        },{
          //bodyStyle	: 'padding-bottom:20px;',
          columnWidth	: .20,
          layout		: 'form',
          items			: [emailTextField]
        }]
      }]
    }]
  });

  /**
    * Function to save rules in DB
    */
   function saveRules(btn) {

    for (var i = 1; i <= ruleCount; ++i) {
       if ( (!criteriaBoxes[i].validate()) || (!timeFrameBoxes[i].validate()) || (!notificationBoxes[i].validate()) ||
           (!isAreBoxes[i].validate()) || (!slopeBoxes[i].validate()) || (!numberBoxes[i].validate()) || (!nameField.validate()) ) {
         Ext.Msg.alert('Missing Field', 'Please specify parameter for Rule');
         return;
       }
     }

    if (flagCommunication == -1) {
      Ext.Msg.alert('Missing Field', 'Please specify communication medium');
      return;
    }

    if (flagCommunication == 0) {
      if ( (!emailTextField.validate()) ) {
        Ext.Msg.alert('Missing Field', 'Please specify at least one recipient');
         return;
      }
    }

     for (var i = 1; i <= frequencyCount; ++ i) {
       if (frequencyFields[i].getValue().length != 0) {
         if ( (!fromFields[i].validate()) || (!toFields[i].validate()) ) {
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

     if(selectedDays.length == 0) {
       checkedDays = null;
     }

     if (flagCommunication == 0) {
       var emailField = Ext.getCmp('id-email');
       Ext.Ajax.request({
          url						: 'assertion.htm',
          method					: 'POST',
          params: {
        	  CriteriaIndex		: criteriaBoxes[1].getValue(),				// CriteriaListIndex is the selected option of the CriteriaList
       	      TimeFrameIndex	: timeFrameBoxes[1].getValue(),				// TimeFrameIndex is the selected option of the TimeFrameaList
        	  NotificationIndex	: notificationBoxes[1].getValue(),			// NotificationIndex is the selected option of the NotificationList
        	  isAreIndex		: isAreBoxes[1].getValue(),
        	  slopeIndex		: slopeBoxes[1].getValue(),
        	  totalRule			: 1,
        	  numberValue		: numberBoxes[1].getValue(),
           	  ruleName			: nameField.getRawValue(),
              selectedDays		: checkedDays,
	          startHour			: fromFields[1].getValue(),
	          endHour			: toFields[1].getValue(),
	          communicationVia	: flagCommunication,
	          recipents			: emailField.getValue()
              },
          scope					: this,
          callback				: saveOtherRules
         });
     } else {
       Ext.Ajax.request({
          url						: 'assertion.htm',
          method					: 'POST',
          params: {
        	  CriteriaIndex			: criteriaBoxes[1].getValue(),				// CriteriaListIndex is the selected option of the CriteriaList
              TimeFrameIndex		: timeFrameBoxes[1].getValue(),				// TimeFrameIndex is the selected option of the TimeFrameaList
              NotificationIndex		: notificationBoxes[1].getValue(),			// NotificationIndex is the selected option of the NotificationList
              isAreIndex			: isAreBoxes[1].getValue(),
              slopeIndex			: slopeBoxes[1].getValue(),
              totalRule				: 1,
              numberValue			: numberBoxes[1].getValue(),
              ruleName				: nameField.getRawValue(),
              selectedDays			: checkedDays,
              startHour				: fromFields[1].getValue(),
              endHour				: toFields[1].getValue(),
              communicationMedium	: flagCommunication
              },
          scope					: this,
          callback				: saveOtherRules
         });
     }
  };

  function saveOtherRules() {
    for (var i = 2; i <= ruleCount; ++i) {
       Ext.Ajax.request({
           url: 'assertion.htm',
           method: 'POST',
           params: {
             CriteriaIndex		: criteriaBoxes[i].getValue(),				// CriteriaListIndex is the selected option of the CriteriaList
             TimeFrameIndex		: timeFrameBoxes[i].getValue(),				// TimeFrameIndex is the selected option of the TimeFrameaList
             NotificationIndex	: notificationBoxes[i].getValue(),			// NotificationIndex is the selected option of the NotificationList
             isAreIndex			: isAreBoxes[i].getValue(),
             slopeIndex			: slopeBoxes[i].getValue(),
             totalRule			: i,
             numberValue		: numberBoxes[i].getValue()
             //ruleName			: nameField.getValue()
               },
           scope: this
           });
     }

    for (var i = 2; i <= frequencyCount; ++i) {
      var otherSelectedDays = new Array();
      otherSelectedDays = frequencyFields[i].getValue();
      var checkedOtherDays = new Array();
       for (j = 0; j < otherSelectedDays.length; ++ j) {
         checkedOtherDays[j] = otherSelectedDays[j].getName();
       }

       Ext.Ajax.request({
           url		: 'assertion.htm',
           method	: 'POST',
           params	: {
        	   selectedDays		: checkedOtherDays,
        	   startHour		: fromFields[i].getValue(),
        	   endHour			: toFields[i].getValue()
       	   },
           scope: this
       });
    }

  };

   function getNewLabel (text)
   {
     return {
          xtype: 'box',
          autoEl: {cn: text}
         };
   }

   function closeWindow (btn)
   {
	   win.close();
   }
   var win = new Ext.Window({
      title		: 'Cockpit',
      width		: 1300,
      border	: 'false',
      height	: 770,
      id		: 'win',
      name		: 'win',
      bodyStyle	: 'background-color:#fff;padding: 10px',
      autoScroll  : true,
      items: [{
          items: [
                  nameTextFieldContainer, getNewRuleForm(), getNewLabel('<br/><b><font size="3">Disabled on</font></b>'),
                  getNewFrequencyForm(),getNewLabel('<br/><b><font size="3">Communication Via</font></b>'), communicationForm
                 ]

        }],
      buttonAlign	: 'right', 											// buttons aligned to the right
      buttons		:[{
              text		: 'Save',
              handler 	: saveRules
            },{
              text		: 'Cancel',
              handler	: closeWindow
            }] 													// buttons of the form
  });

   win.show();
});

	</script>
  </body>
</html>