define([ "core/mediator" ], function(core) {

	return {
		destroy : function(context){
			if(!core.util.is(context)) {
				context = document;
			}
			var modules = core.dom.find("[data-module]", context);
			for(var i = 0; i<modules.length; i++) {
				core.loader.destroy(core.dom.data(modules[i], "moduleId"));
				core.dom.data(modules[i], "moduleId", "");
				core.dom.removeAttr(modules[i], "data-module-id", "");
			}
		},
		execute : function(context) {
			
			if(!core.util.is(context)) {
				context = document;
			}
			
			console.log("module-loader execute");
			//directly attached modules
			var modules = core.dom.find("[data-module]:not([data-module-id])", context),
			//proxy info cariers
				proxies = core.dom.find("[data-module-proxy]", context),
				loader = core.loader;
			core.util.each(modules, function() {
				var deferred = core.dom.data(this, "moduleDeferred");
				// deferred module load
				if(core.util.is(deferred) && deferred === 'deffered'){
					core.dom.data(this, "moduleDeferred", "");
					return;
				}
				
				var name = core.dom.data(this, "module"), params = core.dom
						.data(this, "module" +
							 core.util.camelize(name.replace(".", "_")) + "Params"), 
							 id = core.dom.attr(this, "data-module-id");
				
				if (typeof (id) === "undefined") {
					id = "module-" + name.replace(".", "-") + "-" + core.util.newGUID();
					core.dom.attr(this, "data-module-id", id);
				}
				
				loader.add2boot(id, name.replace(".", "/"), this, params);
			});
			core.util.each(proxies, function() {
				var target= core.dom.find(core.dom.data(this, "module-target"));
				var name = core.dom.data(this, "module-proxy"), params = core.dom
						.data(this, "module"
								+ core.util.camelize(name.replace(".", "_"))
								+ "Params"), id = core.dom.attr(target,
						"data-module-id");
				
				if (typeof (id) == "undefined") {
					id = "module-" + name.replace(".", "-") + "-"
							+ core.util.newGUID();
					core.dom.attr(target, "data-module-id", id);
				}
				
				loader.add2boot(id, name.replace(".", "/"), target, params);
			});
			loader.boot();
		}
	};
});