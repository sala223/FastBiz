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

Wms.searchConroller=Em.Object.create({
	searchText: 'test',
});

Wms.FunctionGroup = Em.Object.extend({
	id:null,
	display:null,
	icon:null,
	subs:[]
});

Wms.FunctionGroupNode = Em.Object.extend({
	functionGroup:null,
	treeItemIsExpanded:false,
	treeItemChildren:[]
});

Wms.FunctionGroupLeaf = Em.Object.extend({
	functionGroup:null,
	treeItemIsExpanded:false
});

Wms.functionsConroller=Em.Object.create({
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

Wms.functionsConroller.loadFunctionGroups();


Wms.FunctionGroupContentTreeItemView=Flame.TreeItemView.extend({
	classNames: ['fg-tree-item'],
	layout: { top: 0, left:0, height:0, padding:5},
	 
	click:function(event) {
		var fg = this.get('content').get('functionGroup');
		if(fg.subs && fg.subs.length>0){
			return true;
		}
		
		if(fg.url){
			open(fg.url);
		}else{
			open(fg.id+".html");
		}
		return false;
	},	
			
	 toggleButton: Flame.DisclosureView.extend({
		classNames: ['flame-tree-view-toggle'],
		ignoreLayoutManager:true,
        useAbsolutePosition:false,
        acceptsKeyResponder:false,
        visibilityTargetBinding:'parentView.isExpanded',
        action:function() {return false;},
        imageExpanded: Flame.image('disclosure_triangle_down.png'),
		imageCollapsed:Flame.image('disclosure_triangle_left.png')
    })
});

Wms.FunctionGroupContentTreeView=Flame.TreeView.extend({
	allowReordering:false,
	defaultIsExpanded:true,
	functionGroup:null,
	itemViewClass:Wms.FunctionGroupContentTreeItemView,
	handlebarsMap:{
		'Wms.FunctionGroupNode':'{{#if content.functionGroup.icon}}<img {{bindAttr src="content.functionGroup.icon"}}/>{{else}}<img src="static/images/wheel-icon.png"}}{{/if}} {{content.functionGroup.display}}', 
		'Wms.FunctionGroupLeaf':'{{#if content.functionGroup.icon}}<img {{bindAttr src="content.functionGroup.icon"}}/>{{else}}<img src="static/images/wheel-icon.png"}}{{/if}} {{content.functionGroup.display}}', 
		'defaultTemplate': '{{content}}'
	},
	
	initContent:function(){
		var fg = this.get('functionGroup');
		if(fg != null && fg.subs != null){	
			var content = [];
			for(var i = 0 ; i < fg.subs.length; i++){
				var sub=fg.subs[i];			
				if(sub.subs && sub.subs.length>0){
					var node=Wms.FunctionGroupNode.create({
						functionGroup:sub
					});
					content.push(node);		
					this._createNodeRecursively(node,sub); 
				}else{
					var node=Wms.FunctionGroupLeaf.create({
						functionGroup:sub
					});
					content.push(node);		
				}	
			}
			this.set('content',content);        
		}	
	}.observes('functionGroup'),
	
	_createNodeRecursively:function(parentNode, functionGroup){
		
		if(functionGroup.subs && functionGroup.subs.length >0){
			if(!parentNode.treeItemChildren || parentNode.treeItemChildren.length==0){
				parentNode.set('treeItemChildren',[]); 
			}
			for(var i = 0 ; i < functionGroup.subs.length; i++){
				var sub=functionGroup.subs[i];		
				if(sub.subs && sub.subs.length>0){
					var node=Wms.FunctionGroupNode.create({
						functionGroup:sub
					});
					this.createNodeRecursively(node,sub);
					parentNode.get('treeItemChildren').push(node);				 
				}else{
					var node=Wms.FunctionGroupLeaf.create({
						functionGroup:sub
					});
					parentNode.get('treeItemChildren').push(node);
				}		
			}
		}
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
				return false;
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
		elementId: 'frameView',
		controller: Wms.functionsConroller,
		leftWidth:330,
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
				autoHeight:true,
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
						if(functionGroups[index].subs && functionGroups[index].subs.length>=0){
							var contentView = Wms.FunctionGroupContentTreeView.create();
							contentView.set('functionGroup',functionGroups[index]);
							self.get('headers').pushObject(functionGroups[index].display); 
							self.get('contentViews').pushObject(contentView);
						}
					});	
					this._createTabs();
				}.observes('functionGroups')
			})
		})
	})	
});






 
 