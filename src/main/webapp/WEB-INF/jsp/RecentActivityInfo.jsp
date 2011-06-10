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

    function getNewLabel (text)
    {
      return {
           xtype	: 'box',
           autoEl	: {cn: text}
          };
    }

    var fromDate = function() {
    	return new Ext.form.DateField ({
    		fieldLabel		: 'Fraom',
	    	id				: 'id-from',
	    	name			: 'fromdate',
	    	width			: 130,
	    	allowBlank		: false,
	    	maxValue 		: new Date()
    	});
    };

    var toDate =  function() {
    	return new Ext.form.DateField ({
    		fieldLabel		: 'To',
	    	id				: 'id-to',
	    	name			: 'todate',
	        width			: 130,
	        allowBlank		: false,
	        maxValue 		: new Date()
		});
    };

    var saveButton = function()
    {
		alert(Ext.getCmp('id-to').getValue().format('Y-m-d'));
    };

    var getURL = function() {
    	var temp = Ext.getCmp('id-to').getValue().format('Y-m-d');
    	alert(temp);
    }

    var addButton = function() {
    	return	new Ext.Button ({
    		text		: 'OK',
    		//bodyStyle	: 'padding-bottom:1110px;',
    		width		: 80,
        	id			: 'add',
        	listeners: {
        		click: function() {
        			getURL();
        		}
        	}
        });
    };

    var durationForm = new Ext.FormPanel({
    	border		: false,
    	labelAlign	: 'top',
        frame		: true,
        bodyStyle	: 'padding-left:100px;',
        items: [{
          items: [{
            rowWidth	: .5,
            layout		:'column',
                items:[{
                	columnWidth		: .10,
                    layout			: 'form',
                    items			: [getNewLabel('<br/><font size="3">Duration: </font>')]
                },{
                	columnWidth		: .17,
                    layout			: 'form',
                    items			: [toDate()]
                },{
             	    columnWidth		: .17,
                    layout			: 'form',
                    items			: [fromDate()]
                },{
             	    columnWidth		: .17,
                    layout			: 'form',
                    items			: [addButton()]
                }]
          }]
        }]
    });

    durationForm.render(Ext.getBody());




    var store = new Ext.data.Store ({
        //remoteSort	: true,
        //baseParams	: {fromDate:Ext.getCmp('id-to').getValue().format('Y-m-d'), toDate:Ext.getCmp('id-to').getValue().format('Y-m-d')},
        //sortInfo	: {field:'lastpost', direction:'DESC'},
        autoLoad	: { params:
        					{start:0, limit:2}
        			  },

        proxy	: new Ext.data.HttpProxy({
            //url: 'http://extjs.com/forum/topics-browse-remote.php'
        	url: 'recentActivity.htm'
        }),

        reader	: new Ext.data.JsonReader({
            root			: 'topics',
            totalProperty	: 'totalCount',
            idProperty		: 'threadid',
            fields: [
				{name: 'Date', type:'date', dateFormat: 'timestamp'},
                {name: 'Type', type: 'string'},
                {name: 'Description', type: 'string'}
            ]
        })
    });

    store.on({
        'beforeload': {
            fn: function(store, options){
                //console.info('store beforeload fired, arguments:', arguments);
                options.params || (options.params = {}); //assert params
                Ext.apply(options.params, {
                    //apply stuff to params
                    //assuming pageNumber has been calculated into this var
                	fromDate:Ext.getCmp('id-from').getValue()?Ext.getCmp('id-from').getValue().format('Y-m-d'):null,
                	toDate:Ext.getCmp('id-to').getValue()?Ext.getCmp('id-to').getValue().format('Y-m-d'):null,
                });
            },
            scope: this
        }
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
            dataIndex	: 'Date',
            width		: 420,
            //renderer	: renderTopic,
            sortable	: true
        },{
            header		: "Type",
            dataIndex	: 'Type',
            width		: 70,
            align		: 'right',
            sortable	: true
        },{
            id			: 'last',
            header		: "Description",
            dataIndex	: 'Description',
            width		: 150,
            //renderer	: renderLast,
            sortable	: true
        }],

      bbar: new Ext.PagingToolbar({
        store			: store,
        pageSize		:2,
        displayInfo		:true
      }),


    });
              grid.render(Ext.getBody());
});

</script>
</body>
</html>