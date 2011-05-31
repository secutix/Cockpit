var showAssertionScreen = function () {
	//var obj = document.getElementById('body-frame');
	document.getElementById('body-frame').src = 'assertion.htm';
}

var showSystemInfo = function () {
	//var obj = document.getElementById('body-frame');
	document.getElementById('body-frame').src = 'system.htm';
}

Ext.onReady(function() {
	var masterPanel = new Ext.Panel({
		renderTo	: document.getElementById('header-div'),
		title    	: 'Header',
		width  	  	: document.getElementById("header-div").offsetWidth,
		height  	: document.getElementById("header-div").offsetHeight,
		border		: false,
		tbar: [{
			xtype		:'buttongroup',
			width    	: document.getElementById("header-div").offsetWidth,
            items	: [{
                text	: 'Alarm Monitoring',
                width 	: document.getElementById("header-div").offsetWidth/6,
                scale	: 'large',
                handler : showAssertionScreen
            },{
                text	: 'Recorded Days',
                width 	: document.getElementById("header-div").offsetWidth/6,
                scale	: 'large'
            },{
                text	: 'System',
                width 	: document.getElementById("header-div").offsetWidth/6,
               //iconCls: 'add24',
                scale	: 'large',
                handler : showSystemInfo
                //menu	: [{text: 'Paste Menu Item'}]
            }]
        }]
 	 });

 	 return masterPanel;
});