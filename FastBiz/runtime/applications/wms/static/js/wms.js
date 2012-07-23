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
		"/share/static/js/flame_inspector.js"
		], function() {});


var Wms = Em.Application.create({
  ready: function() {
    this._super();
    window.scrollTo(0);
  }
});

Wms.User=Em.Object.extend({
	name:null,
    gender:null
});

Wms.mainController= Ember.Object.create({
	searchText: 'test',
});

Wms.HeaderView= Flame.RootView.extend({
	handlebars: '<div id="branding"><span>WhareHouse Management System</span></div>' + 
	'<div id="actions">'+
	'<ul class="action right"><li class="actionItem"><a class="button" id="logout" href="/secure/logout">'+
	'<span>Logout</span></a></li></ul></div>',
	classNameBindings:'test',
	templateName: 'header'
});

Wms.RootView = Flame.RootView.extend({
	controller: Wms.mainController,
	childViews: 'splitView'.w(),
	splitView: Flame.HorizontalSplitView.extend({
		leftWidth:250,
		minLeftWidth:200,
		
		leftView: Flame.View.extend({
			childViews:'expandBar'.w(),
			
			expandBar: Flame.View.extend({
				layout: { height: 35, left: 0, right: 0, top: 0 },
                classNames: ['title'],
                childViews: 'searchFieldView'.w(),
				
				searchFieldView: Flame.SearchTextFieldView.extend({
                layout: { top: 42, left: 5, right: 5 },
                placeholder: 'Search',
				valueBinding: '^controller.searchText'
				}),
			}),	
		}),
		
		rightView: Flame.View.extend({
			
		})
	}),
});
