// An example of using Flame.TreeView. You can reorder items by
// dragging them around (note that also horizontal dragging makes a
// difference when there's ambiguity about dropping inside a folder).
// You can drop items inside a folder only when it's open.

App = Ember.Application.create();

App.Folder = Ember.Object.extend({
    treeItemIsExpanded: false,  // Default to not expanded
    name: null
});

App.File = Ember.Object.extend({
    name: null
});

App.someController = Ember.Object.create({
    tree: [
        App.Folder.create({
            name: 'Folder 1',
            treeItemChildren: [
                App.File.create({ name: 'File 1' }),
                App.File.create({ name: 'File 2' }),
                App.File.create({ name: 'File 3', treeItemChildren: [
                App.File.create({ name: 'File 4' }),
                App.File.create({ name: 'File 5' })
            ] })
            ]
        }),
        App.Folder.create({
            name: 'Folder 2', treeItemChildren: []
        }),
        App.Folder.create({
            name: 'Folder 3',
            treeItemChildren: [
                App.File.create({ name: 'File 4' }),
                App.File.create({ name: 'File 5' })
            ]
        }),
    ]            
});

App.RootView = Flame.RootView.extend({
    childViews: ['treeView'],

    treeView: Flame.TreeView.extend({
        contentBinding: 'App.someController.tree',
        allowReordering: true,  // Set to false to disallow reordering
        handlebarsMap: {
            'App.Folder': '{{content.name}} ({{content.treeItemChildren.length}} files)',
            'defaultTemplate': '{{content.name}}'
        }                
    })       
});