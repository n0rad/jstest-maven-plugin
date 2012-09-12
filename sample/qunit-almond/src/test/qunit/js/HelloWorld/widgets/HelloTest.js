/*global define, require */

require([ "jquery", "HelloWorld/widgets/HelloWorldWidget" ], function($, HelloWorldWidget) {

	//var equal = QUnit.equal, expect = QUnit.expect, test = QUnit.test;

	/**
	 * Test that the setMainContent method sets the text in the category-widget
	 */

	module("HelloWorldWidget.js;HelloWorldWidget");
	test("HelloWorld widget unit test case.", function() {

		var helloWorldWidget, output1;

		// Setup view and call method under test
		helloWorldWidget = new HelloWorldWidget();

		helloWorldWidget.helloWorldText = "HelloWorld Test";

		output1 = helloWorldWidget.setMainContent();

		// alert(output1.html());
		// Expect that the text was set on the expected element
		equal(output1.html(), "HelloWorld Test", "Expected text set in category-widget");
	});
});