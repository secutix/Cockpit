var showWelcomeScreen = function () {
	document.getElementById('body-frame').src = 'recentActivityinfo.htm';
}

var showManageSourceScreen = function () {
	document.getElementById('body-frame').src = 'manageSourceInfo.htm';
}

var showAssertionScreen = function () {
	document.getElementById('body-frame').src = 'assertion.htm';
}

var showSystemInfo = function () {
	document.getElementById('body-frame').src = 'system.htm';
}

Ext.onReady(function() {
	var masterPanel = new Ext.Panel({
		renderTo	: document.getElementById('header-div'),
		title    	: 'Monitoring Tool',
		width  	  	: document.getElementById("header-div").offsetWidth,
		height  	: document.getElementById("header-div").offsetHeight,
		border		: false,
		tbar: [{
			xtype		:'buttongroup',
			width    	: document.getElementById("header-div").offsetWidth,
            items	: [{
                text	: 'Activity',
                width 	: document.getElementById("header-div").offsetWidth/7,
                scale	: 'large',
                handler : showWelcomeScreen
            },{
                text	: 'Manage Source',
                width 	: document.getElementById("header-div").offsetWidth/7,
                scale	: 'large',
                handler : showManageSourceScreen
            },{
                text	: 'Alarm Monitoring',
                width 	: document.getElementById("header-div").offsetWidth/7,
                scale	: 'large',
                handler : showAssertionScreen
            },{
                text	: 'System',
                width 	: document.getElementById("header-div").offsetWidth/7,
               //iconCls: 'add24',
                scale	: 'large',
                handler : showSystemInfo
                //menu	: [{text: 'Paste Menu Item'}]
            }]
        }]
 	 });

 	 return masterPanel;
});