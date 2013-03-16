//Calling define with a dependency array and a factory function
define(['jquery'] , function ($) {
    _deleteEvent = function(e) {
       var $li = $(this).parents("li");
       $li.hide();
    };

    //Define the module value by returning a value.
    return  {  
      init : function() {
    	var node = this.init.arguments[0].node;
        var $lis = $(node).find("li");
        $lis.append('&nbsp;<span class="delete">&#10006;</span>');
        $(node).on("click", ".delete", _deleteEvent);
      }
    };
});