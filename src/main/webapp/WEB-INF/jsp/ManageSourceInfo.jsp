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

    var getUrlTextField = function (){
    	return new Ext.form.TextField({
			border			: false,
			fieldLabel		: 'Name',
			name			: 'first',
			//id			: 'id-name',
			emptyText		: 'Enter URL',
			anchor			: '100%',
			width			: '400',
			allowBlank		:  false
		});
    };

    var getDescritptionTextField = function() {
    	return new Ext.form.TextField({
			border			: false,
			fieldLabel		: 'Descritption',
			name			: 'first',
			//id			: 'id-name',
			emptyText		: 'Remark',
			anchor			: '100%',
			width			: '400',
			allowBlank		:  false
		});
    };

    var descritptionTextField, urlTextField;
    var urlContainer = function() {
    	descritptionTextField = getDescritptionTextField();
    	urlTextField = getUrlTextField();

    	return new Ext.form.FieldSet ({
    		xtype			: 'fieldset',
    		flex        	: 1,
    		border      	: false,
    		hideBorders 	: false,
    		autoHeight 		: true,
    		labelWidth  	: 70,
	  	      items : [
	  	               urlTextField, descritptionTextField
	  	              ]
  	      });
    };

    var getNewAddURLWindow = function() {
    	var win =  new Ext.Window ({
	    	title			: 'Monitoring URL',
	    	width			: 550,
	    	border			: 'false',
	    	height			: 160,
	    	bodyStyle		: 'background-color:#fff;padding: 5px',
	    	autoScroll  	: true,
	    	closable		: false,
	    	layout 			: 'fit',
	    	items			: [
	    		urlContainer()
	    	],
			buttonAlign	: 'right', 											// buttons aligned to the right
			buttons		:[{
				text		: 'Save',
				iconCls		: 'save',
	            handler 	: saveRules
	        },{
	        	text		: 'Cancel',
	        	iconCls		: 'cancel',
	        	handler		: closeWindow
	        }] 																// buttons of the form
		});

    	return win;
    };


    function saveRules (btn) {
    	if (AddURLWindow != null) {

    		if (!urlTextField.validate()) {
  		         Ext.Msg.alert('Missing Field', 'Please specify source url');
  		         return;
 		    }

    		if (!descritptionTextField.validate()) {
   		         Ext.Msg.alert('Missing Field', 'Please specify source description');
   		         return;
  		    }

    		Ext.Ajax.request({
                url					: 'manageSourceInfo.htm',
                method				: 'POST',
                params: {
              	  sourceUrl			: urlTextField.getValue(),
              	  sourceDescription : descritptionTextField.getValue()
               },
               failure: function( r, o ) {
            	   alert( "fail: " + r.responseText );
               },
               scope	: this,
               callback	:  function () {
            	   paging.doRefresh();
               }
            });

    		AddURLWindow.hide();
    	}

    	AddURLWindow = null;
    }

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

        proxy	: new Ext.data.HttpProxy ({
        	url	: 'manageSource.htm'
        }),

        reader	: new Ext.data.JsonReader({
            root			: 'topics',
            totalProperty	: 'totalCount',
            idProperty		: 'threadid',
            fields: [
				{name: 'url', type	: 'string'},
                {name: 'description', type	: 'string'}
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

    var removeSource = function(btn) {
    	 var selectedRow = grid.getSelectionModel().getSelected();
         if (selectedRow == undefined) {
        	 Ext.MessageBox.show ({
        		 title		: 'Warning!',
        		 msg		: 'Please select a source to delete!',
        		 width		: 275,
        		 buttons	: Ext.MessageBox.OK,
        		 icon		: Ext.MessageBox.WARNING
        	});
         } else {
             Ext.Ajax.request({
                 url				: 'manageSourceInfo.htm',
                 method				: 'POST',
                 params: {
                	 selectedSource	: selectedRow.get('url')
                },

                success: function (response) {
      	          var existRule = Ext.util.JSON.decode(response.responseText);
	      	      Ext.MessageBox.show ({
	      	    	  title		: 'Warning!',
	      	    	  msg		: 'Cannot delete selected source. It is already being used by ' + existRule.ruleName + '.',
	      	    	  width		: 260,
	      	    	  buttons	: Ext.MessageBox.OK,
	      	    	  icon		: Ext.MessageBox.WARNING
           		  });
                },

    			failure: function( r, o ) {
    				alert( "fail: " + r.responseText );
    			},

                scope	: this,
                callback				:  function () {
                	paging.doRefresh();
                }
         	});
          }
     };

     var paging = new Ext.PagingToolbar({
	  store			: store,
	  pageSize		: 10,
	  displayInfo	: true
     });

    var grid = new Ext.grid.GridPanel({
        renderTo			: Ext.getBody(),
        width				: 700,
        height				: 500,
        frame				: true,
        title				: 'Available Sources',
        trackMouseOver		: true,
    	autoExpandColumn	: 'topic',
    	style				: 'margin:0 auto;margin-top:100;',
        store				: store,
        selModel         : new Ext.grid.RowSelectionModel({singleSelect : true}),

        columns: [new Ext.grid.RowNumberer({width: 30}),{
            id			: 'topic',
            header		: "URL",
            dataIndex	: 'url',
            width		: 300,
            //renderer	: renderTopic,
            sortable	: true
        },{
            header		: "Description",
            dataIndex	: 'description',
            height		: 50,
            width		: 200,
            //align		: 'right',
			//renderer	: renderType,
            sortable	: true
        }],

        tbar:[{
        	text		: 'Add Source',
            //tooltip	: 'Add a new source',
            handler 	: getURL,
            iconCls		: 'add'
        }, '-', {
            text		: 'Remove Source',
            handler 	: removeSource,
            //tooltip	: 'Remove the selected source',
            iconCls		: 'remove'
        }],

      bbar: paging

    });

    grid.render(Ext.getBody());

});

</script>
</body>
</html>