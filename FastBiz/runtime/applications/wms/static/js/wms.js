Fb.loadLanguageResource('static/js/i18n');
Fb.translate();

var Wms = Em.Application.create({
  ready: function() {
    this._super();
    window.scrollTo(0);
  }
});

Wms.WmsImageUrlPrefix = './static/images/';

Wms.image = function(imageUrl){
    if(typeof Wms.WmsImageUrlPrefix == 'undefined') {
        return imageUrl;
    }else{
        return Wms.WmsImageUrlPrefix + imageUrl;
    }
};

Wms.User=Em.Object.extend({
	name:null,
    gender:null
});

Wms.searchConroller=Ember.Object.create({
	searchText: 'test',
});

Wms.FunctionGroup = Em.Object.extend({
	id:null,
	text:null,
	icon:null,
	subs:[]
});

Wms.functionsConroller=Ember.Object.create({
	functionGroups:[], 
	 
	loadFunctionGroups:function(){ 	
		var controller = this;
		jQuery.getJSON("ui/functionGroup.json", function(json) {
			var rootFunctionGroup = Wms.FunctionGroup.create();
			rootFunctionGroup.setProperties(json['functionGroup']);
			controller.set('functionGroups',rootFunctionGroup['subs']); 
		});		 
	}
});

Wms.MainView = Flame.View.extend({
	elementId: 'mainView',
	childViews:'toolBarView frameView'.w(),
	classNames: ['main'],
	layout: { top: 85, bottom:0, width:'100%'},
	layoutManager: Flame.VerticalStackLayoutManager.create({ spacing: 0 }),
	
	toolBarView:Flame.View.extend({
		elementId: 'toolBarView',
		controller: Wms.searchConroller,
		childViews:'viewSwitcherView searchFieldView'.w(),
		classNames: ['toolbar'],
		layout: {height:30,width:'100%'},
		
		searchFieldView:Flame.SearchTextFieldView .extend({
			elementId: 'searchFieldView',
			classNames: ['search'],
			layout: { top:0, right:$(window).width()*0.08, width:250, height:30 },
		}),
		
		viewSwitcherView:Flame.View.extend({
			elementId: 'viewSwitcherView',
			layout: { top:2, left: 2, width:18, height:28 },
			classNameBindings:['animationClass'],
			isHidden: false,
			tooltipView:undefined,
			animationClass: function(){
				if(this.get('isHidden')){
					return 'viewSwitcher-hidden';
				}else{
					return 'viewSwitcher-full';
				}
			}.property('isHidden'),
			
			init:function(){
				this._super();
				this.addObserver('isHidden', this, this._changeViewState);
			},
			
			click:function(event) {
				this.set('isHidden',!this.get('isHidden'));
			},	
			
			mouseEnter: function(){
				var toolTipView = Fb.ToolTipView.create({layout:{width:60}});  
				if(this.get('isHidden')){
					toolTipView.set('toolTipText','expand'.loc());
				}else{
					toolTipView.set('toolTipText','collapse'.loc());
				}
				
				this.set('toolTipView',toolTipView);
				toolTipView.popup(this,Flame.POSITION_RIGHT);	
				return true;
			},
			
			mouseLeave: function(){
				if((typeof this.get('toolTipView')) != 'undefined'){
					this.get('toolTipView').close();
				}
				return true;
			},
			
			_changeViewState:function(event){
				var mainView = this.getPath('parentView.parentView');
				var frameView = mainView.get('childViews')[1];
				if (!frameView.get('allowResizing')) false;
				
				var isResizing = frameView.get('resizeInProgress');
				if(!isResizing){
					if(this.get('isHidden')){
						frameView.toggleCollapse(event,this); 
					}else{
						frameView.toggleCollapse(event,this);			
					} 
				}
				return true;
			}
		}),	
	}),

	frameView:Flame.HorizontalSplitView.extend({
		elementId: 'flameView',
		controller: Wms.functionsConroller,
		leftWidth:250,
		minLeftWidth:0,
		minRightWidth:$(window).width()*0.8,
		allowResizing:true,	
		dividerWidth:5,
		autoCollapseWidth:20,
		
		init: function() {
			this._super();
			this.get('dividerView').get('classNames').push('splitDivder');   
		},

		toggleCollapse:function(event){
			var switcherView = this.get('parentView').get('childViews')[0].get('childViews')[0];
			var isHidden = switcherView.get('isHidden');
			if(arguments.length == 1){
				switcherView.set('isHidden', !isHidden);
			}else{
				this._super(event);
			}
		},
	
		resize:function(event) {
			var ret = this._super(event);
			if(this.get('resizeInProgress')){
				var leftWidth = this.get('leftWidth');
				var switcherView = this.get('parentView').get('childViews')[0].get('childViews')[0];
				if(leftWidth <= (this.get('minLeftWidth')+this.get('autoCollapseWidth'))){
					switcherView.set('isHidden', true);
				}else{
					switcherView.set('isHidden', false);
				}
			}	
			return ret;
		},
	
		rightView: Flame.TabView.extend({	
		}),
		
		leftView: Flame.View.extend({
			childViews:'functionGroupsView'.w(),
			layout: { height:'100%' },
			functionGroupsView: Fb.AccordionView.extend({
				functionGroupsBinding:Ember.Binding.oneWay('Wms.functionsConroller.functionGroups'),
				animated:'slide', 
				clearStyle:true,
				collapsible:true,
				fillSpace:true,
				
				init:function(){
					this._super();
				},

				_functionGroupsDidChange:function(){
					this.destroyAllChildren(); 
					var functionGroups = this.get('functionGroups'); 
					if(!functionGroups){
						return;
					}
					var self = this;
					var contentViews = this.get('contentViews');
					$.each(functionGroups,function(index){
						var contentView = self.createChildView(Flame.View);
						self.get('headers').push(functionGroups[index].display); 
						self.get('contentViews').push(contentView);
					});	
					this._createTabs();
				}.observes('functionGroups')
			})
		})
		
	})
	 
});

Wms.functionsConroller.loadFunctionGroups();




 
 