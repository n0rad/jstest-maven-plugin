module("GreeterTest");

test("test greet", function() {
    var greeter = new myapp.Greeter();
    ok( greeter.greet("World") == "Hello World!", "Passed!" );
    //assertEquals("Hello World!", greeter.greet("World"));
});

test("test greet2", function() {
    var greeter = new myapp.Greeter();
    ok( greeter.greet("World!") != "Hello World!", "Passed!" );
    //assertEquals("Hello World!", greeter.greet("World"));
});
