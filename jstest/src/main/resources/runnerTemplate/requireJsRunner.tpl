<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>JsTest Runner</title>
    <script type="text/javascript">
            exports = window;
            QUnit = exports;
    </script>	
	
    <script type="text/javascript" src="/runnerResource/jstest-common.js"></script>
	
   $testResources$
	
</head>
<body>
	<script type="text/javascript">

        var tests = $testsJsArray$;


		require.config({baseUrl: 'src',  paths: {test: 'test'}});

/*
            $if(priority)$
            , priority: $priority$
            $endif$
            $if(customRunnerConfiguration)$
            , $customRunnerConfiguration$
            $endif$*/


        var testManager = new TestManager($debug$, $serverMode$, $browserId$, "$runId$", $emulator$);


        require($amdPreload$, function() {
            var tt = require(tests, function(xx) {
              testManager.run();
            });
        });
        
	</script>
</body>
</html>