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

    var store = new Ext.data.Store ({
        autoLoad	: { params:
        					{start:0, limit:10}
        			  },

        proxy	: new Ext.data.HttpProxy({
        	url: 'manageSource.htm'
        }),

        reader	: new Ext.data.JsonReader({
            root			: 'topics',
            totalProperty	: 'totalCount',
            idProperty		: 'threadid',
            fields: [
				{name: 'url', type:'string'},
                {name: 'description', type: 'string'}
            ]
        })
    });

    store.on({
        'beforeload': {
            fn: function(store, options){
                options.params || (options.params = {}); //assert params
            },
            scope: this
        }
    });

    var grid = new Ext.grid.GridPanel({
        renderTo			: Ext.getBody(),
        width				: 700,
        height				: 500,
        frame				: true,
        title				: 'Manage',
        trackMouseOver		: true,
    	autoExpandColumn	: 'topic',
        store				: store,

        columns: [new Ext.grid.RowNumberer({width: 30}),{
            id			: 'topic',
            header		: "URL",
            dataIndex	: 'url',
            width		: 420,
            //renderer	: renderTopic,
            sortable	: true
        },{
            header		: "des",
            dataIndex	: 'description',
            width		: 70,
            align		: 'right',
			//renderer	: renderType,
            sortable	: true
        }],

        tbar:[{
        	text		: 'Add Something',
            tooltip		: 'Add a new source',
            handler 	: getURL,
            iconCls		: 'add'
        }, '-', {
            text		: 'Remove Source',
            tooltip		: 'Remove the selected source',
            iconCls		: 'remove'
        }],

      bbar: new Ext.PagingToolbar({
        store			: store,
        pageSize		: 10,
        displayInfo		: true
      }),

    });

    grid.getSelectionModel().on('rowselect', function(grid, rowIndex, r) {
    	Ext.Ajax.request({
            url						: 'manageSourceInfo.htm',
            method					: 'POST',
            params: {
          	  selectedUrl		 	: r.get('url')
           },
           scope	: this
    	});
    });

    grid.render(Ext.getBody());

});

</script>
</body>
</html>