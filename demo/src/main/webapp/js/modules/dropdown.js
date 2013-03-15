define(["core/mediator"], function(m){
	var _MODULE_NAME = "Dropdown";
	
	_init = function(options) {
		m.plugins.dropdown(m.dom.find("[data-module='dropdown']", this.node),options);
	};
	
	return {
		init : function(options) {
			console.log("INIT module " + _MODULE_NAME);
			m.subscribe("bootstrap", _init.bind(options));
			//_init(options);
		},
		
		destroy : function() {
			
		}
	};
});