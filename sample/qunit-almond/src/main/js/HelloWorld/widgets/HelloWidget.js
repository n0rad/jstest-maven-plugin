/*global define, $ window */

define( "HelloWorld/widgets/HelloWorldWidget", [ "jquery" ], function($) {

    function HelloWorldWidget() {
    }

    HelloWorldWidget.prototype.helloWorldText = undefined;
    
    HelloWorldWidget.prototype.setMainContent = function(){
        var container = $('<div class="container">');
        container.append(this.helloWorldText);
        $('.helloworld-widget').append(container);
        return container;
    };

 return HelloWorldWidget;
});
