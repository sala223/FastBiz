window.JQ = window.JQ || {};
window.Fb = window.Fb || {};
Fb.urls={}; 

Fb.init=function(){
	Flame.imagePath = '/share/static/images/';	 
	Fb.imagePath = '/share/static/images/';	 
	Fb.forEach = Ember.ArrayUtils.forEach;
	Fb.urls.language='permitted/locale/language.json';
	Fb.urls.supportedLanguages='permitted/locale/supportedLanguages.json';
	window.I18N=Ember.STRINGS;
	Fb.language='en';
	Fb.languageVarName='language';
	
	Fb.loadLanguageResource=function(resource){
		return $.ajax({
			type: 'GET',
			url: Fb.urls.language,
			dataType: 'json',
			success: function(data, textStatus, jqXHR) { 
				var languageCode=data.language.code;
				Fb.language = data.language.code;
				var res = resource+'_'+languageCode + '.js';
				$.ajax({
					async: false,
					url: res,
					dataType: "script",
					success: undefined
				}).fail( 
					function(jqXHR, settings, exception){ 
						var defaultRes = resource +'.js';
						Ember.Logger.log('cannot load ' + res + ', load default ' + defaultRes);
						$.ajax({
							async: false,
							url: defaultRes,
							dataType: "script",
							success: undefined
						});
					}
				);
			},
		data: {},
		async: false
		});	
	}
	
	Fb.translate=function(){
		var title = document.title;
		if(title){
			var parts = title.split(" ");
			var newTitle = [];
			for(var i = 0 ; i< parts.length ; i++){
				var part = parts[i];
				if(part.indexOf('t:') == 0){
					newTitle.push(part.substring(2).loc());
				}
			}
			document.title = newTitle.join();
		}
		
		$.each($("I18N"),function(i,e){
			var key = e.firstChild != null? e.firstChild.nodeValue:'';
			if(e.firstChild!=null){
				e.firstChild.nodeValue=key.loc();
			} 
		});
	}
}();


JQ.Widget = Em.Mixin.create({
	didInsertElement : function(){
		var options = this._gatherOptions();
		this._gatherEvents(options);
		var uiType = jQuery.ui[this.get('uiType')];
		var ui = null;
		if(jQuery.ui.version <= '1.8.22'){
			ui = new uiType();
			uiType.call(ui,options,this.get('element')); 
		}else{
			ui = jQuery.ui[this.get('uiType')](options, this.get('element'));
		}
		this.set('ui',ui);
	} ,
	
	willDestroyElement:function(){
		var ui = this.get('ui');
		
		if(ui){
			var observers = this._observers;
			for(var prop in observers){
				if(observers.hasOwnProperty(prop)){
					this.removeObserver(prop, observers[prop]);
				}
			}
			ui._destroy();
		}
	},
		
	_gatherOptions : function(){
		var uiOptions = this.get('uiOptions'), options = {};
			
		uiOptions.forEach( function(key){
			options[key] = this.get(key);
				
			var observer = function(){
				var value = this.get(key);
				this.get('ui')._setOption(key,value);
			};
				
			this.addObserver(key, observer);
			this._observers = this._observers || {};
				
		}, this);
			
		return options;
	},
		
	_gatherEvents : function(options){
		var uiEvents = this.get('uiEvents') || [], self = this;
			
		uiEvents.forEach(function(event){
			var callback = self[event];
				
			if(callback){
				options[event]=function(event,ui){
					callback.call(self,event,ui);
				};
			}
		});
	}
		
});
 

Fb.AccordionView = Flame.View.extend(JQ.Widget,{
	uiType:'accordion',
	uiOptions:['disabled','active','animated','autoHeight','clearStyle','collapsible',
	'event','fillSpace','header','icons','navigation','navigationFilter'],
	uiEvents:['create','change','changestart'],
	classNames:['fb-accordion-view'],
	contentViews:[],
	headers:[],
	headerViewClass:undefined,
	tabsHeight:23,
	 
	init:function(){
		this._super();
		this._createTabs();
		this.addObserver('headers', this, this._createTabs);
        this.addObserver('contentViews', this, this._createTabs);
	},
	
	
	didInsertElement: function() {
		this._super();
		this.$().accordion();
    },
	
	_createTabs:function(){
		this.destroyAllChildren(); 
		var headers = this.get('headers');
		var contentViews = this.get('contentViews');
		var self = this;
		this.get('headers').forEach(function(header, idx){
			if(header){
				self._createTab(header, contentViews[idx]);
			}
		});
	},
	
	_createTab:function(header,contentView){
		var headerView = null;
		if(typeof headerViewClass != 'undefined'){
			headerView = this.createChildView(headerViewClass);
		}else{
			headerView = this.createChildView(Fb.AccordionHeaderView);
		}
		headerView.set('header',header);
		this.get('childViews').pushObject(headerView);
		this.get('childViews').pushObject(contentView);
	} 
});

