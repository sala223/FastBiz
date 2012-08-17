requirejs.config({
    shim: {
		'jquery-ui':{
            deps: ['jquery-1.7.2.min']
         },
        'ember-0.9.8.1':{
            deps: ['jquery-1.7.2.min'],
            exports: 'Ember'
         },
        'flame-0.2.1': {
                deps: ['ember-0.9.8.1'],
                exports: 'Flame'
        },
        'flame_inspector': {
            deps: ['ember-0.9.8.1','flame-0.2.1'],
            exports:'FlameInspector'
        },
		'fb': {
            deps: ['ember-0.9.8.1','flame-0.2.1','flame_inspector','jquery-ui','ember-i18n'] 
        },
        'wms': {
            deps: ['flame-0.2.1','fb','jquery-ui'] 
        },
		'ember-i18n': {
            deps: ['ember-0.9.8.1'] 
        }
    },

    baseUrl: 'static/js',
    paths: {
        'jquery-1.7.2.min': '/share/static/js/jquery-1.7.2',
		'jquery-ui': '/share/static/js/jquery-ui-1.8.22.custom',
		'jquery.url': '/share/static/js/jquery.url.js',
		'ember-0.9.8.1': '/share/static/js/ember-0.9.8.1',
		'flame-0.2.1': '/share/static/js/flame-0.2.1',
		'flame_inspector':'/share/static/js/flame_inspector',
		'fb':'/share/static/js/fb',
		'ember-i18n':'/share/static/js/ember-i18n-1.2.0'
    }
});

require(["wms"]);

(function(){
	function includeCSS(filename){
		$('head').append( $('<link rel="stylesheet" type="text/css" />').attr('href', filename) );
	}
	
	if(!window['Fb']){
		window['Fb'] = {}
	}
	window['Fb']['includeCSS']=includeCSS;
})();

Fb.includeCSS("/share/static/css/flame.css");
Fb.includeCSS("/share/static/css/fb.css");
Fb.includeCSS("static/css/wms.css");





 