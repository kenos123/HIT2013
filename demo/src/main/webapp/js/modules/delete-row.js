//Calling define with a dependency array and a factory function
define(['core/mediator'] , function (m) {
	
    _deleteRow = function(e) {
    	e.preventDefault();
       var url = $(this).parent("form").attr("action");
       var options = {
    		   'method':'DELETE',
    		   'url':url,
    		   'success':_success.bind(e)
       }
       m.dom.ajax(options);
    };
    
    _success = function(e){
    	var row = $(this.currentTarget).closest("tr");
    	if(e.statusCode==200) {
        m.publish("ajax-success",e);
    	m.dom.hide(row);
    	}
    }

    //Define the module value by returning a value.
    return  {  
      init : function() {
    	var node = this.init.arguments[0].node;
        $(node).on("click", _deleteRow);
      }
    };
});