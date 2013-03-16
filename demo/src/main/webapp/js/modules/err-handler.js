/**
 * module for handling errors in application and displaying them in specified container.
 */
define(["core/mediator"], function(m){
	"use strict";
	var _MODULE_NAME = "Err-handler";
	
	/*options can be overriden by inserting module params. Expected structure of container is 
	 * <containerNode><h4>here comes error header</h4><p> here comes error message</p></containerNode>
	 */
	var _options ={
			containerNode:".main-error-container"
	};
	
	/*
	 * Init function 
	 */
	var _init = function() {
		
		if(this.params){
				var pom = m.util.convertJSON(this.params);
				_options = m.util.extend(_options,m.util.parseJson(pom));
			}			
	},
	_handleAjaxError = function(data){
		console.log("Handling error");
		f.publish("clear-success");
		var	h = m.dom.find('h4',_options.containerNode),
			text = m.dom.find("p",_options.containerNode);
		if(data.messages !== undefined){
			var messageHtml = "";
			m.util.each(data.messages,function (i, item) {
				messageHtml += item.userMessage + "<br />";
			});
			m.dom.html(text,messageHtml);
			m.dom.html(h,data.responseStatus);
		} else {
			m.dom.html(h,"Error");
			m.dom.html(text, data.statusText);
		}
		document.location.href = '#';
		m.dom.effects.fadeIn(_options.containerNode);
	},
	_bootstrap = function(){
		_moveErrors();
	},
	_moveErrors = function() {
		var errors = m.dom.find(".errors:not(:empty)"); // not empty error blocks
		if(errors.length>0) { // we got some errors
			var mainErrorContainer=m.dom.find("#main-error-container");
			var errorsBlock = m.dom.find("p",mainErrorContainer);

			var errContHtml="";
			m.util.eachReversed(errors,function(i,item){
				 errContHtml += m.dom.html(item) + "<br />";
				 
			});
			m.dom.html(errorsBlock, errContHtml);
			m.dom.show(mainErrorContainer);
		}
	},
	_clearErrors = function(){
		var cancel = m.dom.find("[data-dismiss]", _options.containerNode);
		if(cancel.length > 0){
			m.dom.trigger(cancel, "click");
		} else {
			m.dom.hide(_options.containerNode);
		}
	};

	return {
		init : function(options) {	
			
			console.log("INIT module " + _MODULE_NAME);
			_init.bind(options);
			 
			//subscribtion to all error types and handler binding 
			m.subscribe("ajax-error", _handleAjaxError);
			m.subscribe("clear-errors", _clearErrors);
			m.subscribe("bootstrap", _bootstrap);
		},
		
		destroy : function() {
			
		}
	};
});