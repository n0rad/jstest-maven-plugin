<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>JsTest Runner</title>
	<script type="text/javascript">curl = {baseUrl: 'src', paths : {test : 'test'}};</script>

	$testResources$

</head>
<body>
	<script type="text/javascript">
		var require = curl;
		var tests = $testsJsArray$;
		
		var testManager = new TestManager($debug$, $serverMode$, $browserId$, "$runId$", $emulator$);

		var testFileLoaded = 0;
		var loaded = function() {
			testFileLoaded++;
			if (testFileLoaded == tests.length) {
				testManager.run();
			}
		};

		require($amdPreload$, function() {
			require(tests);
		});
	</script>
</body>
</html>