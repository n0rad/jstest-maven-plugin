/*global require */

require([ "jquery-1.7.2", "HelloWorld/widgets/HelloWorldWidget" ], function($, HelloWorldWidget) {

	//var equal = QUnit.equal, expect = QUnit.expect, test = QUnit.test;

	/**
	 * Test that the setMainContent method sets the text in the category-widget
	 */
	test("HelloWorld widget unite test case.", function() {

		var helloWorldWidget, output1;

		// Setup view and call method under test
		helloWorldWidget = new HelloWorldWidget();

		helloWorldWidget.HelloWorldText = "HelloWorld Test";

		output1 = helloWorldWidget.setMainContent();

		// Expect that the text was set on the expected element
		equal(output1.html(), "HelloWorld Test",	"Expected text not set in category-widget");
	});
});