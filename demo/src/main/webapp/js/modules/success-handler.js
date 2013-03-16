/**
 * module for handling errors in application and displaying them in specified container.
 */
define(["core/mediator"], function(m){
	"use strict";
	var _MODULE_NAME = "Success-handler";
	
	/*options can be overriden by inserting module params. Expected structure of container is 
	 * <containerNode><h4>here comes error header</h4><p> here comes error message</p></containerNode>
	 */
	var _options ={
		containerNode:".main-success-container"
	},
	
	/*
	 * Init function 
	 */
	_init = function() {
		if(this.params){
			var pom = m.util.convertJSON(this.params);
			_options = m.util.extend(_options,f.util.parseJson(pom));
		}			
	},
	_handleAjaxSuccess = function(data){
		console.log("Handling success", data);
		m.publish("clear-errors");
		_clearSuccess();
		var h = m.dom.find('h4',_options.containerNode),
			text = m.dom.find('p',_options.containerNode);
		if(data.messages !== undefined){
			var messageHtml = "";
			m.util.each(data.messages,function (i, item) {
				if(m.util.is(item.userMessage) && item.userMessage != null) {
					messageHtml += item.userMessage + "<br />"; 
				}
			});
			if(messageHtml!=="") {
				m.dom.html(text,messageHtml);
				m.dom.html(h,data.responseStatus);
				document.location.href = '#';
				m.dom.effects.fadeIn(_options.containerNode);
			}
		} 		
	},
	_moveSuccess = function() {
		var success = m.dom.find(".success:not(:empty)"); // not empty error blocks
		
		if(success.length>0) { // we got some errors
			var mainSuccessContainer=m.dom.find("#main-success-container"), 
				successBlock = m.dom.find("p",mainSuccessContainer);
			m.util.each(success, function(i,item){
				 var error = m.dom.html(item);
				 m.dom.append(successBlock,error);
			});
			m.dom.show(mainSuccessContainer);
		}
	},
	_clearSuccess = function() {
		var cancel = m.dom.find("[data-dismiss]", _options.containerNode);
		if(cancel.length > 0){
			m.dom.trigger(cancel, "click");
		} else {
			m.dom.hide(_options.containerNode);
		}
	},
	_bootstrap = function(){
		_moveSuccess();
	};
	return {
		init : function(options) {		
			console.log("INIT module " + _MODULE_NAME);
			_init.bind(options)
			m.subscribe("clear-success", _clearSuccess); 
			m.subscribe("ajax-success", _handleAjaxSuccess);
			m.subscribe("bootstrap",_bootstrap)
		},
		
		destroy : function() {
			
		}
	};
});