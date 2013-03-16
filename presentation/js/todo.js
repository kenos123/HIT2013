//Calling define with a dependency array and a factory function
define(['jquery'] , function (j) {
    _deleteEvent = function(e) {
       var li = j(this).parents("li");
       li.hide();
    };

    //Define the module value by returning a value.
    return  {  
      init : function() {
        var lis = j("#todo").find("li");
        lis.append('&nbsp;<span class="delete">&#10006;</span>');
        j("#todo").on("click", ".delete", _deleteEvent);
        console.log("TODO module initialized");
      }
    };
});