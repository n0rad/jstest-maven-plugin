test( "hello test standalone", function() {
  ok( 1 == "1", "Passed!" );
});

module("sampleTest.js;module1");
test( "hello test1.0", function() {
	var element = new ElementMover();
  ok( 1 == "1", "Passed!" );
});

test( "hello test1.1", function() {
	  ok( 1 == "1", "Passed!" );
	});


module("module2");
test( "hello test2.0", function() {
  ok( 1 == "1", "yopla" );
});

test( "hello test2.1", function() {
	  ok( 1 == "1", "yopla" );
	});
