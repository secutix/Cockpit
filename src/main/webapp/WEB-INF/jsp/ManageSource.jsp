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

    Ext.form.Field.prototype.msgTarget = 'side';

    var bd = Ext.getBody();
    var AddURLWindow = null;

    var nameTextField = function (){
    	return new Ext.form.TextField({
			border			: false,
			fieldLabel		: 'Name',
			name			: 'first',
			//    id				: 'id-name',
			emptyText		: 'Entesr URL',
			anchor			: '100%',
			width			: '400',
			allowBlank		:  false
		});
    };

    var descritptionTextField = function() {
    	return new Ext.form.TextField({
			border			: false,
			fieldLabel		: 'Descritption',
			name			: 'first',
			//    id				: 'id-name',
			emptyText		: 'Remark',
			anchor			: '100%',
			width			: '400',
			allowBlank		:  false
		});
    };

    var urlContainer = function() {
    	return new Ext.form.FieldSet ({
    		xtype			: 'fieldset',
    		flex        	: 1,
    		border      	: false,
    		hideBorders 	: false,
    		autoHeight 	: true,
    		labelWidth  	: 70,
    		height  	  	: 42,
    		defaultType 	: 'field',
    		defaults    	: {
    			anchor     	: '100%',
    			allowBlank 	: false,
    			border		: 'false'
	  	      },
	  	      items : [
	  	               nameTextField(), descritptionTextField()
	  	              ]
  	      });
    };

    var getNewAddURLWindow = function() {
    	var win =  new Ext.Window ({
	    	title		: 'Monitoring URL',
	    	width		: 550,
	    	border		: 'false',
	    	height		: 160,
	    	bodyStyle	: 'background-color:#fff;padding: 5px',
	    	autoScroll  : true,
	    	closable: false,
	    	items: [
	    		urlContainer()
	    		],
			buttonAlign	: 'right', 											// buttons aligned to the right
			buttons		:[{
	      		text		: 'Add URL'
	           //handler 	: saveRules
	        },{
	          text		: 'Cancel',
	          handler	: closeWindow
	        }] 													// buttons of the form
		});
    	return win;
    };


    /*function saveRules (btn)
    {
    	if (AddURLWindow != null) {

    		//Save Rules here

    		AddURLWindow.hide();
    	}
    	//AddURLWindow = null;
    }*/

    function closeWindow (btn) {
    	if (AddURLWindow != null) {
    		AddURLWindow.close();
    	}
    	AddURLWindow = null;
    }

    var getURL = function (btn) {
    	if (AddURLWindow == null) {
    		AddURLWindow = getNewAddURLWindow();
    		AddURLWindow.show();
    	}
    };

    var addButton = function() {
    	return	new Ext.Button ({
    		text		: 'Add',
    		bodyStyle	: 'padding-bottom:1110px;',
    		width		: 80,
        	id			: 'add',
        	listeners: {
        		click: function() {
        			getURL();
        		}
        	}
        });
    };

   var deleteButton = function() {
    	return	new Ext.Button ({
    		text		: 'Delete',
    		bodyStyle	: 'padding-bottom:1110px;',
    		width		: 80,
        	id			: 'del'
        });
    }

    var manageSource = new Ext.FormPanel({
    	border		: false,
    	labelAlign	: 'top',
        frame		: true,
        bodyStyle	: 'padding-left:10px;',
        items: [{
          items: [{
            rowWidth	: .5,
            layout		:'column',
                items:[{
                	columnWidth		: .05,
                    layout			: 'form',
                    items			: [addButton()]
                },{
             	    columnWidth		: .05,
                    layout			: 'form',
                    items			: [deleteButton()]
                }]
          }]
        }]
    });

    manageSource.render(Ext.getBody());

    var store = new Ext.data.Store ({
        remoteSort	: true,
        //baseParams	: {lightWeight:true, ext: 'js'},
        //sortInfo	: {field:'lastpost', direction:'DESC'},
        autoLoad	: { params:
        					{start:0, limit:2}
        			  },

        proxy	: new Ext.data.ScriptTagProxy({
            //url: 'http://extjs.com/forum/topics-browse-remote.php'
        	url: 'recentActivity.htm'
        }),

        reader	: new Ext.data.JsonReader({
            root			: 'topics',
            totalProperty	: 'totalCount',
            idProperty		: 'threadid',
            fields: [
                'title', 'forumtitle', 'forumid', 'author',
                {name: 'replycount', type: 'int'},
                {name: 'lastpost', mapping: 'lastpost', type: 'date', dateFormat: 'timestamp'},
                'lastposter', 'excerpt'
            ]
        })
    });


    var grid = new Ext.grid.GridPanel({
        renderTo			: Ext.getBody(),
        width				: 700,
        height				: 500,
        frame				: true,
        title				: 'Available Sources',
        trackMouseOver		: false,
    	autoExpandColumn	: 'topic',
        store				: store,

        columns: [new Ext.grid.RowNumberer({width: 30}),{
            id			: 'topic',
            header		: "Date",
            dataIndex	: 'title',
            width		: 420,
            renderer	: renderTopic,
            sortable	: true
        },{
            header		: "Type",
            dataIndex	: 'replycount',
            width		: 70,
            align		: 'right',
            sortable	: true
        },{
            id			: 'last',
            header		: "Description",
            dataIndex	: 'lastpost',
            width		: 150,
            renderer	: renderLast,
            sortable	: true
        }],

      bbar: new Ext.PagingToolbar({
        store			: store,
        pageSize		:2,
        displayInfo		:true
      }),


    });

    // render functions
    function renderTopic(value, p, record){
        return String.format(
                '<b><a href="http://extjs.com/forum/showthread.php?t={2}" target="_blank">{0}</a></b><a href="http://extjs.com/forum/forumdisplay.php?f={3}" target="_blank">{1} Forum</a>',
                value, record.data.forumtitle, record.id, record.data.forumid);
    }
    function renderLast(value, p, r){
        return String.format('{0}<br/>by {1}', value.dateFormat('M j, Y, g:i a'), r.data['lastposter']);
    }
});

</script>
</body>
</html>