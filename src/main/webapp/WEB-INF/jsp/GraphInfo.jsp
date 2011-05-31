<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <%@ include file="../../libraries.html" %>
  <title> Cockpit </title>
</head>
<script type="text/javascript">

Ext.onReady(function(){

    Ext.QuickTips.init();

    // turn on validation errors beside the field globally
    Ext.form.Field.prototype.msgTarget = 'side';

    var bd = Ext.getBody();

    var duration =  new Ext.form.TextField ({
    	border			: false,
      	fieldLabel		: 'Duration <br />(09-23-59)',
      	regex			: /^([0][0-9])-([0-1][0-9]|[2][0-3])-([0-5][0-9])$/,
      	regexText		: 'Invalid format. Enter duration in days-hours-minutes format',
      	disabled		: false,
      	anchor			: '50%',
      	labelPad		: 18,
      	emptyText		: 'days-hours-minutes',
      	allowBlank		: false
      });

    var compareWith = new Ext.data.SimpleStore({
        fields		: ['id', 'value'],
        data 		: [['1','Recorded Day'],['2','Past Day']]
    });

    var compareWithCombo = new Ext.form.ComboBox({
    	store				: compareWith,
    	mode				: 'local',
    	forceSelection 		: true,
    	allowBlank 			: false,
    	fieldLabel			: 'Compare with',
    	name				: 'compareWith',
    	anchor				: '50%',
    	displayField		: 'value',
    	valueField			: 'id',
    	listeners: {
    		select: function(f,record,index){
    			//Ext.Msg.alert('Title',i);
    			}
        }
    });

    var fromDate =  new Ext.form.DateField ({
    	fieldLabel		: 'from',
    	itemId			: 'id-from',
    	name			: 'fromdate',
    	width			: 100,
    	allowBlank		: false,
    	maxValue 		: new Date()
    });

    var toDate =  new Ext.form.DateField ({
    	fieldLabel		: 'to',
    	id				: 'id-to',
    	name			: 'todate',
        width			: 100,
        allowBlank		: false,
        maxValue 		: new Date()
	});


    var compareDate =  new Ext.form.TextField ({
    	border			: false,
      	fieldLabel		: 'Compare with',
      	//regex			: /^([0][0-9])-([0-1][0-9]|[2][0-3])-([0-5][0-9])$/,
      	//regexText		: 'Invalid format. Enter duration in days-hours-minutes format',
      	disabled		: false,
      	anchor			: '50%',
      	labelPad		: 18,
      	emptyText		: 'year-month-date(2011-05-17)',
      	allowBlank		: false
      });


    var onlineInfo = new Ext.form.FieldSet ({
    	checkboxToggle	: true,
        title			: 'Online',
        autoHeight		: true,
        defaults		: {width: 210},
        defaultType		: 'textfield',
        collapsed		: false,
        items :[
               	duration, compareWithCombo
        	   ]

    });

    var offlineInfo = new Ext.form.FieldSet ({
    	checkboxToggle	: true,
        title			: 'Offline',
        autoHeight		: true,
        defaults		: {width	: 175},
        collapsed		: true,
        items :[
                fromDate, toDate, compareDate
        	]
    });

    var saveButton = function()
    {
		alert(Ext.getCmp('id-to').getValue().format('Y-m-d'));
    };

    var mode = new Ext.FormPanel({
        labelWidth		: 75, // label settings here cascade unless overridden
        frame			: true,
        title			: 'Select Mode',
        bodyStyle		: 'padding:5px 5px 0',
        width			: 550,
        items: [
				onlineInfo, offlineInfo
               ],

        buttons: [{
            text		: 'Save',
            handler		: saveButton
        },{
            text: 'Cancel'
        }]
    });

    mode.render(Ext.getBody());
});

</script>
</body>
</html>