Fb.AccordionHeaderView=Flame.View.extend({
    classNames: ['fb-accordion-header-view'],
    layout: { left: 0, top: 0, right: 0, height:30},
	header:'',
	handlebars: "{{header}}",
	tagName:'h3', 
	
	templateContext: function() {
        return { header: this.get('header') };
    }.property('header'),
	
	init: function(){
		this._super();
	}
});

Fb.AccordionContentView=Ember.View.extend({
    classNames: ['fb-accordion-content-view'],
	tagName:'div',
	init: function(){
		this._super();
	}
});


Fb.languageConroller=Ember.Object.create({
	refresh:true,
	supportedLanguages: [],
	
	loadingMenuItems:function(){
		var supportedLanguages = this.get('supportedLanguages');
		if(supportedLanguages.length == 0 || this.get('refresh')){
			$.getJSON(Fb.urls.supportedLanguages, function(data, textStatus, jqXHR){
				var languages = data['languageList'];
				for(var i=0; i<languages.length; i++) {
					var language = languages[i];
					supportedLanguages.push({languageTitle:language['display'],languageCode:language['code'],isChecked:false,enabled:true, action:'changeLanguage'});
				}
			});
		}
	}
});

Fb.LanguageSelectionMenu=Flame.SelectButtonView.extend({
	elementId: 'languageSelectionView',
	controller: Fb.languageConroller,
	itemCheckedKey:'isChecked',
	itemEnabledKey:'enabled',
	itemTitleKey:'languageTitle',
	itemValueKey:'languageCode',
	itemActionKey:'action',
	subMenuKey:undefined,
	value:Fb.language,
	itemsBinding:'controller.supportedLanguages',
	layout:{width:100},
	
	init: function(){
		this._super(); 
		this.set('value',Fb.language);
		var classes=this.get('classNames');
		classes.push('fb-language-selection');
	},
	
	 menu: function() {
        var self = this;
        return Flame.MenuView.extend({
			target:this,
            selectButtonView: this,
            itemTitleKey: this.get('itemTitleKey'),
            itemValueKey: this.get('itemValueKey'),
            itemActionKey: this.get('itemActionKey'),
            subMenuKey: this.get('subMenuKey'),
            itemsBinding: 'selectButtonView.items',
            valueBinding: 'selectButtonView.value',
            minWidth: this.getPath('layout.width') || this.$().width(),
            close: function() {
                self.gotoState('idle');
                this._super();
            }
        });
    }.property(),
	
	changeLanguage:function(){
		var selectedLanguage = this.get('value');
		if(selectedLanguage == Fb.language){
			return false;
		}
		window.location.reload(true);
		var searchString = window.location.search.substring(1);
		if(searchString){
			var params = searchString.split("&");
			var hasLanguagePar = false;
			var hasTokenPar = false;
			var newHref = window.location.href;
			for (var i=0;i<params.length;i++){
				var val = params[i].split("=");
				if (val[0] == Fb.languageVarName) {
					hasLanguagePar = true;
					newHref = newHref.replace(val[1],selectedLanguage);
				}
				if (val[0] == 'cctoken') {
					hasTokenPar = true;
					newHref = newHref.replace(val[1],new Date().getTime()); 
				}
			}
			
			if(!hasLanguagePar){
				newHref = newHref+'&'+Fb.languageVarName+'='+selectedLanguage;
			}
			if(!hasTokenPar){
				newHref = newHref+'&cctoken='+new Date().getTime();
			}	
			window.location.replace(newHref);
		}else{
			window.location.replace(window.location.href+'?language='+selectedLanguage+'&cctoken='+new Date().getTime());
		}
	} 
	
});

Fb.languageConroller.loadingMenuItems();

Fb.ToolTipView=Flame.Popover.extend({
	classNames:['fb-tooltip'],
	toolTipText : '',
	isModal:false,
	layout:{width:100, heigth:0},
	handlebars: '{{view contentView}}',
	tagName:'div',
	
	contentView: Flame.LabelView.extend({
		layout:{width:'parentView.layout.width', heigth:0},
		textAlign: Flame.ALIGN_CENTER,
		tagName:'span',
		classNames:['fb-tooltip-span'],
		valueBinding:'parentView.toolTipText'
	})
});







