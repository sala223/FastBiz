 (function(){
	function includeCss(filename){
	    var fileRef=document.createElement("link");
		fileRef.setAttribute("rel", "stylesheet");
		fileRef.setAttribute("type", "text/css");
		fileRef.setAttribute("href", filename);
		document.getElementsByTagName("head")[0].appendChild(fileref);
	}
	
	if(!window['fb']){
		window['fb'] = {}
	}
	window['fb']['includeCss']=includeCss;
})();

require([
		"/share/static/js/jquery-1.7.2.min.js",
		"/share/static/js/ember-0.9.8.1.min.js",
		"/share/static/js/ember-0.9.8.1.js", 
		"/share/static/js/flame-0.2.1.min.js",
		"/share/static/js/flame-0.2.1.js",
		"/share/static/js/flame_inspector.js",
		"static/js/wms.js"
		], function() {});


 