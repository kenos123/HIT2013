window.app = window.app || {};

window.app.languages = {
	pageLocale : 'window.app.i18n.locale'
};

window.app.modules = {
	path: 'modules/'
};

if (!Function.prototype.bind) {  
	  Function.prototype.bind = function (oThis) {  
	    if (typeof this !== "function") {  
	      // closest thing possible to the ECMAScript 5 internal IsCallable function  
	      throw new TypeError("Function.prototype.bind - what is trying to be bound is not callable");  
	    }  
	  
	    var aArgs = Array.prototype.slice.call(arguments, 1),   
	        fToBind = this,   
	        fNOP = function () {},  
	        fBound = function () {  
	          return fToBind.apply(this instanceof fNOP ? this : oThis || window,  
	                               aArgs.concat(Array.prototype.slice.call(arguments)));  
	        };  
	  
	    fNOP.prototype = this.prototype;  
	    fBound.prototype = new fNOP();  
	  
	    return fBound;  
	  };  
	} 
window.app.debug = window.app.debug || 0;
if(!window.app.debug || typeof window.console === 'undefined'){
	var c = function(){var a=["log","debug","info","warn","error","assert","dir","dirxml","group","groupEnd","time","timeEnd","count","trace","profile","profileEnd"];window.console={};for(var b=0;b<a.length;++b)window.console[a[b]]=function(){}}();
}
var config = {
  paths: window.app.paths,
  shim: window.app.shim,
  config: {
	  i18n: {
		  locale: window.app.i18n.locale
	  }
  }
};
//disable caching by adding variable argument into request for files
require.config(config);


require(["core/mediator", "core/module-loader"], function(mediator, moduleLoader) {
	// find modules and register them
	mediator.dom.ready(function() {
		moduleLoader.execute();
	});	
});