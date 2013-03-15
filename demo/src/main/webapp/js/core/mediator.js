/**
 * Application core. Implements the mediator pattern and encapsulates the core
 * functionality for this application. Based on the work by Addy Osmani and
 * Nicholas Zakas.
 * 
 * @link <a href="http://addyosmani.com/largescalejavascript/">Patterns For
 *       Large-Scale JavaScript Application Architecture</a>
 * @link <a
 *       href="http://speakerdeck.com/u/addyosmani/p/large-scale-javascript-application-architecture">Large-scale
 *       JavaScript Application Architecture Slides</a>
 * @link <a href="http://addyosmani.com/blog/large-scale-jquery/">Building
 *       Large-Scale jQuery Applications</a>
 * @link <a
 *       href="http://www.youtube.com/watch?v=vXjVFPosQHw&feature=youtube_gdata_player">Nicholas
 *       Zakas: Scalable JavaScript Application Architecture</a>
 * @link <a
 *       href="http://net.tutsplus.com/tutorials/javascript-ajax/writing-modular-javascript-new-premium-tutorial/">Writing
 *       Modular JavaScript: New Premium Tutorial</a>
 */
/* jslint nomen:true, sloppy:true, browser:true */
/* global define:false, require:false */
define(["jquery", "libs/jquery/json.min", "mustache", "regexp" ],
function($) {
	'use strict';
	var channels = {}; // Loaded modules and their callbacks
	var mediator = {// Mediator object
		modules : {}
	}; 
	
	$.fn.serializeObject = function() {
		var o = {};
		var a = this.serializeArray();
		$.each(a, function() {
			if (o[this.name]) {
				if (!o[this.name].push) {
					o[this.name] = [o[this.name]];
				}
				o[this.name].push(this.value || '');
			} else {
				o[this.name] = this.value || '';
			}
		});
		return o;
	};
	

	/**
	 * Override the default error handling for requirejs
	 * 
	 * @todo When error messages become part of core, use them instead
	 * @link <a href="http://requirejs.org/docs/api.html#errors">Handling Errors</a>
	 */
	/* global requirejs:false */
	requirejs.onError = function(err) {
		if (err.requireType === 'timeout') {
			console.warn('Could not load module ' + err.requireModules);
		} else {
			throw err;
		}
	};

	/**
	 * Subscribe to an event
	 * 
	 * @param {string}
	 *            channel Event name
	 * @param {object}
	 *            subscription Module callback
	 * @param {object}
	 *            context Context in which to execute the module
	 */
	mediator.subscribe = function(channel, callback) {
		console.log("__subscribe", channel);

		channels[channel] = (!channels[channel]) ? []
				: channels[channel];

		channels[channel].push(callback);
	};

	/**
	 * Publish an event, passing arguments to subscribers. Will call autoload if
	 * the channel is not already registered.
	 * 
	 * @param {string}
	 *            channel Event name
	 */
	mediator.publish = function(channel, data) {
		console.log("__publish", channel, data);

		var i, l;// , args = [].slice.call(arguments, 1);

		if (channels[channel]) {
			l = channels[channel].length;
			for (i = 0; i < l; i += 1) {
				if(typeof channels[channel][i] === 'undefined' ) {
					delete channels[channel][i];
					continue;
				}
				channels[channel][i](data);
			}
		}
	};

	mediator.loader = (function() {
		var bootTable = [];
		var firstBoot = true;
		return {
			/*
			 * Adds module to booting table
			 * 
			 * @param {string} id Node id @param {string} file path to module
			 * @param {object} node DOM node @param {array} params
			 * Initialization parameters for module
			 */
			add2boot : function(id, file, node, params) {
				bootTable.push({
					'id' : id,
					'file' : file,
					'node' : node,
					'params' : params
				});
			},

			/**
			 * Starts bootstrap
			 */
			boot : function() {
				var tempBootTable = bootTable.slice();
				var btl = tempBootTable.length;
				for ( var i = 0; i < btl; i++) {
					var mod = tempBootTable[i];
					console.log("boot module", mod);
					this.autoload(mod.id, mod.file, mod.node,
							mod.params);
				}
			},

			/**
			 * Load module
			 */
			autoload : function(id, file, node, params, onLoaded) {
				var i, l;
				require([ window.app.modules.path + file ], function(module) {

					var moduleContext = {
							'id': id,
							'node' : node,
							'params' : params
					};
					module.init(moduleContext);

					if(mediator.util.is(moduleContext)) {
						mediator.modules[id] = {"context": moduleContext, "file":file}; 
					}
					for ( var i = 0; i < bootTable.length; i++) {
						if (bootTable[i].id === id) {
							bootTable.splice(i, 1);
							if (bootTable.length === 0) {
								if (firstBoot) {
									mediator.publish("bootstrap");
									firstBoot = false;
								} else {
									mediator.publish("reboot-context");
								}
							}
							break;
						}
					}
					if(mediator.util.is(onLoaded)) {
						onLoaded();
					}

				});
			},
			destroy: function(id){
				
				var moduleInstance = mediator.modules[id];
				console.info("destroy", moduleInstance);
				if(mediator.util.is(moduleInstance)){
					console.info(moduleInstance);
					require([window.app.modules.path + moduleInstance.file], function(module){
						module.destroy.call(moduleInstance.context);
						delete mediator.modules[id];
					});
					
				}
			}

		};
	})();

	mediator.util = {
		// envelope in which all data across modules will be and SHOULD
		// be passed
		envelope : {
			meta : { // namespace for meta information of the message
				publisher : "" // and other neccessary values....
			},
			data : "" // real data which will be processed by the
		// module
		},
		/**
		 * Find value of variable within the object by string that represents
		 * path written by dot notation.
		 * 
		 * @param path
		 *            Path within "data" written by dot notation
		 * @param data
		 *            Object where variable is searched
		 * @returns Value of variable or null if it is not founded
		 */
		getVarByPath : function(path, data) {
			var v = data, paths = path.split('.');
			for ( var i = 0; i < paths.length; i++) {
				if (this.is(v[paths[i]])) {
					v = v[paths[i]];
				} else {
					return null;
				}
			}
			return v;
		},

		forEach : function(items, fn) {
			$.each(items, fn);
		},
		// works only for DOM objects (jQuery instances)
		each : function(items, fn) {
			$(items).each(fn);
		},
		eachReversed : function(items, fn) {
			$($(items).get().reverse()).each(fn);
		},
		inArray : function(value, array) {
			return $.inArray(value, array);
		},
		decamelize : function(camelCase, delimiter) {
			delimiter = (delimiter === undefined) ? "_" : delimiter;
			return camelCase.replace(/([A-Z])/g, delimiter + '$1')
					.toLowerCase();
		},
		/**
		 * @link <a href="https://gist.github.com/827679">camelize.js</a>
		 * @param {string}
		 *            str String to make camelCase
		 */
		camelize : function(str) {
			return str.replace(/(?:^|[-_])(\w)/g,
					function(delimiter, c) {
						return c ? c.toUpperCase() : '';
					});
		},

		is : function(val) {
			return (typeof val !== 'undefined');
		},
		isString : function(input) {
			return (typeof (input) === 'string' && isNaN(input));
		},
		typeEqual : function(input, desiredType) {
			return mediator.util.type(input).toLowerCase() === desiredType;
		},
		isArray : function(obj) {
			return $.isArray(obj);
		},
		type : function(input) {
			return Object.prototype.toString.call(input).match(
					/^\[object\s(.*)\]$/)[1];
		},

		/**
		 * Always returns the fn within the context
		 * 
		 * @param {object}
		 *            fn Method to call
		 * @param {object}
		 *            context Context in which to call method
		 * @returns {object} Fn with the correct context
		 */
		method : function(fn, context) {
			return $.proxy(fn, context);
		},
		parseJson : function(json) {
			return $.parseJSON(json);
		},
		serializeJson : function(json) {
			return JSON.stringify(json);
		},
		newGUID : function() {
			var s4 = function() {
				return (((1 + Math.random()) * 0x10000) | 0).toString(
						16).substring(1);
			};
			return (s4() + s4() + "-" + s4() + "-" + s4() + "-" + s4() +
					 "-" + s4() + s4() + s4());

		},
		extend : function() {
			var args = [].slice.call(arguments, 0);
			return $.extend.apply(this, args);
		},
		convertJSON : function(jsonString) {
			var pom = jsonString.replace(/\\\'/g, "\\\'");
			pom = mediator.regex.replace(pom, /([^\\])''/g, "$1\"\"");
			pom = mediator.regex.replace(pom, /([^\\])'/g, "$1\"");
			return pom;
		},
		map : function(data, fn) {
			return $.map(data, fn);
		},
		serialize : function(selector, decoded) {
			if (decoded !== undefined) {
				var pom = $(selector).serialize();
				if (decoded) {
					return decodeURIComponent(pom);
				}
			}
			return $(selector).serialize();
		},
		serializeObject : function(selector) {
			return $(selector).serializeObject();
		}
	};

	mediator.dom = {
		ready : function(fn) {
			$(document).ready(fn);
		},
		create : function(value) {
			return $(value);
		},
		getParent : function(el, parentSel) {
			return $(el).parent(parentSel);
		},
		getParents : function(el, parentSel) {
			return $(el).parents(parentSel);
		},
		closest : function(el, selector) {
			return $(el).closest(selector);
		},
		children : function(el, selector) {
			return $(el).children(selector);
		},
		find : function(selector, context) {
			context = context || document;
			return $(context).find(selector);
		},
		append : function(selector, content) {
			return $(selector).append(content);
		},
		prepend : function(selector, content) {
			return $(selector).prepend(content);
		},
		after : function(selector, content) {
			return $(selector).after(content);
		},
		before : function(selector, content) {
			return $(selector).before(content);
		},
		clone : function(selector) {
			return $(selector).clone();
		},
		remove : function(selector) {
			return $(selector).remove();
		},
		hide : function(selector) {
			return $(selector).hide();
		},
		show : function(selector) {
			return $(selector).show();
		},
		data : function(selector, attribute, value) {
			switch(arguments.length) {
			case 3:
				$(selector).data(attribute, value);	
				break;
			case 2: return $(selector).data(attribute);	
			case 1: return $(selector).data();	
			} 
			return $(selector).data(attribute);
		},
		attr : function(selector, attribute, value) {
			if (value !== undefined) {
				$(selector).attr(attribute, value);
			} else {
				return $(selector).attr(attribute);
			}
		},
		removeAttr : function(selector, attribute) {
			return $(selector).removeAttr(attribute);
		},

		addClass : function(selector, cssClass) {
			$(selector).addClass(cssClass);
		},

		removeClass : function(selector, cssClass) {
			$(selector).removeClass(cssClass);
		},
		toggleClass : function(selector, cssClass){
			$(selector).toggleClass(cssClass);
		},
		hasClass : function(selector, cssClass) {
			return $(selector).hasClass(cssClass);
		},

		css : function(selector, stls) {
			$(selector).css(stls);
		},

		html : function(selector, value) {
			if (mediator.util.is(value)) {
				$(selector).html(value);
			} else {
				return $(selector).html();
			}
		},
		text : function(selector, value) {
			if (mediator.util.is(value)) {
				$(selector).text(value);
			} else {
				return $(selector).text();
			}
		},
		is : function(el, selector) {
			return $(el).is(selector);

		},
		value : function(selector, value) {
			if (value !== undefined) {
				$(selector).val(value);
			} else {
				return $(selector).val();
			}
		},
		/**
		 * events [, selector] [, data], handler(eventObject)
		 * 
		 * events	-	One or more space-separated event types and optional
		 * namespaces, such as "click" or "keydown.myPlugin".
		 * 
		 * selector	-	A selector string to filter the descendants of the selected
		 * elements that trigger the event. If the selector is null or omitted,
		 * the event is always triggered when it reaches the selected element.
		 * 
		 * data	-	Data to be passed to the handler in event.data when an event is
		 * triggered.
		 * 
		 * handler(eventObject)	-	A function to execute when the event is
		 * triggered. The value false is also allowed as a shorthand for a
		 * function that simply does return false.
		 */
		on: function(el, events, selector, data, handler) {
			if(mediator.util.typeEqual(selector, 'function')) {
				handler = selector;
				$(el).on(events, handler);
			} else if(mediator.util.typeEqual(data, 'function')) {
				handler = data;
				$(el).on(events, selector, handler);
			} 
		},
		bind: function(el, evt, data, fn) {
			if (el && evt) {
				if(fn === undefined) {
					fn = data;
					data = undefined;
				}
				
				if (mediator.util.typeEqual(evt, 'function')) {
					fn = evt;
				}

				$(el).on(evt, data, fn);
			} 
		},

		unbind : function(el, evt, fn) {

			if (el && evt) {
				if (mediator.util.typeEqual(evt, 'function')) {
					fn = evt;
				}
				$(el).off(evt, fn);
			} else {
				$(el).off(el, evt);
			}
		},
		trigger : function(el, evt) {
			$(el).trigger(evt);
		},
		one : function(el, evt, fn) {

			if (el && evt) {
				if (mediator.util.typeEqual(evt, 'function')) {
					fn = evt;
				}
				$(el).one(evt, fn);
			}
		},
		ajax : function(options) {

			var xhrArgs = {};
			xhrArgs.type = options.method;
			xhrArgs.url = options.url;
			xhrArgs.data = options.data;
			xhrArgs.success = options.success;
			xhrArgs.beforeSend = options.beforeSend;
			xhrArgs.dataType = options.dataType;
			xhrArgs.contentType = options.contentType;
			xhrArgs.cache = options.cache;
			xhrArgs.error = options.error;
			if (xhrArgs.contentType === 'application/json') {
				xhrArgs.data = JSON.stringify(xhrArgs.data);
			}
			return $.ajax(xhrArgs);
		},

		// ,toggle:function(el,fn1,fn2) {
		// $(el).toggle(fn1,fn2);
		// }
		effects : {
			fadeIn : function(selector, duration, fn) {
				if (fn !== undefined) {
					return $(selector).fadeIn(duration, fn);
				}
				return $(selector).fadeIn(duration);
			},
			fadeOut : function(selector, duration, fn) {
				if (fn !== undefined) {
					return $(selector).fadeOut(duration, fn);
				}
				return $(selector).fadeOut(duration);
			},
			slideUp : function(selector, duration, fn) {
				if (fn !== undefined) {
					return $(selector).slideUp(duration, fn);
				}
				return $(selector).slideUp(duration);
			},
			slideDown : function(selector, duration, fn) {
				if (fn !== undefined) {
					return $(selector).slideDown(duration, fn);
				}
				return $(selector).slideDown(duration);
			}
		}
	};

	mediator.templates = {
		render : function(template, data) {
			return window.Mustache.render(template, data);
		}
	};

	mediator.plugins = {
		dropdown : function(nodes, options) {
			require(["plugins/bootstrap.dropdown"],
				function() {
				options = options || {};	
					$(nodes).dropdown(options);
				});
		}
		
	};
	mediator.regex = {
		replace : function(str, search, replacement) {
			return window.XRegExp.replace(str, search, replacement);
		}
	};
	return mediator;

});